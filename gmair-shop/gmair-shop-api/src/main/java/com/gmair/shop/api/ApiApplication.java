

package com.gmair.shop.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.gmair.shop"})
@EnableTransactionManagement
@EnableDiscoveryClient
// extends SpringBootServletInitializer 使用外置的容器启动
// 该部分注释亦可
public class ApiApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApiApplication.class);
	}
}
