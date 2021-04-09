package finley.gmair.service;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 22:38
 * @description: OrderNewService
 */

@FeignClient(name = "order-agent", configuration = OrderNewService.MultipartSupportConfig.class)
public interface OrderNewService {

    @PostMapping(value = "/order/uploadAndSync", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultData uploadAndSync(@RequestParam("file") MultipartFile file,
                             @RequestParam("password") String password);

    @Configuration
    class MultipartSupportConfig {
        @Bean
        @Primary
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
