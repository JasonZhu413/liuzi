/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package com.liuzi.fastdfs.boot.base;

import org.csource.common.IniFileReader;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

  public static final String PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS = "fastdfs.connect_timeout_in_seconds";
  public static final String PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS = "fastdfs.network_timeout_in_seconds";
  public static final String PROP_KEY_CHARSET = "fastdfs.charset";
  public static final String PROP_KEY_HTTP_ANTI_STEAL_TOKEN = "fastdfs.http_anti_steal_token";
  public static final String PROP_KEY_HTTP_SECRET_KEY = "fastdfs.http_secret_key";
  public static final String PROP_KEY_HTTP_TRACKER_HTTP_PORT = "fastdfs.http_tracker_http_port";
  public static final String PROP_KEY_TRACKER_SERVERS = "fastdfs.tracker_servers";

  public static int g_connect_timeout;
  public static int g_network_timeout;
  public static String g_charset;
  public static boolean g_anti_steal_token;
  public static String g_secret_key;
  public static int g_tracker_http_port;
  public static int g_file_upload_max_size;
  public static int g_connection_pool_size;
  public static String fileServer;
  public static String trackerServer;
  
  public static TrackerGroup g_tracker_group;

  private ClientGlobal() {}

  /**
   * load global variables
   *
   * @param conf_filename config filename
   */
  public static void init() throws IOException, MyException {
    String[] szTrackerServers = trackerServer.split(",");
    String[] parts;

    InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
    for (int i = 0; i < szTrackerServers.length; i++) {
      parts = szTrackerServers[i].split("\\:", 2);
      if (parts.length != 2) {
        throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
      }

      tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
    }
    g_tracker_group = new TrackerGroup(tracker_servers);
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
