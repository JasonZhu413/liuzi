/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package com.liuzi.fastdfs.base;

import net.coobird.thumbnailator.geometry.Positions;

import org.csource.common.IniFileReader;
import org.csource.common.MyException;
import org.springframework.util.StringUtils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Global variables
 *
 * @author Happy Fish / YuQing
 * @version Version 1.11
 */
public class ClientGlobal {

  public static final String CONF_KEY_CONNECT_TIMEOUT = "connect_timeout";
  public static final String CONF_KEY_NETWORK_TIMEOUT = "network_timeout";
  public static final String CONF_KEY_CHARSET = "charset";
  public static final String CONF_KEY_HTTP_ANTI_STEAL_TOKEN = "http.anti_steal_token";
  public static final String CONF_KEY_HTTP_SECRET_KEY = "http.secret_key";
  public static final String CONF_KEY_HTTP_TRACKER_HTTP_PORT = "http.tracker_http_port";
  public static final String CONF_KEY_TRACKER_SERVER = "tracker_server";

  public static final String PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS = "fastdfs.connect_timeout_in_seconds";
  public static final String PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS = "fastdfs.network_timeout_in_seconds";
  public static final String PROP_KEY_CHARSET = "fastdfs.charset";
  public static final String PROP_KEY_HTTP_ANTI_STEAL_TOKEN = "fastdfs.http_anti_steal_token";
  public static final String PROP_KEY_HTTP_SECRET_KEY = "fastdfs.http_secret_key";
  public static final String PROP_KEY_HTTP_TRACKER_HTTP_PORT = "fastdfs.http_tracker_http_port";
  public static final String PROP_KEY_TRACKER_SERVERS = "fastdfs.tracker_servers";

  public static final int DEFAULT_CONNECT_TIMEOUT = 5; //second
  public static final int DEFAULT_NETWORK_TIMEOUT = 30; //second
  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final boolean DEFAULT_HTTP_ANTI_STEAL_TOKEN = false;
  public static final String DEFAULT_HTTP_SECRET_KEY = "FastDFS1234567890";
  public static final int DEFAULT_HTTP_TRACKER_HTTP_PORT = 80;
  
  public static final int DEFAULT_FILE_UPLOAD_MAX_SIZE = 0;
  public static final int DEFAULT_CONNECTION_POOL_SIZE = 8;

  public static int g_connect_timeout = DEFAULT_CONNECT_TIMEOUT * 1000; //millisecond
  public static int g_network_timeout = DEFAULT_NETWORK_TIMEOUT * 1000; //millisecond
  public static String g_charset = DEFAULT_CHARSET;
  public static boolean g_anti_steal_token = DEFAULT_HTTP_ANTI_STEAL_TOKEN; //if anti-steal token
  public static String g_secret_key = DEFAULT_HTTP_SECRET_KEY; //generage token secret key
  public static int g_tracker_http_port = DEFAULT_HTTP_TRACKER_HTTP_PORT;
  
  public static int g_file_upload_max_size = DEFAULT_FILE_UPLOAD_MAX_SIZE;
  public static int g_connection_pool_size = DEFAULT_CONNECTION_POOL_SIZE;
  public static String fileServer = null;
  public static TrackerGroup g_tracker_group;
  
  /**
   * 是否压缩
   */
  public static boolean g_compress = false;
  /**
   * 缩略图透明度
   */
  public static float g_quality = 1f;
  /**
   * 缩略图原图比例
   */
  public static float g_compressScaleLarge = 1f;
  /**
   * 缩略图中图比例
   */
  public static float g_compressScaleMiddle = 0.7f;
  /**
   * 缩略图小图比例
   */
  public static float g_compressScaleSmall = 0.4f;
  
  /**
   * 是否添加水印
   */
  public static boolean g_watermark = false;
  /**
   * 水印位置
   */
  public static Positions g_watermark_positions = Positions.BOTTOM_RIGHT;
  /**
   * 水印透明度
   */
  public static float g_watermark_quality = 1f;
  /**
   * 图片水印
   */
  public static boolean g_watermark_img = true;
  /**
   * 图片水印目录
   */
  public static String g_watermark_img_path;
  /**
   * 图片水印宽
   */
  public static int g_watermark_img_width = 100;
  /**
   * 图片水印高
   */
  public static int g_watermark_img_height = 50;
  /**
   * 文字水印字体大小
   */
  public static int g_watermark_font_size = 15;
  /**
   * 文字水印字体颜色
   */
  public static Color g_watermark_font_color = Color.red;
  
  
  private ClientGlobal() {}

  /**
   * load global variables
   *
   * @param conf_filename config filename
   */
  public static void init(String conf_filename) throws IOException, MyException {
    IniFileReader iniReader;
    String[] szTrackerServers;
    String[] parts;

    iniReader = new IniFileReader(conf_filename);

    g_connect_timeout = iniReader.getIntValue("connect_timeout", DEFAULT_CONNECT_TIMEOUT);
    if (g_connect_timeout < 0) {
      g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
    }
    g_connect_timeout *= 1000; //millisecond

    g_network_timeout = iniReader.getIntValue("network_timeout", DEFAULT_NETWORK_TIMEOUT);
    if (g_network_timeout < 0) {
      g_network_timeout = DEFAULT_NETWORK_TIMEOUT;
    }
    g_network_timeout *= 1000; //millisecond

    g_charset = iniReader.getStrValue("charset");
    if (g_charset == null || g_charset.length() == 0) {
      g_charset = "ISO8859-1";
    }

    String ts = iniReader.getStrValue("tracker_server");
    if (ts == null) {
    	throw new MyException("item \"tracker_server\" in " + conf_filename + " not found");
    }
    
    szTrackerServers = ts.trim().split(",");
    if (szTrackerServers == null || szTrackerServers.length == 0) {
        throw new MyException("item \"tracker_server\" in " + conf_filename + " not found");
    }
    /*szTrackerServers = iniReader.getValues("tracker_server");
    if (szTrackerServers == null) {
      throw new MyException("item \"tracker_server\" in " + conf_filename + " not found");
    }*/

    InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
    for (int i = 0; i < szTrackerServers.length; i++) {
      parts = szTrackerServers[i].split("\\:", 2);
      if (parts.length != 2) {
        throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
      }

      tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
    }
    g_tracker_group = new TrackerGroup(tracker_servers);

    g_tracker_http_port = iniReader.getIntValue("http.tracker_http_port", 80);
    g_anti_steal_token = iniReader.getBoolValue("http.anti_steal_token", false);
    if (g_anti_steal_token) {
      g_secret_key = iniReader.getStrValue("http.secret_key");
    }
    
    //新增 最大下载大小M，默认0无限制
    g_file_upload_max_size = iniReader.getIntValue("upload.max.size", DEFAULT_FILE_UPLOAD_MAX_SIZE);
    //新增 初始化连接池数 默认8
    g_connection_pool_size = iniReader.getIntValue("connect.pool.size", DEFAULT_CONNECTION_POOL_SIZE);
    //新增 访问上传资源地址
    fileServer = iniReader.getStrValue("file_server");
    if(StringUtils.isEmpty(fileServer)){
    	fileServer = "/";
    }else{
    	String lastStr = fileServer.substring(fileServer.length() - 1);
    	String xg = "/";
    	if(!xg.equals(lastStr)){
    		fileServer += xg;
    	}
    }
    
    
    g_compress = iniReader.getBoolValue("image.compress", false);
    
    String quality = iniReader.getStrValue("image.quality");
    if(!StringUtils.isEmpty(quality)){
    	g_quality = Float.parseFloat(quality);
    }
    String compressScaleLarge = iniReader.getStrValue("image.compress.scale.large");
    if(!StringUtils.isEmpty(compressScaleLarge)){
    	g_compressScaleLarge = Float.parseFloat(compressScaleLarge);
    }
    String compressScaleMiddle = iniReader.getStrValue("image.compress.scale.middle");
    if(!StringUtils.isEmpty(compressScaleMiddle)){
    	g_compressScaleMiddle = Float.parseFloat(compressScaleMiddle);
    }
    String compressScaleSmall = iniReader.getStrValue("image.compress.scale.small");
    if(!StringUtils.isEmpty(compressScaleSmall)){
    	g_compressScaleSmall = Float.parseFloat(compressScaleSmall);
    }
    
    g_watermark = iniReader.getBoolValue("image.watermark", false);
    //水印位置
    //g_watermark_positions
    String watermarkQuality = iniReader.getStrValue("image.watermark.quality");
    if(!StringUtils.isEmpty(watermarkQuality)){
    	g_watermark_quality = Float.parseFloat(watermarkQuality);
    }
    g_watermark_img = iniReader.getBoolValue("image.watermark.img", true);
    g_watermark_img_path = iniReader.getStrValue("image.watermark.img.path");
    g_watermark_img_width = iniReader.getIntValue("image.watermark.img.width", 100);
    g_watermark_img_height = iniReader.getIntValue("image.watermark.img.height", 50);
    g_watermark_font_size = iniReader.getIntValue("image.watermark.font.size", 15);
    //文字水印字体颜色
    //g_watermark_font_color
    
    
  }

  /**
   * load from properties file
   *
   * @param propsFilePath properties file path, eg:
   *                      "fastdfs-client.properties"
   *                      "config/fastdfs-client.properties"
   *                      "/opt/fastdfs-client.properties"
   *                      "C:\\Users\\James\\config\\fastdfs-client.properties"
   *                      properties文件至少包含一个配置项 fastdfs.tracker_servers 例如：
   *                      fastdfs.tracker_servers = 10.0.11.245:22122,10.0.11.246:22122
   *                      server的IP和端口用冒号':'分隔
   *                      server之间用逗号','分隔
   */
  public static void initByProperties(String propsFilePath) throws IOException, MyException {
    Properties props = new Properties();
    InputStream in = IniFileReader.loadFromOsFileSystemOrClasspathAsStream(propsFilePath);
    if (in != null) {
      props.load(in);
    }
    initByProperties(props);
  }

  public static void initByProperties(Properties props) throws IOException, MyException {
    String trackerServersConf = props.getProperty(PROP_KEY_TRACKER_SERVERS);
    if (trackerServersConf == null || trackerServersConf.trim().length() == 0) {
      throw new MyException(String.format("configure item %s is required", PROP_KEY_TRACKER_SERVERS));
    }
    initByTrackers(trackerServersConf.trim());

    String connectTimeoutInSecondsConf = props.getProperty(PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS);
    String networkTimeoutInSecondsConf = props.getProperty(PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS);
    String charsetConf = props.getProperty(PROP_KEY_CHARSET);
    String httpAntiStealTokenConf = props.getProperty(PROP_KEY_HTTP_ANTI_STEAL_TOKEN);
    String httpSecretKeyConf = props.getProperty(PROP_KEY_HTTP_SECRET_KEY);
    String httpTrackerHttpPortConf = props.getProperty(PROP_KEY_HTTP_TRACKER_HTTP_PORT);
    if (connectTimeoutInSecondsConf != null && connectTimeoutInSecondsConf.trim().length() != 0) {
      g_connect_timeout = Integer.parseInt(connectTimeoutInSecondsConf.trim()) * 1000;
    }
    if (networkTimeoutInSecondsConf != null && networkTimeoutInSecondsConf.trim().length() != 0) {
      g_network_timeout = Integer.parseInt(networkTimeoutInSecondsConf.trim()) * 1000;
    }
    if (charsetConf != null && charsetConf.trim().length() != 0) {
      g_charset = charsetConf.trim();
    }
    if (httpAntiStealTokenConf != null && httpAntiStealTokenConf.trim().length() != 0) {
      g_anti_steal_token = Boolean.parseBoolean(httpAntiStealTokenConf);
    }
    if (httpSecretKeyConf != null && httpSecretKeyConf.trim().length() != 0) {
      g_secret_key = httpSecretKeyConf.trim();
    }
    if (httpTrackerHttpPortConf != null && httpTrackerHttpPortConf.trim().length() != 0) {
      g_tracker_http_port = Integer.parseInt(httpTrackerHttpPortConf);
    }
  }

  /**
   * load from properties file
   *
   * @param trackerServers 例如："10.0.11.245:22122,10.0.11.246:22122"
   *                       server的IP和端口用冒号':'分隔
   *                       server之间用逗号','分隔
   */
  public static void initByTrackers(String trackerServers) throws IOException, MyException {
    List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
    String spr1 = ",";
    String spr2 = ":";
    String[] arr1 = trackerServers.trim().split(spr1);
    for (String addrStr : arr1) {
      String[] arr2 = addrStr.trim().split(spr2);
      String host = arr2[0].trim();
      int port = Integer.parseInt(arr2[1].trim());
      list.add(new InetSocketAddress(host, port));
    }
    InetSocketAddress[] trackerAddresses = list.toArray(new InetSocketAddress[list.size()]);
    initByTrackers(trackerAddresses);
  }

  public static void initByTrackers(InetSocketAddress[] trackerAddresses) throws IOException, MyException {
    g_tracker_group = new TrackerGroup(trackerAddresses);
  }

  /**
   * construct Socket object
   *
   * @param ip_addr ip address or hostname
   * @param port    port number
   * @return connected Socket object
   */
  public static Socket getSocket(String ip_addr, int port) throws IOException {
    Socket sock = new Socket();
    sock.setSoTimeout(ClientGlobal.g_network_timeout);
    sock.connect(new InetSocketAddress(ip_addr, port), ClientGlobal.g_connect_timeout);
    return sock;
  }

  /**
   * construct Socket object
   *
   * @param addr InetSocketAddress object, including ip address and port
   * @return connected Socket object
   */
  public static Socket getSocket(InetSocketAddress addr) throws IOException {
    Socket sock = new Socket();
    sock.setSoTimeout(ClientGlobal.g_network_timeout);
    sock.connect(addr, ClientGlobal.g_connect_timeout);
    return sock;
  }

  public static int getG_connect_timeout() {
    return g_connect_timeout;
  }

  public static void setG_connect_timeout(int connect_timeout) {
    ClientGlobal.g_connect_timeout = connect_timeout;
  }

  public static int getG_network_timeout() {
    return g_network_timeout;
  }

  public static void setG_network_timeout(int network_timeout) {
    ClientGlobal.g_network_timeout = network_timeout;
  }

  public static String getG_charset() {
    return g_charset;
  }

  public static void setG_charset(String charset) {
    ClientGlobal.g_charset = charset;
  }

  public static int getG_tracker_http_port() {
    return g_tracker_http_port;
  }

  public static void setG_tracker_http_port(int tracker_http_port) {
    ClientGlobal.g_tracker_http_port = tracker_http_port;
  }

  public static boolean getG_anti_steal_token() {
    return g_anti_steal_token;
  }

  public static boolean isG_anti_steal_token() {
    return g_anti_steal_token;
  }

  public static void setG_anti_steal_token(boolean anti_steal_token) {
    ClientGlobal.g_anti_steal_token = anti_steal_token;
  }

  public static String getG_secret_key() {
    return g_secret_key;
  }

  public static void setG_secret_key(String secret_key) {
    ClientGlobal.g_secret_key = secret_key;
  }

  public static TrackerGroup getG_tracker_group() {
    return g_tracker_group;
  }

  public static void setG_tracker_group(TrackerGroup tracker_group) {
    ClientGlobal.g_tracker_group = tracker_group;
  }

  public static String configInfo() {
    String trackerServers = "";
    if (g_tracker_group != null) {
      InetSocketAddress[] trackerAddresses = g_tracker_group.tracker_servers;
      for (InetSocketAddress inetSocketAddress : trackerAddresses) {
        if(trackerServers.length() > 0) trackerServers += ",";
        trackerServers += inetSocketAddress.toString().substring(1);
      }
    }
    return "{"
      + "\n  g_connect_timeout(ms) = " + g_connect_timeout
      + "\n  g_network_timeout(ms) = " + g_network_timeout
      + "\n  g_charset = " + g_charset
      + "\n  g_anti_steal_token = " + g_anti_steal_token
      + "\n  g_secret_key = " + g_secret_key
      + "\n  g_tracker_http_port = " + g_tracker_http_port
      + "\n  trackerServers = " + trackerServers
      + "\n}";
  }

}
