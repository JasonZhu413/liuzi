package com.liuzi.util.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
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
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.liuzi.util.common.Log;


public class HttpUtil {
	
	/**
	 * 请求超时时间（毫秒）
	 * 默认6秒
	 */
	public static final int TIMEOUT = 6000;
	/**
	 * 最大连接数，默认200
	 */
	public static final int MAX_TOTAL = 200;
    /**
	 * 每个路由的基础连接，默认50
	 */
	public static final int MAX_PER_ROUTE = 50;
    /**
	 * 目标主机的最大连接数，默认100
	 */
	public static final int MAX_ROUTE = 100;
	/**
	 * 重试次数，默认0
	 */
	public static final int RETRY = 0;
	
    
    @Getter @Setter
    private CloseableHttpClient closeableHttpClient;
    
    private int maxRoute = MAX_ROUTE;
    
    private boolean debug = false;
    
    public HttpUtil(){
    	this(TIMEOUT, MAX_TOTAL, MAX_PER_ROUTE, RETRY);
    }

	public HttpUtil(int timeout, int maxTotal, int maxPerRoute, int retry){
		timeout = timeout > 0 ? timeout : TIMEOUT;
		maxTotal = maxTotal > 0 ? maxTotal : MAX_TOTAL;
		maxPerRoute = maxPerRoute > 0 ? maxPerRoute : MAX_PER_ROUTE;
		final int retryCount = retry <= 0 ? 0 : RETRY;
		
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
		
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = 
				new PoolingHttpClientConnectionManager(registry);
		poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
		
		RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
        
        //请求重试处理 httpRequestRetryHandler
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                    int executionCount, HttpContext context) {
                if (executionCount >= retryCount) {//重试
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
        
        /*String hostname = url.split("/")[2];
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
    	
        HttpHost httpHost = new HttpHost(hostname, port);
        poolingHttpClientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);*/
        
        this.closeableHttpClient = HttpClients.custom()
        		.setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
	}

	/**
	 * 设置目标主机的最大连接数
	 * @param maxRoute
	 */
    public void setMaxRoute(int maxRoute) {
    	if(maxRoute > 0){
    		this.maxRoute = maxRoute;
    	}
	}
    
    /**
     * 是否打印
     * @param debug
     */
    public void setDebug(boolean debug) {
		this.debug = debug;
	}
    
    private void log(Object msg){
    	if(debug){
    		Log.debug(msg + "");
    	}
    }
    
    private void error(Exception e, Object msg, Object... params){
    	Log.error(e, msg + "", params);
    }
    
    /**
     * POST请求
     */
    public <T> T post(String url, Object params){
    	return response(getHttpPost(url, params, false));
    }

    /**
     * POST请求
     */
	public <T> T postByJson(String url, Object params){
        return response(getHttpPost(url, params, true));
    }
    
    /**
     * 参数
     */
    @SuppressWarnings("unchecked")
	private HttpPost getHttpPost(String url, Object params, boolean json) {
    	if(url == null){
    		throw new IllegalArgumentException("url is null");
    	}
    	
    	log("Httpclient post start, type is json: " + json + "...");
    	log("Url: " + url);
    	log("Params: " + params);
    	
    	/*try {
			url = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			error(e, "POST地址编码转换错误{}", url);
		}*/
    	
    	HttpPost httpPost = new HttpPost(url);
    	if(json){
    		httpPost.setHeader(HttpHeaders.CONNECTION, "close");
        	httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
    	}
    	
    	if(params == null){
    		return httpPost;
    	}
    	
    	if(json){
    		try {
	            if(params instanceof Map){
	            	params = JSONObject.fromObject(((Map<String, Object>) params));
				}
	            httpPost.setEntity(getStringEntry(params.toString()));
				return httpPost;
    		}catch(Exception e){
    			error(e, "POST参数组装错误{}", params);
    		}
    	}
    	
        try {
			if(params instanceof Map){
				List<NameValuePair> list = new ArrayList<>();
            	for(Entry<String, Object> entry : ((Map<String, Object>) params).entrySet()){
            		list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            	}
            	httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
			}else{
				httpPost.setEntity(getStringEntry(params.toString()));
			}
        } catch (Exception e) {
        	error(e, "POST参数组装错误{}", params);
        }
        
        return httpPost;
    }
    
    private StringEntity getStringEntry(String params){
    	StringEntity entity = new StringEntity(params, "UTF-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		return entity;
    }
    
	@SuppressWarnings("unchecked")
	private <T> T response(HttpPost httpPost){
    	CloseableHttpResponse response = null;
        
        try {
        	response = closeableHttpClient.execute(httpPost);
        			//HttpClientContext.create());
            response.setHeader(HttpHeaders.CONNECTION, "close");
            
            int code = response.getStatusLine().getStatusCode();
            
            log("response code:" + code);
            
            if(code != HttpStatus.SC_OK){
            	httpPost.abort();
            }
            
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            
            log("response result:" + result);
            
            return (T) result;
        } catch (Exception e) {
        	httpPost.abort();
        	error(e, "POST请求错误");
        } finally {
        	if (response != null){
        		try {
					response.close();
				} catch (IOException e) {
					error(e, "POST请求response关闭错误");
				}
            }
        	httpPost.releaseConnection();
            
            log("http post end");
        }
        
        return null;
    }
	
	/**
     * GET请求
     */
    public <T> T get(String url) {
    	System.out.println(this.closeableHttpClient == null);
        return get(url, null);
    }
    
    /**
     * GET请求
     */
    @SuppressWarnings("unchecked")
	public <T> T get(String url, Map<String, Object> params) {
    	if(url == null){
    		throw new IllegalArgumentException("url is null");
    	}
    	
    	StringBuffer sbf = null;
        if(params != null && !params.isEmpty()){
        	sbf = new StringBuffer();
        	for(Entry<String, Object> entry : params.entrySet()){
        		sbf.append("&" + entry.getKey() + "=" + entry.getValue());
        	}
        }
        
        if(sbf != null){
        	String[] infos = url.split("?");
            //有问号
            if(infos.length > 1){
            	url += StringUtils.isEmpty(infos[1]) ? sbf.substring(1, sbf.length()) : sbf.toString();
            }else{
            	url += "?" + sbf.substring(1, sbf.length());
            }
        }
        
    	try {
			url = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			error(e, "GET请求编码转换错误 {}", url);
		}
    	
        HttpGet httpGet = new HttpGet(url);
        
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();

            log("response code:" + code);
            
            if(code != HttpStatus.SC_OK){
            	httpGet.abort();
            }
            
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);

            log("response result:" + result);

            return (T) result;
        } catch (IOException e) {
        	httpGet.abort();
        	error(e, "GET请求错误");
        } finally {
            try {
                if (response != null){
                	response.close();
                }
                httpGet.releaseConnection();
            } catch (IOException e) {
            	error(e, "GET请求，response关闭错误");
            }
        }
        
        return null;
    }
}
