package finley.gmair.config;

import finley.gmair.bean.enums.PayClientType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author Joby
 */
@Component
@AllArgsConstructor
public class SelectPayConfig {

    private final PayConfigLoader payConfigLoader;

    public PayClientType selectPayConfig(String payClient,PayConfig payConfig){
        if(payClient.equals(PayClientType.OFFICIALACCOUNT.name())){
            payConfig.setAppId(payConfigLoader.getOa().getAppId());
            payConfig.setMerchantId(payConfigLoader.getOa().getMerchantId());
            payConfig.setKey(payConfigLoader.getOa().getKey());
            payConfig.setNotifyUrl(payConfigLoader.getOa().getNotifyUrl());
            return PayClientType.OFFICIALACCOUNT;
        }else{
            payConfig.setAppId(payConfigLoader.getMp().getAppId());
            payConfig.setMerchantId(payConfigLoader.getMp().getMerchantId());
            payConfig.setKey(payConfigLoader.getMp().getKey());
            payConfig.setNotifyUrl(payConfigLoader.getMp().getNotifyUrl());
            return PayClientType.SHOPMP;
        }
    }
}
