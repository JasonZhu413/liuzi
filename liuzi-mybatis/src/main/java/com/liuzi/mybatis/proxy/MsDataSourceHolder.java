package com.liuzi.mybatis.proxy;

public class MsDataSourceHolder {
	/**
     * 主数据库标识
     */
    public static final String MASTER = "master";
    /**
     * 从数据库标识
     */
    public static final String SLAVE = "slave";
 
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();
 
    private MsDataSourceHolder() {
        //
    }
 
    public static void putDataSource(String key) {
        holder.set(key);
    }
 
    public static String getDataSource() {
        return holder.get();
    }
 
    public static void clearDataSource() {
        holder.remove();
    }
 
    public static boolean isMaster(){
        return holder.get().equals(MASTER);
    }
}
