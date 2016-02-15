package com.dw.pract.utils;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;

@EnableScheduling
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer
{

    private static final int MAX_QUEUE_CAPACITY = 1000;

    private static final int MAX_POOL_SIZE = 10000;

    private static final int CORE_POOL_SIZE = 25;

    private static final int MY_EXECUTOR_POOL_SIZE = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

    public AsyncConfig()
    {
        LOGGER.debug("component is initialized");
    }

    @Override
    public Executor getAsyncExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(MAX_QUEUE_CAPACITY);
        executor.setThreadNamePrefix("PractExecutor-");
        executor.initialize();
        DelegatingSecurityContextExecutor securityContextExecutor = new DelegatingSecurityContextExecutor(executor);
        return new DelegatingSecurityContextExecutor(securityContextExecutor);
    }

    @Bean(name = "MyExecutor")
    public Executor getMyExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(MY_EXECUTOR_POOL_SIZE);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(4);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize();
        DelegatingSecurityContextExecutor securityContextExecutor = new DelegatingSecurityContextExecutor(executor);
        return new DelegatingSecurityContextExecutor(securityContextExecutor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
    {
        return new MyAsyncUncaughtExceptionHandler();
    }

}
