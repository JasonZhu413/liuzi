package com.liuzi.util.queue;

import java.util.concurrent.LinkedBlockingQueue;

import com.liuzi.util.executor.ExecutorCallBack;
import com.liuzi.util.executor.ExecutorPool;


/**
 * 消息队列缓冲定义
 */
public class PushBlockQueue extends LinkedBlockingQueue<Object>{
    
    private static final long serialVersionUID = -8224792866430647454L;
    //private static ExecutorService es = Executors.newFixedThreadPool(10);//线程池
    private static PushBlockQueue pbq = new PushBlockQueue();//单例
    private boolean flag = false;
    
    private PushBlockQueue(){}
    
    public static PushBlockQueue getInstance(){
        return pbq;
    }
    
    /**
     * 队列监听启动
     */
    public void start(){
        if(!this.flag){
            this.flag = true;
        }else{
            throw new IllegalArgumentException("队列已处于启动状态,不允许重复启动.");
        }
        
        ExecutorPool.execute(new ExecutorCallBack() {
			@Override
			public void call(ExecutorPool pool) {
				try {
                    Object obj = take();
                    new PushBlockQueueHandler(obj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
			}
		});;
        
        /*new Thread(new Runnable(){
            @Override
            public void run() {
                while(flag){
                	System.out.println("队列持续监听......");
                    try {
                        Object obj = take();//使用阻塞模式获取队列消息
                        es.execute(new PushBlockQueueHandler(obj));//线程池处理
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }
    
    /**
     * 停止队列监听
     */
    public void stop(){
        this.flag = false;
    }
	
}

