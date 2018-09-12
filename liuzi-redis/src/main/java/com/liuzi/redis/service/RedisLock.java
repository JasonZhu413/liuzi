package com.liuzi.redis.service;

public interface RedisLock extends AutoCloseable {
    /**
     * 释放分布式锁
     */
    void unlock();
}