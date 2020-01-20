//package finley.gmair.scheduler.wechat;
//
//import finley.gmair.model.wechat.AccessToken;
//import finley.gmair.service.AccessTokenService;
//import finley.gmair.util.ResponseCode;
//import finley.gmair.util.ResultData;
//import finley.gmair.util.WechatProperties;
//import finley.gmair.util.WechatUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@PropertySource("classpath:wechat.properties")
//public class WechatScheduler {
//    private Logger logger = LoggerFactory.getLogger(WechatScheduler.class);
//
//    @Autowired
//    private AccessTokenService accessTokenService;
//
//    @Value("${wechat_appid}")
//    private String wechatAppId;
//
//    @Value("${wechat_secret}")
//    private String wechatSecret;
//
//    @Value("${mini_appid}")
//    private String miniAppId;
//
//    @Value("${mini_secret}")
//    private String miniSecret;
//
//    /**
//     * This method will renew the access token once an hour
//     * and will be saved to database
//     */
//    @Scheduled(cron = "0 0 * * * ?")
//    public void renewal() {
//        String at = token(wechatAppId, wechatSecret);
//        ticket(at);
//        token(miniAppId, miniSecret);
//    }
//
//    private String token(String appid, String secret) {
//        logger.info("获取appid: " + appid + "的access token");
//        String token = WechatUtil.queryAccessToken(appid, secret);
//        logger.info("获取成功, token为: " + token);
//        //start a thread and save the valid token to database
//        if (!StringUtils.isEmpty(token)) {
//            AccessToken at = new AccessToken(appid, token);
//            ResultData result = accessTokenService.renew(at);
//            if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
//                //manage to refresh the access token
//
//            } else {
//                //something wrong when trying to save the data to database
//            }
//        } else {
//            //something wrong with the query access token in wechat util
//        }
//        return token;
//    }
//
//    private void ticket(String accessToken) {
//        WechatProperties.setAccessToken(accessToken);
//        WechatProperties.setJsapiTicket(WechatUtil.queryJsApiTicket(accessToken));
//    }
//}
