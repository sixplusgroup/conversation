package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author CK
 */

@FeignClient("wechat-agent")
public interface WeChatService {

    @GetMapping(value = "/wechat/message/sendFilterCleanMessage")
    ResultData sendFilterCleanMessage(@RequestParam("json")String json, @RequestParam("type")int type);

    @GetMapping(value = "/wechat/message/sendFilterReplaceMessage")
    ResultData sendFilterReplaceMessage(@RequestParam("json")String json, @RequestParam("type")int type);
}
