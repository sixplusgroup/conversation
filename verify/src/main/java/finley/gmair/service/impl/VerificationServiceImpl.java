package finley.gmair.service.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.IdCardVerificationRequest;
import com.tencentcloudapi.faceid.v20180301.models.IdCardVerificationResponse;
import finley.gmair.service.VerificationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 16:01 2019/8/16
 */
@Service
@PropertySource({"classpath:verification.properties"})
public class VerificationServiceImpl implements VerificationService {

    private Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Value("${secret_id}")
    private String secretId;

    @Value("${secret_key}")
    private String secretKey;

    @Override
    public ResultData verify(String idCard, String name) {
        ResultData result = new ResultData();

        try {

            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("faceid.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            //region 暂时设置为  华东地区（上海）
            FaceidClient client = new FaceidClient(cred, "ap-shanghai", clientProfile);

            //todo 用JSONObject来构造参数，不要自己拼字符串
            String cardStr = "\"IdCard\":\"" + idCard + "\"";
            String nameStr = "\"Name\":\"" + name + "\"";
            String params = "{" + cardStr + "," + nameStr + "}";
            logger.info("params json : " + params);
            IdCardVerificationRequest req = IdCardVerificationRequest.fromJsonString(params, IdCardVerificationRequest.class);

            IdCardVerificationResponse resp = client.IdCardVerification(req);

            logger.info("response json : " + IdCardVerificationRequest.toJsonString(resp));

            if (resp.getResult().trim().equals("0")) {
                result.setData(Boolean.valueOf(true));
            } else {
                result.setData(Boolean.valueOf(false));
                //logger.warn("result : " + resp.getResult() + " , description : " + resp.getDescription());
            }
            result.setDescription(resp.getDescription());

        } catch (TencentCloudSDKException e) {
            logger.error(e.toString());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.toString());
        }

        return result;
    }
}
