package finley.gmair.service.impl;


import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.service.MachineStatusCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "machine-queue")
public class MachineReceiver {

    @Autowired
    MachineStatusMongoDao machineStatusMongoDao;

    @Autowired
    MachineStatusCacheService machineStatusCacheService;

    @Autowired
    RedisService redisService;

    @RabbitHandler
    public void process(String uid) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("uid", uid);
        long current = System.currentTimeMillis();
        condition.put("createAtGTE", current - 30 * 1000);
        ResultData resultData = machineStatusMongoDao.query(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            //System.out.println(JSONObject.toJSON(resultData.getData()));
            try {
                redisService.set(((MachineStatus)resultData.getData()).getUid(),resultData.getData(),(long)120);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
