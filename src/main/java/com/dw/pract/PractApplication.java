package com.dw.pract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PractApplication
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PractApplication.class);

    public static void main(String[] args)
    {
        LOGGER.debug("Welcome to application start");
        SpringApplication.run(PractApplication.class, args);
    }
}