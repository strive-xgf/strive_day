package com.xgf.task.config;

import com.xgf.task.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xgf
 * @create 2021-12-23 14:21
 * @description 线程池配置
 **/

@Slf4j
@EnableAsync
@Configuration
public class AsyncTaskAutoConfiguration {

    /**
     * 自定义线程池 ： TaskUtil使用
     * @return
     */
    @Bean("taskThreadPool")
    public AsyncTaskExecutor taskThreadPool() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Math.max(AsyncTaskConfigProperties.ThreadPoolTaskExecutorProperties.CORE_POOL_SIZE, Runtime.getRuntime().availableProcessors() - 1));
        executor.setMaxPoolSize(Math.max(AsyncTaskConfigProperties.ThreadPoolTaskExecutorProperties.MAX_POOL_SIZE, executor.getCorePoolSize() * 2));
        executor.setQueueCapacity(AsyncTaskConfigProperties.ThreadPoolTaskExecutorProperties.QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(AsyncTaskConfigProperties.ThreadPoolTaskExecutorProperties.KEEP_ALIVE_SECONDS);
        executor.setThreadNamePrefix(AsyncTaskConfigProperties.ThreadPoolTaskExecutorProperties.THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        log.info("====== init taskThreadPool -> corePoolSize:[{}], maxPoolSize:[{}], namePrefix:[{}]",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getThreadNamePrefix());
        executor.initialize();

        executor.setTaskDecorator(TaskUtil::process);

        return executor;
    }

}
