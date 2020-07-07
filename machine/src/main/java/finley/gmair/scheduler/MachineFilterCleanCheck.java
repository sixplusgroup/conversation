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
        ResultData response = machineFilterCleanService.fetchAll();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return;
        }
        List<MachineFilterClean> store = (List<MachineFilterClean>) response.getData();
        //首先更新所有数据的isNeedClean字段，
        //isNeedClean为true时可以不更新
        for (MachineFilterClean one : store) {
            if (!one.isNeedClean()) {
                ResultData checkRes = machineFilterCleanService.filterCleanCheck(one);
                if (checkRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("check machineFilterClean failed");
                }
            }
        }

        //得到更新之后的数据
        response = machineFilterCleanService.fetchAll();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return;
        }
        store = (List<MachineFilterClean>) response.getData();
        for (MachineFilterClean one : store) {
            //消息提醒开关处于打开状态 && 设备需要清洗 && 本周期内还未提醒
            if (one.isCleanRemindStatus() && one.isNeedClean() && !one.isReminded()) {
                ResultData sendRes = machineFilterCleanService.sendWeChatMessage(one.getQrcode());
                if (sendRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("send weChat message failed");
                }

                Map<String, Object> modification = new HashMap<>();
                modification.put("qrcode", one.getQrcode());
                modification.put("isReminded", true);
                ResultData modifyRes = machineFilterCleanService.modify(modification);
                if (modifyRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("modify isReminded failed");
                }
            }
        }
    }
}
