package finley.gmair.service.impl;

import finley.gmair.dao.ConsumerQRcodeBindDao;
import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.dao.PreBindDao;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.service.ConsumerQRcodeBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumerQRcodeBindServiceImpl implements ConsumerQRcodeBindService {
    @Autowired
    private ConsumerQRcodeBindDao consumerQRcodeBindDao;

    @Autowired
    private MachineQrcodeBindDao machineQrcodeBindDao;


    @Autowired PreBindDao preBindDao;

    @Transactional
    @Override
    public ResultData createConsumerQRcodeBind(ConsumerQRcodeBind consumerQRcodeBind){
        ResultData result = new ResultData();

        //check qrcode exist in prebind
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("codeValue",consumerQRcodeBind.getCodeValue());
        condition.put("blockFlag",false);
        ResultData response = preBindDao.query(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setData(response.getData());
            result.setDescription("not find qrcode in prebind");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch prebind table");
            return result;
        }
        PreBindCode preBindCode = ((List<PreBindCode>)response.getData()).get(0);

        //check qrcode exist in qrcode_machine_bind table
        condition.clear();
        condition.put("codeValue",preBindCode.getCodeValue());
        condition.put("blockFlag",false);
        response = machineQrcodeBindDao.select(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("exist qrcode in code-machine-bind table");
            return result;
        }

        //check machineId exist in qrcode_machine_bind table
        condition.clear();
        condition.put("machineId",preBindCode.getMachineId());
        condition.put("blockFlag",false);
        response = machineQrcodeBindDao.select(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("exist machineId in code-machine-bind table");
            return result;
        }

        //create qrcode-machine bind
        MachineQrcodeBind machineQrcodeBind = new MachineQrcodeBind();
        machineQrcodeBind.setCodeValue(preBindCode.getCodeValue());
        machineQrcodeBind.setMachineId(preBindCode.getMachineId());
        response = machineQrcodeBindDao.insert(machineQrcodeBind);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create qrcode-machine bind");
            return result;
        }

        //create consumer-qrcode bind
        response = consumerQRcodeBindDao.insert(consumerQRcodeBind);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create qrcode-consumer bind");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchConsumerQRcodeBind(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = consumerQRcodeBindDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No bind found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find bind");
        }
        return result;
    }

    @Override
    public ResultData modifyConsumerQRcodeBind(Map<String, Object> condition){
        ResultData result= new ResultData();
        ResultData response = consumerQRcodeBindDao.update(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK)
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to modify bind");
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to modify bind");
        }
        return result;
    }
}
