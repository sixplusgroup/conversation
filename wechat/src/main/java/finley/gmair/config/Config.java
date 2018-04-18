package finley.gmair.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import finley.gmair.util.WechatUtil;

import java.io.InputStream;
import java.util.Properties;

@Component
public class Config {
	private Logger logger = LoggerFactory.getLogger(Config.class);
	
	private static String accessToken;

	private static String jsapiTicket;

	private static Properties props = new Properties();

	static {
		InputStream input = Config.class.getClassLoader().getResourceAsStream("wechat.properties");
		try {
			props.load(input);
		} catch (Exception e) {

		}
	}

	private Config() {
		super();
	}

	public static String getAccessToken() {
		if (StringUtils.isEmpty(accessToken)) {
			accessToken = WechatUtil.queryAccessToken(Config.getValue("wechat_appid"),
					Config.getValue("wechat_secret"));
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
		Config.jsapiTicket = jsapiTicket;
	}

	public static void setAccessToken(String token) {
		accessToken = token;
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public void schedule() {
		accessToken = WechatUtil.queryAccessToken(Config.getValue("wechat_appid"), Config.getValue("wechat_secret"));
	}
}
