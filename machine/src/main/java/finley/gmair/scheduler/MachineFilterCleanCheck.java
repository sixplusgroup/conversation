package finley.gmair.scheduler;

import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.service.MachineFilterCleanService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/7 19:09
 * @description: TODO
 */

@Component
public class MachineFilterCleanCheck {

    private Logger logger = LoggerFactory.getLogger(MachineFilterCleanCheck.class);

    @Autowired
    private MachineFilterCleanService machineFilterCleanService;

    /**
     * 每天零点查看并更新machine_filter_clean表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyCheck() {
        //首先更新表中的isNeedClean字段
        Map<String, Object> condition = new HashMap<>();
        ResultData response = machineFilterCleanService.updateIsNeedClean(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("machine_filter_clean daily update failed");
            return;
        }

        //得到更新之后的数据
        response = machineFilterCleanService.fetchNeedRemind();
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            logger.info("no machine needs to be reminded");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            logger.error("fetch machines that need to be reminded failed");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<MachineFilterClean> store = (List<MachineFilterClean>) response.getData();
            for (MachineFilterClean one : store) {
                ResultData sendRes = machineFilterCleanService.sendWeChatMessage(one.getQrcode());
                if (sendRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("send weChat message failed");
                }
                else {
                    Map<String, Object> modification = new HashMap<>();
                    modification.put("qrcode", one.getQrcode());
                    modification.put("isReminded", true);
                    ResultData modifyRes = machineFilterCleanService.modify(modification);
                    if (modifyRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.error(one.getQrcode() + ": modify isReminded failed");
                    }
                }
            }
        }
    }
}
