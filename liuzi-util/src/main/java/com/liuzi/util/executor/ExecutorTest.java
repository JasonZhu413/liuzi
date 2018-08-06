package com.liuzi.util.executor;

import java.util.concurrent.Callable;

import com.liuzi.util.executor.ExecutorPool;





/**
 * 测试类
 */
public class ExecutorTest {

    public static void main(String[] args) {
        ExecutorPool pool = ExecutorPool.getInstance();
        for (int i = 0; i < 200; i++) {
            pool.execute(new ExcuteTask2(i + ""));
        }
        pool.shutdown();
    }

    /**
     * 执行任务1，实现Callable方式
     */
    static class ExcuteTask1 implements Callable<String> {
        private String taskName;

        public ExcuteTask1(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public String call() throws Exception {
            try {
            	
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("-------------这里执行业务逻辑，Callable TaskName = " + taskName + "-------------");
            return ">>>>>>>>>>>>>线程返回值，Callable TaskName = " + taskName + "<<<<<<<<<<<<<<";
        }
    }

    /**
     * 执行任务2，实现Runable方式
     */
    static class ExcuteTask2 implements Runnable {
        private String taskName;

        public ExcuteTask2(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            try {
            	
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
