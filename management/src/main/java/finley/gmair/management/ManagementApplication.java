package finley.gmair.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@ComponentScan({"finley.gmair.config", "finley.gmair.sf.config", "finley.gmair.controller", "finley.gmair.service"})
@EnableFeignClients(basePackages = "finley.gmair.service")
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {//修改环境默认路径
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("d:/Test");
        return factory.createMultipartConfig();
    }
}



