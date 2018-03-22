package finley.gmair.scheduler;

import finley.gmair.util.WechatProperties;
import finley.gmair.util.WechatUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WechatScheduler {
    /**
     * This method will renew the access token once an hour
     * and will be saved to database
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void renewal() {
        String token = WechatUtil.queryAccessToken(WechatProperties.getValue("wechat_appid"), WechatProperties.getValue("wechat_secret"));
        WechatProperties.setAccessToken(token);
        //start a thread and save the valid token to database
    }
}
