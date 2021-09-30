

package com.gmair.shop.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.gmair.shop"})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gmair.shop.service")
public class ApiApplication extends SpringBootServletInitializer{ // extends SpringBootServletInitializer 使用外置的容器启动  该部分注释亦可
	public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApiApplication.class);
	}
}
