package finley.gmair.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WechatUtil {

    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    public static String queryJsApiTicket(String token) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                + token + "&type=jsapi";
        String result = "";
        try {
            URL address = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) address
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            InputStream is = connection.getInputStream();
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String message = new String(bytes, "UTF-8");
            JSONObject object = JSON.parseObject(message);
            result = object.getString("ticket");
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return result;
    }

    public static final String inputStream2String(InputStream in)
            throws IOException {
        if (in == null) {
            return "";
        } else {
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];

            int n;
            while ((n = in.read(b)) != -1) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            return out.toString();
        }
    }
}
