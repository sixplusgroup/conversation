package finley.gmair.service;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(value = "order-agent", configuration = OrderService.MultipartSupportConfig.class)
public interface OrderService {
    @PostMapping(value = "/order/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultData upload(MultipartFile file);

    @Configuration
    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }

}
