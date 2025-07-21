package com.jiuaoedu.communicationframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtil {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolUtil.class);

    private ThreadPoolUtil() {
        throw new IllegalStateException("工具类不允许实例化");
    }

    /**
     * 创建固定大小的线程池（推荐使用）
     *
     * @param coreSize         核心线程数（固定大小）
     * @param threadNamePrefix 线程名称前缀（用于日志和排查问题）
     * @param queueCapacity    任务队列容量（有界队列，防止OOM）
     * @return 配置合理的线程池实例
     */
    public static ExecutorService createFixedThreadPool(
            int coreSize,
            String threadNamePrefix,
            int queueCapacity) {

        // 校验参数合法性
        if (coreSize <= 0) {
            throw new IllegalArgumentException("核心线程数必须为正数: " + coreSize);
        }
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException("队列容量必须为正数: " + queueCapacity);
        }

        // 自定义线程工厂（替代ThreadGroup，安全且可控）
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1); // 原子计数器，线程安全
            private final String namePrefix = threadNamePrefix + "-thread-";

            @Override
            public Thread newThread(Runnable r) {
                // 直接使用当前线程的ThreadGroup（避免手动创建），或默认系统线程组
                Thread thread = new Thread(
                        r,  // 任务
                        namePrefix + threadNumber.getAndIncrement()  // 线程名：前缀+自增序号
                );
                // 设置为守护线程（可选，根据业务需求）
                // thread.setDaemon(false);
                // 设置线程优先级（默认5即可，避免设置过高）
                if (thread.getPriority() != Thread.NORM_PRIORITY) {
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
        };

        // 构建线程池（核心参数优化）
        return new ThreadPoolExecutor(
                coreSize,                  // 核心线程数（固定大小）
                coreSize,                  // 最大线程数（与核心线程数一致，固定线程池）
                0L,                        // 空闲线程存活时间（固定线程池无需回收核心线程）
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity),  // 有界队列（防止任务堆积导致OOM）
                threadFactory,                            // 自定义线程工厂（无ThreadGroup）
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略（提交任务的线程自行执行，缓解压力）
        );
    }

    /**
     * 优雅关闭线程池（确保任务正确处理，资源释放）
     *
     * @param executor 线程池实例
     * @param timeout  最大等待时间
     * @param unit     时间单位
     */
    public static void shutdownGracefully(ExecutorService executor, long timeout, TimeUnit unit) {
        if (executor == null || executor.isTerminated()) {
            return;
        }
        // 1. 拒绝接收新任务
        executor.shutdown();
        try {
            // 2. 等待已提交的任务完成
            if (!executor.awaitTermination(timeout, unit)) {
                // 3. 超时后强制中断未完成的任务
                executor.shutdownNow();
                // 4. 等待强制中断完成
                if (!executor.awaitTermination(timeout, unit)) {
                    ThreadPoolUtil.log.warn("线程池未能完全关闭，可能存在未处理的任务");
                }
            }
        } catch (InterruptedException e) {
            // 恢复中断状态（重要，避免上层调用者丢失中断信号）
            Thread.currentThread().interrupt();
            // 强制中断剩余任务
            executor.shutdownNow();
        }
    }
}