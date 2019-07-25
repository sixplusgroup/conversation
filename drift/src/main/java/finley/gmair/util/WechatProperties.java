package finley.gmair.util;

import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Properties;

public class WechatProperties {
    private static String accessToken;

    private static String jsapiTicket;

    private static Properties props = new Properties();

    static {
        InputStream input = WechatProperties.class.getClassLoader().getResourceAsStream("wechat.properties");
        try {
            props.load(input);
        } catch (Exception e) {

        }
    }

    private WechatProperties() {
        super();
    }

    public static String getAccessToken() {
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = WechatUtil.queryAccessToken(WechatProperties.getValue("wechat_appid"),
                    WechatProperties.getValue("wechat_secret"));
        }
        return accessToken;
    }

    public static String getJsapiTicket() {
        if (StringUtils.isEmpty(jsapiTicket)) {
            getAccessToken();
        }
        return jsapiTicket;
    }

    public static void setJsapiTicket(String jsapiTicket) {
        WechatProperties.jsapiTicket = jsapiTicket;
    }

    /**
     * This method can only be called by Wechat Scheduler
     *
     * @param token
     */
    public static void setAccessToken(String token) {
        accessToken = token;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public void schedule() {
        accessToken = WechatUtil.queryAccessToken(WechatProperties.getValue("wechat_appid"), WechatProperties.getValue("wechat_secret"));
    }
}
