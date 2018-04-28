package finley.gmair.scheduler.wechat;

import finley.gmair.model.wechat.AccessToken;
import finley.gmair.service.AccessTokenService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatProperties;
import finley.gmair.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatScheduler {
    @Autowired
    private AccessTokenService accessTokenService;
    /**
     * This method will renew the access token once an hour
     * and will be saved to database
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void renewal() {
        String token = WechatUtil.queryAccessToken(WechatProperties.getValue("wechat_appid"), WechatProperties.getValue("wechat_secret"));
        WechatProperties.setAccessToken(token);
        new Thread(() -> {
            ResultData result = new ResultData();
            Map<String, Object> condition = new HashMap<>();
            condition.put("accessToken", token);
            if (!accessTokenService.existToken(condition)) {
                result = accessTokenService.create(new AccessToken(token));
            } else {
                result = accessTokenService.modify(new AccessToken(token));
            }
        }).start();
        //start a thread and save the valid token to database
    }
}
