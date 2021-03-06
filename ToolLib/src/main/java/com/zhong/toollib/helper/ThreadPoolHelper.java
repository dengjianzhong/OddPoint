package com.zhong.toollib.helper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 【线程池的优势】
 * （1）、降低系统资源消耗，通过重用已存在的线程，降低线程创建和销毁造成的消耗；
 * （2）、提高系统响应速度，当有任务到达时，通过复用已存在的线程，无需等待新线程的创建便能立即执行；
 * （3）方便线程并发数的管控。因为线程若是无限制的创建，可能会导致内存占用过多而产生OOM，并且会造成cpu过度切换（cpu切换线程是有时间成本的（需要保持当前执行线程的现场，并恢复要执行线程的现场））。
 * （4）提供更强大的功能，延时定时线程池。
 *
 *         Executors.newSingleThreadExecutor();// corePool=1,maxPool=1;
 *         Executors.newCachedThreadPool();// corePool=0,maxPool=Integer.MAX_VALUE;
 *         Executors.newScheduledThreadPool(1);// corePool=开发者固定,maxPool=Integer.MAX_VALUE;
 *         Executors.newFixedThreadPool(1);// corePool=开发者固定,maxPool=开发者固定;
 */
public class ThreadPoolHelper {
    private int corePoolNumber;
    private static ExecutorService executorService;
    private static ThreadPoolHelper threadPoolHelper;

    private ThreadPoolHelper() {
        corePoolNumber = Runtime.getRuntime().availableProcessors();
        executorService = threadPoolHelper.initThreadPool();
    }

    private ThreadPoolExecutor initThreadPool() {
        BlockingQueue blockingQueue = new LinkedBlockingDeque<Runnable>();
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(corePoolNumber, corePoolNumber * 2,
                1L, TimeUnit.SECONDS, blockingQueue);

        return executorService;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ThreadPoolHelper getInstance() {
        if (threadPoolHelper == null) {
            synchronized (ThreadPoolHelper.class) {
                if (threadPoolHelper == null) {
                    threadPoolHelper = new ThreadPoolHelper();
                }
            }
        }
        return threadPoolHelper;
    }

    /**
     * 执行异步任务
     *
     * @param runnable the runnable
     */
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }


    /**
     * 关闭线程池
     */
    public void closeThreadPool() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
