package com.liuzi.util.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liuzi.util.executor.ExecutorCallBack;
import com.liuzi.util.executor.ExecutorPool;
import com.liuzi.util.executor.ExecutorServiceFactory;

/**
 * 线程处理类
 */
public class ExecutorPool {

	private static Logger logger = LoggerFactory.getLogger(ExecutorPool.class);
	
    private ExecutorService executor;
    private static ExecutorPool pool;
    private final int threadMax = 20;

    private ExecutorPool() {
        System.out.println("threadMax>>>>>>>" + threadMax);
        executor = ExecutorServiceFactory.getInstance().createFixedThreadPool(threadMax);
    }
    
    private ExecutorPool(int len) {
        System.out.println("threadMax>>>>>>>" + len);
        executor = ExecutorServiceFactory.getInstance().createFixedThreadPool(len);
    }

    public static ExecutorPool getInstance() {
    	if(pool == null){
    		pool = new ExecutorPool();
    	}
        return pool;
    }
    
    public static ExecutorPool getInstance(int len) {
    	if(pool == null){
    		pool = new ExecutorPool(len);
    	}
        return pool;
    }

    /**
     * 关闭线程池，这里要说明的是：调用关闭线程池方法后，线程池会执行完队列中的所有任务才退出
     */
    public void shutdown(){
        executor.shutdown();
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     */
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     */
    public Future<?> submit(Callable<?> task) {
        return executor.submit(task);
    }

    /**
     * 直接提交任务到线程池，无返回值
     */
    public void execute(Runnable task){
        executor.execute(task);
    }
    
    
	public static void execute(ExecutorCallBack callBack) {
		ExecutorPool pool = ExecutorPool.getInstance();
		pool.submit(new Runnable() {
            @Override
            public void run() {
            	try {
            		logger.info("thread pool excute call...");
					callBack.call(pool);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ExecutorPool execute error");
				}
				//pool.shutdown();
            }
        });
	}
}
