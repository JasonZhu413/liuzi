package com.liuzi.util.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;



/**
 * 线程处理类
 */
@Slf4j
public class ThreadPoolExecutor {

    private ExecutorService executor;

    public ThreadPoolExecutor(int len, String msg) {
        log.info("  >>>>>>> 初始化线程池: " + msg + ", 最大连接数: " + len + " >>>>>>>");
        executor = ThreadServiceFactory.getInstance().createFixedThreadPool(len);
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
}
