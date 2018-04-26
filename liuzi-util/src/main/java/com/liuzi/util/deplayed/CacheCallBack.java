package com.liuzi.util.deplayed;

public interface CacheCallBack<K, V> {

    public void handler(K key, boolean isEnd) throws Exception;

}
