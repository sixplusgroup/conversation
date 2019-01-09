package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("consumer-auth-agent")
public interface AuthConsumerService {

    @GetMapping("/auth/consumer/profile")
    ResultData profile(@RequestParam("consumerId") String consumerId);

    @PostMapping("/auth/consumer/wechat/bind")
    ResultData bindWechat(@RequestParam("consumerId") String consumerId,
                          @RequestParam("openid") String openid);

    @PostMapping("/auth/consumer/wechat/unbind")
    ResultData unbindWechat(@RequestParam("consumerId") String consumerId);

    @PostMapping("/auth/consumer/edit/username")
    ResultData editUsername(@RequestParam("consumerId") String consumerId,
                            @RequestParam("username") String username);

    @PostMapping("/auth/consumer/edit/phone")
    ResultData editPhone(@RequestParam("consumerId") String consumerId,
                         @RequestParam("newPhone") String newPhone);

    @PostMapping("/auth/consumer/edit/location")
    ResultData editLocation(@RequestParam("consumerId") String consumerId,
                            @RequestParam("province") String province,
                            @RequestParam("city") String city,
                            @RequestParam("district") String district,
                            @RequestParam("detail") String detail,
                            @RequestParam("preferred") boolean preferred);

    @GetMapping(value = "/auth/consumer/check/existphone")
    ResultData checkPhoneExist(@RequestParam("phone") String phone);

}
