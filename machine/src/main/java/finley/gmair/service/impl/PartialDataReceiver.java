package finley.gmair.service.impl;


import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.LatestPM2_5Dao;
import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = "partial-data-queue")
public class PartialDataReceiver {

    @Autowired
    MachineStatusMongoDao machineStatusMongoDao;

    @Autowired
    private LatestPM2_5Dao latestPM2_5Dao;

    @Autowired
    private MachineQrcodeBindDao machineQrcodeBindDao;

    @RabbitHandler
    public void process(String uid) {

        //first query partial pm2_5 by uid in mongo
        ResultData resultData = machineStatusMongoDao.queryPartialLatestPm25(uid,"partial_pm2_5");
        if(resultData.getResponseCode() != ResponseCode.RESPONSE_OK)
            return;
        List<MachinePartialStatus> list = (List<MachinePartialStatus>) resultData.getData();
        int pm2_5 = (int) list.get(0).getData();

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

        //new a thread to compare the latest pm2_5 and the boundary pm2_5

        //according the machineId,find the modelId
        condition.clear();
        condition.put("machineId",uid);
        condition.put("blockFlag",false);
        MachineQrcodeBindVo mqbvo = ((List<MachineQrcodeBindVo>)machineQrcodeBindDao.select(condition).getData()).get(0);
        
    }
}
