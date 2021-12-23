package com.xgf.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-12-23 10:25
 * @description ThreadPoolTaskExecutor 线程池属性配置
 *  ThreadPoolTaskExecutor ： Spring框架提供的线程池技术【底层是基于JDK的ThreadPoolExecutor实现】
 **/

@Data
@Component
@ConfigurationProperties(prefix = "executor")
public class AsyncTaskConfigProperties {

    /**
     * ThreadPoolTaskExecutor 线程池属性配置
     */
    public static class ThreadPoolTaskExecutorProperties {
        /**
         * 线程池维护线程的最少数量【无任务也会一直存在】
         * 配置 allowCoreThreadTimeout = true（默认false）时，核心线程会超时关闭
         */
        public static final int CORE_POOL_SIZE = 32;

        /**
         * 线程池维护线程的最大数量
         */
        public static final int MAX_POOL_SIZE = 65;

        /**
         * 缓存队列（阻塞队列）当核心线程数达到最大时，新任务会放在队列中排队等待执行
         */
        public static final int QUEUE_CAPACITY = 100;

        /**
         * 允许线程空闲时间
         * 【当前线程数 > corePoolSize，会知道减少到corePoolSize数，allowCoreThreadTimeout = true，最少可以减少到0】
         */
        public static final int KEEP_ALIVE_SECONDS = 60;

        /**
         * 线程名称前缀
         */
        public static final String THREAD_NAME_PREFIX = "taskUtil_";
    }



}
