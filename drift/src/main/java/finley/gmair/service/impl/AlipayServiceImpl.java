package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.ZhimaCreditScoreGetRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.ZhimaCreditScoreGetResponse;
import finley.gmair.service.AlipayService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AlipayServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/2 6:53 PM
 */
@Service
@PropertySource("classpath:sesame.properties")
public class AlipayServiceImpl implements AlipayService {
    private Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

    private final static String BASE = "https://openapi.alipay.com/gateway.do";

    private AlipayClient client;

    @Value("${zhima_appid}")
    private String appId;

    @Value("${private_key}")
    private String privateKey;

    private final static String format = "json";

    private final static String charset = "UTF-8";

    @Value("${public_key}")
    private String publicKey;

    public AlipayServiceImpl() {
        this.client = new DefaultAlipayClient(BASE, appId, privateKey, format, charset, publicKey);
    }

    public ResultData code2openid(String code) {
        ResultData result = new ResultData();
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(code);
        request.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = client.execute(request);
            result.setData(oauthTokenResponse);
            logger.info(JSON.toJSONString(oauthTokenResponse));
        } catch (AlipayApiException e) {
            logger.error("[Error] - ".concat(e.getErrMsg()));
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getErrMsg());
        }
        return result;
    }

    public ResultData request(String token) {
        ResultData result = new ResultData();
        ZhimaCreditScoreGetRequest request = new ZhimaCreditScoreGetRequest();
        request.setBizContent("{" +
                "\"transaction_id\":\"201512100936588040000000465158\"," +
                "\"product_code\":\"w1010100100000000001\"" +
                "  }");
        try {
            ZhimaCreditScoreGetResponse response = client.execute(request, token);
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
