package com.xgf.task;


import com.xgf.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author strive_day
 * @create 2021-12-22 18:26
 * @description 异步工具 【线程池资源有限，大量使用可能导致阻塞】
 */
@Slf4j
public class TaskUtil {

    /**
     * 同步执行无返回值的任务
     *
     * @param runnable 任务
     */
    public static void run(Runnable runnable) {
        runnable.run();
    }

    /**
     * 【同步执行】【有返回值】的任务
     *
     * @param supplier 任务
     * @param <T>      结果类型
     * @return 任务结果 T
     */
    public static <T> T supply(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * 【异步执行】【无返回值】的任务
     *
     * @param runnable 任务
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, getTaskThreadPool());
    }

    /**
     * 【异步执行】【有返回值】的任务
     *
     * @param supplier 任务
     * @param <T> 返回值类型
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, getTaskThreadPool());
    }

    /**
     * 等待所有异步任务完成
     *
     * @param cfs 异步任务列表
     */
    public static void waitAll(CompletableFuture<?> cfs) {
        CompletableFuture.allOf(cfs).join();
    }

    /**
     * 获取 TaskUtil 线程池
     *
     * @return 自定义线程池 taskThreadPool
     */
    private static AsyncTaskExecutor getTaskThreadPool() {
        return (AsyncTaskExecutor) SpringUtil.getBean("taskThreadPool");
    }

    /**
     * 组装任务执行（日志）
     *
     * @param runnable 任务
     * @return 包装结果
     */
    public static Runnable process(Runnable runnable) {
        Supplier<Void> wrapper = wrapper(() -> {
            runnable.run();
            return null;
        });
        return wrapper::get;
    }

    /**
     * 包装任务
     *
     * @param supplier 任务
     * @param <T>      返回值类型
     * @return 包装好的任务
     */
    private static <T> Supplier<T> wrapper(Supplier<T> supplier) {

        long threadId = Thread.currentThread().getId();
        return () -> {
//            long startTime = System.currentTimeMillis();
            long threadIdCurrent = Thread.currentThread().getId();
            log.debug("====== TaskUtil start：thread {} -> {}", threadId, threadIdCurrent);

            try {
                return supplier.get();
            } finally {
//                log.info("====== TaskUtil end, cost {} ms", System.currentTimeMillis() - startTime);
            }
        };
    }
}
