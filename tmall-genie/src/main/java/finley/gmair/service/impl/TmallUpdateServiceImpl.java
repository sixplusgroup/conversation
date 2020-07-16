package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.TmallUpdateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TaobaoUtil;
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

    private static final String METHOD_NAME = "alibaba.ailabs.iot.device.list.update.notify";
    private static final String SERVER_URL = "http://gw.api.taobao.com/router/rest";
    private static final String APP_KEY = "30478551";
    private static final String APP_SECRET = "be8812c7d6e6fbb2202e1fc0fd182a6b";
    private static final String SKILL_ID = "54170";

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
}
