package com.xgf.task;


import com.xgf.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
    public static void waitAll(CompletableFuture<?> ...cfs) {
        CompletableFuture.allOf(cfs).join();
    }

    /**
     * 等待异步任务完成，(所有子任务执行完成后，按顺序将子任务内部异常捕获并继续抛出到主线程上去）
     *
     * @param cfs 异步任务列表
     */
    public static void waitAllThrow(CompletableFuture<?>... cfs) {
        try {
            waitAll(cfs);
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException) {
                // 只抛内部异常
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }

    /**
     * 获取首个抛的异常（按任务顺序）
     */
    public static void waitFirstException(CompletableFuture<?>... cfs) {
        waitFirstException(Arrays.asList(cfs));
    }

    /**
     * 获取首个抛的异常（按任务顺序）
     */
    public static void waitFirstException(List<CompletableFuture<?>> futureCollection) {
        if (CollectionUtils.isEmpty(futureCollection)) {
            return;
        }

        boolean endFlag = false;
        RuntimeException exception = null;

        for (CompletableFuture<?> future : futureCollection) {
            try {
                if (!endFlag) {
                    future.get();
                } else {
                    future.cancel(true);
                }
            } catch (InterruptedException e) {
                log.error("====== TaskUtil waitFirstException InterruptedException message = 【{}】", e.getMessage(), e);
            } catch (ExecutionException e) {
                endFlag = true;
                if (e.getCause() instanceof RuntimeException) {
                    log.debug("====== TaskUtil waitFirstException exist exception message = 【{}】", e.getMessage());
                    exception = (RuntimeException) e.getCause();
                }
            }
        }

        if (Objects.nonNull(exception)) {
            throw exception;
        }
    }


    /**
     * 获取首个抛的异常（按时间顺序）
     */
    public static void waitAnyException(CompletableFuture<?>... cfs) {
        waitAnyException(Arrays.asList(cfs));
    }

    /**
     * 获取首个抛的异常（按时间顺序）
     */
    public static void waitAnyException(List<CompletableFuture<?>> futureCollection) {
        if (CollectionUtils.isEmpty(futureCollection)) {
            return;
        }
        // 任务数
        final int taskCount = futureCollection.size();
        // 用于累计执行完的任务烽
        final AtomicInteger count = new AtomicInteger();
        // 用于存放任务执行的异常
        final AtomicReference<RuntimeException> result = new AtomicReference<>();

        // 加同步锁是为了防止其它往里面塞异常
        synchronized (result) {
            // future.whenComplete 异步
            futureCollection.forEach(future -> future.whenComplete((value, e) -> {
                // 计数 如果任务抛出异常，或者全部执行完毕，通知主线程
                if (e instanceof CompletionException || count.incrementAndGet() >= taskCount) {
                    log.debug("====== TaskUtil waitAnyException notifyAll");
                    synchronized (result) {
                        if (e != null) {
                            result.compareAndSet(null, (RuntimeException) e.getCause());
                        }
                        result.notifyAll();
                    }
                }
            }));

            log.debug("====== TaskUtil waitAnyException result.wait() start");
            try {
                // 等待任务线程通知(会不会导致永远不被唤醒)
                result.wait(100000L);
            } catch (InterruptedException e) {
                log.error("====== TaskUtil waitAnyException InterruptedException message 【{}】 ", e.getMessage(), e);
            }
            log.debug("====== TaskUtil waitAnyException result.wait() end");
            futureCollection.forEach(f -> f.cancel(true));
        }

        // 按执行时间先后顺序抛出异常
        Optional.ofNullable(result.get()).ifPresent(e -> {
            throw e;
        });
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
