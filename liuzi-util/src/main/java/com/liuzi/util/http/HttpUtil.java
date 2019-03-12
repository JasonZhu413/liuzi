package com.liuzi.util.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


public class HttpUtil {
    
    private static CloseableHttpClient httpClient = null;

    private final static Object syncLock = new Object();
    private final static int timeOut = 10000;//超时时间 10秒
    private final static int maxTotal = 200;//最大连接数
    private final static int maxPerRoute = 50;//每个路由的基础连接
    private final static int maxRoute = 100;//目标主机的最大连接数

    /**
     * getHttpClient
     */
    public static CloseableHttpClient getHttpClient(String url) {
    	if(httpClient == null){
    		synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = init(url);
                }
            }
    	}
        return httpClient;
    }
    
    /**
     * 创建HttpClient对象
     * 
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient init(String url) {
    	String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
    	
    	Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
    	
        HttpHost httpHost = new HttpHost(hostname, port);
        
        //cm
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
        
        //requestConfig
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeOut)
                .setConnectTimeout(timeOut)
                .setSocketTimeout(timeOut)
                .build();
        
        //请求重试处理 httpRequestRetryHandler
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                    int executionCount, HttpContext context) {
                if (executionCount >= 5) {//重试5次，放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {//服务器丢掉了连接，重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {//不重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {//超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {//目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {//连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {//SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                //请求是幂等，再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
        
        return httpClient;
    }

    /**
     * 参数
     */
    private static void getHttpPost(HttpPost hp, Map<String, Object> params) {
    	System.out.println("init params...");
    	
    	System.out.println("params:" + params);
    	if(params != null){
            try {
            	
            	List<NameValuePair> list = new ArrayList<>();
            	for(Entry<String, Object> entry : params.entrySet()){
            		list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            	}
            	hp.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    	}
    }

    /**
     * post
     */
    public static String post(String url, Map<String, Object> params){
    	System.out.println("*********** httpclient post begin ***********");
    	
    	HttpPost hp = new HttpPost(url);
    	//hp.setHeader("Content-Type", "application/json;charset=UTF-8");
    	hp.setHeader(HttpHeaders.CONNECTION, "close"); 
    	
    	getHttpPost(hp, params);//
        
        CloseableHttpResponse response = null;
        String result = null;
        
        CloseableHttpClient hc = getHttpClient(url);//获取httpclient
        try {
        	System.out.println("request execute..." );
            response = hc.execute(hp, HttpClientContext.create());//执行excute
            response.setHeader(HttpHeaders.CONNECTION, "close");
            
            int code = response.getStatusLine().getStatusCode();//返回code
            System.out.println("response, code:" + code);
            if(code != HttpStatus.SC_OK){
            	hp.abort();
            }
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            System.out.println("consume entity, result:" + result);
        } catch (Exception e) {
        	hp.abort();
            e.printStackTrace();
            System.out.println("http post error：" + e.getMessage());
        } finally {
        	if (response != null){
        		try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        	hp.releaseConnection();
            
            System.out.println("http post close...");
        }
        
        System.out.println("*********** httpclient post end ***********");
        return result;
    }
    
    /**
     * GET请求URL获取内容
     * 
     * @param url
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static String get(String url) {
    	System.out.println("*********** httpclient get begin ***********");
    	
        HttpGet httpget = new HttpGet(url);
        
        CloseableHttpResponse response = null;
        String result = null;
        
        try {
        	System.out.println("execute..." );
            response = getHttpClient(url).execute(httpget,
                    HttpClientContext.create());
            int code = response.getStatusLine().getStatusCode();//返回code
            System.out.println("response code:" + code);
            if(code != HttpStatus.SC_OK){
            	httpget.abort();
            }
            
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            System.out.println("consume......");
        } catch (IOException e) {
        	httpget.abort();
            e.printStackTrace();
            System.out.println("catch httpget error：" + e.getMessage());
        } finally {
            try {
                if (response != null){
                	response.close();
                }
                
                httpget.releaseConnection();
                
                System.out.println("response close...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("response/httppost close。。。");
        }
        System.out.println("result：" + result);
        System.out.println("*********** httpclient get end ***********");
        return result;
    }
    
    public static void main(String[] args) {
    	
	}
}
