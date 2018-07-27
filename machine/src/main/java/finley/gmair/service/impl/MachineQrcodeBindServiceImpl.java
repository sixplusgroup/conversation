package finley.gmair.service.impl;

import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class MachineQrcodeBindServiceImpl implements MachineQrcodeBindService {

    @Autowired
    private MachineQrcodeBindDao machineQrcodeBindDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineQrcodeBindDao.select(condition);
    }

    @Override
    public ResultData insert(MachineQrcodeBind machineQrcodeBind) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("machineId", machineQrcodeBind.getMachineId());
        ResultData response = machineQrcodeBindDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(String.format("machineId %s 已经被绑定", machineQrcodeBind.getMachineId()));
            return result;
        }
        condition.clear();
        condition.put("blockFlag", false);
        condition.put("codeValue", machineQrcodeBind.getCodeValue());
        response = machineQrcodeBindDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(String.format("二维码 %s 已经被绑定", machineQrcodeBind.getCodeValue()));
            return result;
        }

        return machineQrcodeBindDao.insert(machineQrcodeBind);
    }

    @Override
    public ResultData modifyByQRcode(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = machineQrcodeBindDao.update(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to update the code-machine table");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to update the code-machine table");
            return result;
        }
        return result;
    }

}
