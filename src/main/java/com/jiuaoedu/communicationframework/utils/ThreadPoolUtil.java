package com.jiuaoedu.communicationframework.utils;

import java.util.concurrent.*;

public class ThreadPoolUtil {
    public static ExecutorService createFixedThreadPool(int size, String threadNamePrefix) {
        return new ThreadPoolExecutor(
            size,
            size,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                private final ThreadGroup group = new ThreadGroup(threadNamePrefix);
                private int counter = 0;
                
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(group, r, threadNamePrefix + "-" + counter++);
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}