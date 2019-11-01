package com.liuzi.util.queue;

/**
 * 队列消息处理实现
 */
public class PushBlockQueueHandler implements Runnable {

    private Object obj;
    public PushBlockQueueHandler(Object obj){
        this.obj = obj;
    }
    
    @Override
    public void run() {
        doBusiness();
    }
    
    public void doBusiness(){
        System.out.println("处理请求 " + obj);
    }

}
