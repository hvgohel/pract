package com.dw.pract.utils;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class MyAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MyAsyncUncaughtExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params)
    {
        if (logger.isErrorEnabled())
        {
            logger.error("Unexpected error occured invoking async method : {} with arguments : {}", method, params, ex);
        }
    }

}
