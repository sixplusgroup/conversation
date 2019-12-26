package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Properties;

public class WechatProperties {
    private static Logger logger = LoggerFactory.getLogger(WechatProperties.class);

    private static String accessToken;

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
            String responnse = HttpDeal.getResponse("http://www.pinpu.cn/service/api.asmx/GetAccessToken");
            if (StringUtils.isEmpty(responnse)) {
                logger.error("cannot obtain access token");
            }
            JSONObject json = new JSONObject();
            try {
                json = JSONObject.parseObject(responnse);
            } catch (Exception e) {
                logger.error("Unrecognized access token format: " + responnse);
            }
            accessToken = json.getString("access_token");
        }
        return accessToken;
    }

    public static String getJsapiTicket() {
        getAccessToken();
        String jsapiTicket = WechatUtil.queryJsApiTicket(accessToken);
        return jsapiTicket;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
