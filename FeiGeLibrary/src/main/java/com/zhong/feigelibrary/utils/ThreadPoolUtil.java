package com.zhong.feigelibrary.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    private int corePoolNumber = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService;
    private static ThreadPoolUtil threadPoolUtil;

    public ThreadPoolExecutor initThreadPool() {
        BlockingQueue blockingQueue = new LinkedBlockingDeque<Runnable>(512);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(corePoolNumber, corePoolNumber * 2, 1L, TimeUnit.SECONDS, blockingQueue);
        return executorService;
    }

    public static ExecutorService getThreadPool() {
        if (threadPoolUtil == null && executorService == null) {
            threadPoolUtil = new ThreadPoolUtil();
            executorService = threadPoolUtil.initThreadPool();
        }
        return executorService;
    }
}
