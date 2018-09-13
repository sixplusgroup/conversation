package finley.gmair.service.impl;


import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


//该方法暂时被废弃
@Component
@RabbitListener(queues = "machine-v1-queue")
public class MachineV1StatusReceiver {

    @Autowired
    MachineStatusMongoDao machineStatusMongoDao;

    @Autowired
    RedisService redisService;

    @RabbitHandler
    public void process(String machineId) {
        System.out.println("start handle data!!!!");
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        long current = System.currentTimeMillis();
        condition.put("createAtGTE", current - 30 * 1000);
        ResultData resultData = machineStatusMongoDao.queryMachineV1Status(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            try {
                MachineV1Status mv1s = (MachineV1Status) resultData.getData();
                MachineStatus machineStatus = new MachineStatus(
                        mv1s.getMachineId(), mv1s.getPm25(), mv1s.getTemperature(), mv1s.getHumidity(), mv1s.getCo2(),
                        mv1s.getVelocity(), mv1s.getPower(), mv1s.getWorkMode(), mv1s.getHeat(), mv1s.getLight());
                boolean flag=redisService.set(mv1s.getMachineId(),machineStatus,(long)120);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
