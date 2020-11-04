package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 22:38
 * @description: OrderNewService
 */

@FeignClient("order-new-agent")
public interface OrderNewService {

    @PostMapping("/order-new/uploadAndSync")
    ResultData uploadAndSync(@RequestParam("file") MultipartFile file,
                             @RequestParam("password") String password);
}
