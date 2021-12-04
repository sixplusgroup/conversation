package finley.gmair.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Joby
 */
@Component
public class PayConfigStrategyFactory {

    @Autowired
    private PayConfigLoader payConfigLoader;

    private Map<String,PayConfig> PAYCONFIG_STRATEGY_MAP = new HashMap<>();
    @PostConstruct
    private void initMap(){
        PAYCONFIG_STRATEGY_MAP.put(PayConfigKey.DRIFTMP,payConfigLoader.getOa());
        PAYCONFIG_STRATEGY_MAP.put(PayConfigKey.SHOPMP,payConfigLoader.getMp());
    }

    public PayConfig getPayConfig(String payConfigKey){
        PayConfig payConfig = PAYCONFIG_STRATEGY_MAP.get(payConfigKey);
        return payConfig == null ? PAYCONFIG_STRATEGY_MAP.get(PayConfigKey.DRIFTMP) : payConfig;
        //默认是公众号配置, 防止发生空指针
    }

    private interface PayConfigKey{
        String DRIFTMP = "DRIFTMP";
        String SHOPMP = "SHOPMP";
    }
}
