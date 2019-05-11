package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.util.MqttProperties;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MqttService
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/11 6:07 PM
 */
@Service
public interface MqttService {

    ResultData publish(String topic, JSONObject object);
}
