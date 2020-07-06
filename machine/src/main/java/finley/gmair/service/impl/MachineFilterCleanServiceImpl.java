package finley.gmair.service.impl;

import finley.gmair.dao.MachineFilterCleanDao;
import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.service.MachineFilterCleanService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:32
 * @description: TODO
 */

@Service
public class MachineFilterCleanServiceImpl implements MachineFilterCleanService {

    private static final int CLEAN_TIME_INTERVAL = 30;

    @Autowired
    private MachineFilterCleanDao machineFilterCleanDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineFilterCleanDao.query(condition);
    }

    @Override
    public ResultData fetchByQRCode(String qrcode) {
        ResultData res = new ResultData();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return res;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        return fetch(condition);
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        return machineFilterCleanDao.update(condition);
    }

    @Override
    public ResultData filterCleanCheck(MachineFilterClean selectedOne) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();
        resData.put("qrcode", selectedOne.getQrcode());

        if (selectedOne.isNeedClean()) {
            resData.put("isNeedClean", selectedOne.isNeedClean());
        }
        else {
            long dayDiff = (new Date().getTime() - selectedOne.getLastConfirmTime().getTime()) /
                    (1000 * 60 * 60 * 24);
            if (dayDiff >= CLEAN_TIME_INTERVAL) {
                resData.put("isNeedClean", true);
                //更新isNeedClean字段
                Map<String, Object> modification = new HashMap<>();
                modification.put("qrcode", selectedOne.getQrcode());
                modification.put("isNeedClean", true);
                ResultData modifyRes = modify(modification);
                if (modifyRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    res.setDescription("modify MachineFilterClean failed");
                    return res;
                }
            }
            else {
                resData.put("isNeedClean", false);
            }
        }
        res.setData(resData);
        return res;
    }

    @Override
    public ResultData addNewBindMachine(String qrcode) {
        ResultData res = new ResultData();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return res;
        }

        MachineFilterClean newBindMachine = new MachineFilterClean();
        newBindMachine.setQrcode(qrcode);
        return machineFilterCleanDao.add(newBindMachine);
    }
}
