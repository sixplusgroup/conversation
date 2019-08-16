package finley.gmair.service.impl;

import finley.gmair.service.VerificationService;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.faceid.v20180301.FaceidClient;

import com.tencentcloudapi.faceid.v20180301.models.IdCardVerificationRequest;
import com.tencentcloudapi.faceid.v20180301.models.IdCardVerificationResponse;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 16:01 2019/8/16
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    private Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Override
    public ResultData verify(String idCard, String name) {
        ResultData result = new ResultData();

        try{

            //todo
            Credential cred = new Credential("", "");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("faceid.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            //region 暂时设置为  华东地区（上海）
            FaceidClient client = new FaceidClient(cred, "ap-shanghai", clientProfile);

            String cardStr = "\"IdCard\":\"" + idCard + "\"";
            String nameStr = "\"Name\":\"" + name + "\"";
            String params = "{" + cardStr + ","  + nameStr + "}";
            IdCardVerificationRequest req = IdCardVerificationRequest.fromJsonString(params, IdCardVerificationRequest.class);

            IdCardVerificationResponse resp = client.IdCardVerification(req);

            logger.info(IdCardVerificationRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

        return result;
    }
}
