package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("wechat-agent")
public interface WechatService {

    @PostMapping("/wechat/user/openid")
    ResultData openid(@RequestParam("code") String code);

    //get access_token
    @GetMapping("/wechat/accessToken/query")
    ResultData getToken();

    //upload temporary picture to get mediaId
    @PostMapping("/wechat/picture/upload/get/mediaId")
    String upload2mediaId(@RequestParam("accessToken") String accessToken,
                          @RequestParam("imgUrl") String imgUrl);

    //reply picture to user
    @PostMapping("/wechat/picture/reply")
    String picture2user(@RequestParam("openId") String openId,
                        @RequestParam("mediaId") String mediaId);
}
