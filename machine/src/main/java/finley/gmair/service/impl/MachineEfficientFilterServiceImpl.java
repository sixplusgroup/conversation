package finley.gmair.service.impl;

import finley.gmair.dao.MachineEfficientFilterDao;
import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.service.MachineEfficientFilterService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/26 11:25
 * @description: TODO
 */

@Service
public class MachineEfficientFilterServiceImpl implements MachineEfficientFilterService {

    @Autowired
    private MachineEfficientFilterDao machineEfficientFilterDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineEfficientFilterDao.query(condition);
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
        return machineEfficientFilterDao.update(condition);
    }

    @Override
    public ResultData addNewBindMachine(String qrcode) {
        ResultData res = new ResultData();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return res;
        }

        //检测是否已存在此二维码
        ResultData response = fetchByQRCode(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode already exists");
            return res;
        }

        MachineEfficientFilter newBindMachine = new MachineEfficientFilter();
        newBindMachine.setQrcode(qrcode);
        return machineEfficientFilterDao.add(newBindMachine);
    }

    @Override
    public ResultData fetchNeedRemindFirst() {
        return machineEfficientFilterDao.queryNeedRemindFirst();
    }

    @Override
    public ResultData fetchNeedRemindSecond() {
        return machineEfficientFilterDao.queryNeedRemindSecond();
    }
}
