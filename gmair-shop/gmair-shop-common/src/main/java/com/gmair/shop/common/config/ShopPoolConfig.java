package com.gmair.shop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author Joby
 */
@Configuration
public class ShopPoolConfig {

    @Bean
    public ThreadPoolExecutor shopPool(){
        return new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

}
