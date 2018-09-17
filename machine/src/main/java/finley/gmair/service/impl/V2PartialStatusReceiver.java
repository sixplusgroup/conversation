package finley.gmair.service.impl;


import finley.gmair.dao.LatestPM2_5Dao;
import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.service.CoreV2Service;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "partial-data-queue")
public class V2PartialStatusReceiver {

    @Autowired
    MachineStatusMongoDao machineStatusMongoDao;

    @Autowired
    private LatestPM2_5Dao latestPM2_5Dao;

    @Autowired
    private CoreV2Service coreV2Service;

    @RabbitHandler
    public void process(String uid) {

        //first query partial pm2_5 by uid in mongo
        ResultData resultData = machineStatusMongoDao.queryPartialLatestPm25(uid,"partial_pm2_5");
        if(resultData.getResponseCode() != ResponseCode.RESPONSE_OK)
            return;
        MachinePartialStatus machinePartialStatus = (MachinePartialStatus) resultData.getData();
        int pm2_5 = (int) machinePartialStatus.getData();

        //如果上一个小时的滤网pm25值小于25,那么关掉滤网警戒灯.
        if (pm2_5 < 25) {
            coreV2Service.configScreen(uid, 0);
        }

        //check if machineId exist in pm_2_5_latest table
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId",uid);
        condition.put("blockFlag",false);
        ResultData response = latestPM2_5Dao.query(condition);
        //the machineId exist,just update
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            condition.clear();
            condition.put("machineId",uid);
            condition.put("pm2_5",pm2_5);
            latestPM2_5Dao.updateByMachineId(condition);
        }
        //the machineId is not exist,insert
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            LatestPM2_5 latestPM2_5 = new LatestPM2_5(uid,pm2_5);
            latestPM2_5Dao.insert(latestPM2_5);
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            return;
        }


    }
}
