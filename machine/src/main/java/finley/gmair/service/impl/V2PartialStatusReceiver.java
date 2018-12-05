package finley.gmair.service.impl;


import finley.gmair.dao.OutPm25HourlyDao;
import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.FilterLight;
import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.service.CoreV2Service;
import finley.gmair.service.FilterLightService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "partial-data-queue")
public class V2PartialStatusReceiver {

    @Autowired
    MachineStatusMongoDao machineStatusMongoDao;

    @Autowired
    private OutPm25HourlyDao outPm25HourlyDao;


    @RabbitHandler
    public void process(String uid) {
        try {
            //first query partial pm2_5 by uid in mongo
            ResultData resultData = machineStatusMongoDao.queryPartialLatestPm25(uid, "partial_pm2_5");
            if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK)
                return;
            MachinePartialStatus machinePartialStatus = (MachinePartialStatus) resultData.getData();
            int pm2_5 = (int) machinePartialStatus.getData();

            //check if machineId exist in pm_2_5_latest table
            //the machineId exist,just update
            OutPm25Hourly outPm25Hourly = new OutPm25Hourly(uid, pm2_5, LocalDateTime.now().getHour());
            outPm25HourlyDao.insert(outPm25Hourly);
        } catch (Exception e) {


        }
    }
}
