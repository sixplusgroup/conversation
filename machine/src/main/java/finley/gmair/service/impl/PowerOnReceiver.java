package finley.gmair.service.impl;

import finley.gmair.dao.BoardVersionDao;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.service.CoreV1Service;
import finley.gmair.service.CoreV2Service;
import finley.gmair.service.CoreV3Service;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = "turn-on-queue")
public class PowerOnReceiver {
    private Logger logger = LoggerFactory.getLogger(PowerOnReceiver.class);

    @Autowired
    private BoardVersionDao boardVersionDao;

    @Autowired
    private CoreV1Service coreV1Service;

    @Autowired
    private CoreV2Service coreV2Service;

    @Autowired
    private CoreV3Service coreV3Service;

    @RabbitHandler
    public void process(String uid) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", uid);
        condition.put("blockFlag", false);
        ResultData response = boardVersionDao.queryBoardVersion(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return;
        }
        BoardVersion boardVersion = ((List<BoardVersion>) response.getData()).get(0);
        switch (boardVersion.getVersion()) {
            case 1:
                new Thread(() -> {
                    for (int i = 0; i < 6; i++) {
                        try {
                            coreV1Service.configPower(uid, 1, 1);
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {

                        }
                    }
                }).start();
                //coreV1Service.configPower(uid, 1, 1);
                break;
            case 2:
                new Thread(() -> {
                    for (int i = 0; i < 6; i++) {
                        try {
                            coreV2Service.configPower(uid, 1, 2);
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {

                        }
                    }
                }).start();
                //coreV2Service.configPower(uid, 1, 2);
                break;
            case 3:
                try {
                    coreV3Service.configPower(uid, 1);
                } catch (Exception e) {
                    logger.error("[Error: ] ".concat(uid).concat(" fail to be turned on."));
                    logger.error(e.getMessage());
                }
                break;
        }
    }
}
