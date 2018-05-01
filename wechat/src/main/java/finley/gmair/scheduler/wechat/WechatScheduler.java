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
import org.springframework.util.StringUtils;

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
    @Scheduled(cron = "0 0 * * * ?")
    public void renewal() {
        String token = WechatUtil.queryAccessToken(WechatProperties.getValue("wechat_appid"), WechatProperties.getValue("wechat_secret"));
        WechatProperties.setAccessToken(token);
        //start a thread and save the valid token to database
        if (!StringUtils.isEmpty(token)) {
            new Thread(() -> {
                AccessToken at = new AccessToken(token);
                ResultData result = accessTokenService.renew(at);
                if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    //manage to refresh the access token
                } else {
                    //something wrong when trying to save the data to database
                }
            }).start();
        } else {
            //something wrong with the query access token in wechat util
        }
    }
}
