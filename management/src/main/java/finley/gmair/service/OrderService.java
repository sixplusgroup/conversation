package finley.gmair.service;

import feign.Headers;
import feign.Param;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("order-agent")
public interface OrderService {
    @PostMapping("/order/upload")
    @Headers("Content-Type: multipart/form-data")
    ResultData upload(@Param("order_list") MultipartFile order);
}
