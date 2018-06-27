package finley.gmair.service;

import finley.gmair.form.consumer.LocationForm;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("consumer-auth-agent")
public interface AuthConsumerService {

    @GetMapping("/auth/consumerid")
    ResultData getConsumerId(@RequestParam("phone") String phone);

    @GetMapping("/auth/consumer/profile")
    ResultData profile(@RequestParam("phone") String phone);

    @PostMapping("/auth/consumer/wechat/bind")
    ResultData bindWechat(@RequestParam("phone") String phone,
                          @RequestParam("openid") String openid);

    @PostMapping("/auth/consumer/wechat/unbind")
    ResultData unbindWechat(@RequestParam("phone") String phone);

    @PostMapping("/auth/consumer/edit/username")
    ResultData editUsername(@RequestParam("phone") String phone,
                            @RequestParam("username") String username);

    @PostMapping("/auth/consumer/edit/phone")
    ResultData editPhone(@RequestParam("oldPhone") String oldPhone,
                         @RequestParam("newPhone") String newPhone);

    @PostMapping("/auth/consumer/edit/location")
    ResultData editLocation(@RequestParam("phone") String phone,
                            @RequestParam("province") String province,
                            @RequestParam("city") String city,
                            @RequestParam("district") String district,
                            @RequestParam("detail") String detail,
                            @RequestParam("preferred") boolean preferred);


}
