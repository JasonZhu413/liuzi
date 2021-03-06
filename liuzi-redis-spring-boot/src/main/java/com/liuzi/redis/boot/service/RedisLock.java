package com.liuzi.redis.boot.service;

public interface RedisLock extends AutoCloseable {
	/**
     * 释放分布式锁
     */
    void unlock();
    
    /**
     * 释放分布式锁
     */
    boolean release();
}