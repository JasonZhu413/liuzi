package com.liuzi.memcached;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import com.liuzi.util.common.DateUtil;

@Slf4j
public class MemcachedLog {
	private final static String MEMCACHED_LOG = "D:\\memcached.log";  
    private final static String LINUX_MEMCACHED_LOG = "/usr/local/logs/memcached.log";  
    private static FileWriter fileWriter;
    private static BufferedWriter logWrite;
    // 获取PID，可以找到对应的JVM进程  
    private final static RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
    private final static String PID = runtime.getName();  

    /** 
     * 初始化写入流 
     */  
    static {  
        try {  
            String osName = System.getProperty("os.name");  
            if (osName.indexOf("Windows") == -1) {  
                fileWriter = new FileWriter(MEMCACHED_LOG, true);  
            } else {  
                fileWriter = new FileWriter(LINUX_MEMCACHED_LOG, true);  
            }  
            logWrite = new BufferedWriter(fileWriter);  
        } catch (IOException e) {  
            log.error("memcached 日志初始化失败", e);  
            closeLogStream();  
        }  
    }  

    /** 
     * 写入日志信息 
     *  
     * @param content 
     *            日志内容 
     */  
    public static void writeLog(String content) {  
        try {  
            logWrite.write("[" + PID + "] " + "- [" + DateUtil.date2Str(new Date()) + "]\r\n"  
                    + content);  
            logWrite.newLine();  
            logWrite.flush();  
        } catch (IOException e) {  
        	log.error("memcached 写入日志信息失败", e);  
        }  
    }  

    /** 
     * 关闭流 
     */  
    private static void closeLogStream() {  
        try {  
            fileWriter.close();  
            logWrite.close();  
        } catch (IOException e) {  
        	log.error("memcached 日志对象关闭失败", e);  
        }  
    }  
}
