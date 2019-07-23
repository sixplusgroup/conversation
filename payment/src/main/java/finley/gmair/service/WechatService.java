package finley.gmair.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @ClassName: WechatService
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/23 4:28 PM
 */
@Service
@PropertySource({"classpath:wechatpay.properties"})
public class WechatService {
    @Value("${app_id}")
    private String appId;

    @Value("${merchant_id}")
    private String merchantId;

}
