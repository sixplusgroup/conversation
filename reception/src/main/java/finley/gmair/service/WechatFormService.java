package finley.gmair.service;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "wechat-agent", configuration = WechatFormService.MultipartSupportConfig.class)

public interface WechatFormService {
    @PostMapping(value = "/wechat/picture/upload/and/reply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadAndReply(@RequestParam("openId") String openId, MultipartFile file);

    @Configuration
    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }


}
