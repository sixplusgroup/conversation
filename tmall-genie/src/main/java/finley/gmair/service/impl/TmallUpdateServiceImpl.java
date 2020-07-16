package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import feign.Response;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.service.TmallUpdateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TaobaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class TmallUpdateServiceImpl implements TmallUpdateService {

    @Autowired
    AuthConsumerService authConsumerService;

    private static final String METHOD_NAME = "alibaba.ailabs.iot.device.list.update.notify";
    private static final String SERVER_URL = "http://gw.api.taobao.com/router/rest";
    private static final String APP_KEY = "30478551";
    private static final String APP_SECRET = "be8812c7d6e6fbb2202e1fc0fd182a6b";
    private static final String SKILL_ID = "54170";

    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE = "code";
    private static final String REDIRECT_URI = "https://open.bot.tmall.com/oauth/callback";
    private static final String CLIENT_ID = "client_4";
    private static final String CLIENT_SECRET = "123456";

    /**
     * 设备列表更新通知
     *
     * @param accessToken
     * @return
     */
    @Override
    public ResultData updateListNotify(String accessToken) throws IOException {
        Map<String, String> params = new HashMap<>();
        // 参数
        params.put("method", METHOD_NAME);
        params.put("app_key", APP_KEY);
        params.put("sign_method", TaobaoUtil.SIGN_METHOD_HMAC);
        params.put("skill_id", SKILL_ID);
        params.put("token", accessToken);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("timestamp", df.format(new Date()));
        params.put("format", "json");
        params.put("v", "2.0");
        params.put("type", "1");
        // 签名
        params.put("sign", TaobaoUtil.signTopRequest(params, APP_SECRET, TaobaoUtil.SIGN_METHOD_HMAC));
        // 请求API
        JSONObject jsonObject = JSONObject.parseObject(TaobaoUtil.callApi(new URL(SERVER_URL), params));
        ResultData resultData = new ResultData();
        if (jsonObject.getString("error_response") != null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setData(jsonObject.get("error_response"));
        } else {
            resultData.setResponseCode(ResponseCode.RESPONSE_OK);
            resultData.setData(true);
        }
        return resultData;
    }

    /**
     * 密码模式token换取授权码token
     *
     * @param accessToken 密码模式token
     * @return 授权码token
     */
    @Override
    public String getAuthorizationToken(String accessToken) {
        //请求授权端点
        Response response = authConsumerService.authorize(RESPONSE_TYPE, CLIENT_ID, REDIRECT_URI, accessToken);
        //获取重定向location
        String location = Lists.newArrayList(response.headers().get("Location")).get(0);
        //根据location解析code
        String code = location.split("code=")[1].substring(0, 6);
        //根据code换取授权码token
        Map<String, String> map = authConsumerService.getToken(code, GRANT_TYPE, REDIRECT_URI, CLIENT_ID, CLIENT_SECRET);
        System.out.println(map);
        return map.get("access_token");
    }
}
