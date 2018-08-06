package com.liuzi.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Map;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;


public class HttpClientUtil {

    
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
    	System.out.println("params:" + params);
    	if(params != null){
            try {
            	StringEntity se = new StringEntity(JSONObject.fromObject(params).toString());
                se.setContentType("text/json");
                hp.setEntity(se);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    	}
    	
    	System.out.println("init params...");
    }

    /**
     * post
     */
    public static String post(String url, Map<String, Object> params){
    	System.out.println("*********** httpclient post begin ***********");
    	
    	HttpPost hp = new HttpPost(url);
    	hp.setHeader("Content-Type", "application/json;charset=UTF-8");
    	hp.setHeader(HttpHeaders.CONNECTION, "close"); 
    	
    	getHttpPost(hp, params);//
        
        CloseableHttpResponse response = null;
        String result = null;
        
        CloseableHttpClient hc = getHttpClient(url);//获取httpclient
        try {
        	System.out.println("execute..." );
            response = hc.execute(hp, HttpClientContext.create());//执行excute
            response.setHeader(HttpHeaders.CONNECTION, "close");
            
            int code = response.getStatusLine().getStatusCode();//返回code
            System.out.println("response code:" + code);
            if(code != HttpStatus.SC_OK){
            	hp.abort();
            }
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            System.out.println("consume entity。。。");
        } catch (Exception e) {
        	hp.abort();
            e.printStackTrace();
            System.out.println("catch httppost error：" + e.getMessage());
        } finally {
        	if (response != null){
        		try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        	hp.releaseConnection();
            
            System.out.println("response/httppost close。。。");
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
    	String url = "http://10.1.1.212/CGPush/PublishAction/publish";
    	JSONObject obj = new JSONObject();
    	obj.put("bidtype", 0);
    	obj.put("tflag", "netbid");
    	obj.put("netprice", 0);
    	obj.put("netbidnumber", null);
    	obj.put("iscanbid", true);
    	obj.put("itemcode", 3587);
    	obj.put("bid_status", 3);
    	obj.put("lastpublish", 1528191082528L);
    	obj.put("name", "米歇尔?诺伊拉父子酒园梧玖特级田干红2004年份");
    	obj.put("nextprice", 500);
    	obj.put("time", "08:00:00");
    	Map<String, Object> params = new HashMap<>();
    	params.put("msg", obj.toString());
    	params.put("channelName", 1);
    	
    	/*for(int i = 0; i < 1000; i ++){
    		String ent = post(url, params);
        	System.out.println(i+"返回：" + ent);
    	}*/
    	test(url, params);
	}
    
    public static void test(String url, Map<String, Object> params) {
    	TestRun t1 = new TestRun("线程1", url, params);
    	TestRun t2 = new TestRun("线程2", url, params);
    	TestRun t3 = new TestRun("线程3", url, params);
    	TestRun t4 = new TestRun("线程4", url, params);
    	TestRun t5 = new TestRun("线程5", url, params);
    	t1.start();
    	t2.start();
    	t3.start();
    	t4.start();
    	t5.start();
    }
    
    public static class TestRun extends Thread {
    	private String name;
        private String url;
        private Map<String, Object> params;
        
        public TestRun(String name, String url, 
        		Map<String, Object> params) {
        	this.name = name;
        	this.url = url;
        	this.params = params;
        }
        
        @Override
        public void run() {
        	for(int i = 0; i < 100; i++){
        		String ent = post(url, params);
        		System.out.println(name + "["+(i+1)+"]结束， 返回：" + ent);
        	}
        }
    }
    
    /*public synchronized static String post(String url, Map<String, Object> params){
    	System.out.println("*********** post begin ***************");
    	String p = "";
    	if(params != null){
    		p = JSONObject.fromObject(params).toString();
    	}
        
        try {
        	URL postUrl = new URL(url);
        	System.out.println("create url...");
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            System.out.println("open connection...");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            //connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.setRequestProperty("accept","application/json");
            System.out.println("set property...");
            connection.connect();
            System.out.println("connect...");
            OutputStream out = connection.getOutputStream();
            out.write(p.getBytes());
            out.flush();
            out.close();
            System.out.println("output...");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            System.out.println("create reader...");
            String line;
            StringBuffer result = new StringBuffer("");
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("read line...");
            reader.close();
            connection.disconnect();
            System.out.println("reader/connection disconnect...");
            System.out.println("*********** post end ***************");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }*/
}
