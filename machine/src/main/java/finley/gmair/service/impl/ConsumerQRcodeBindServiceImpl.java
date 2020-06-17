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
        //create consumer-qrcode bind
        ResultData response = consumerQRcodeBindDao.insert(consumerQRcodeBind);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create qrcode-consumer bind");
            return result;
        }

        //check qrcode exist in prebind
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("codeValue",consumerQRcodeBind.getCodeValue());
        condition.put("blockFlag",false);
        response = preBindDao.query(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }

        PreBindCode preBindCode = ((List<PreBindCode>)response.getData()).get(0);
        //create qrcode-machine bind
        condition.clear();
        condition.put("codeValue",preBindCode.getCodeValue());
        condition.put("blockFlag",false);
        ResultData response1 = machineQrcodeBindDao.select(condition);
        condition.clear();
        condition.put("machineId",preBindCode.getMachineId());
        condition.put("blockFlag",false);
        ResultData response2 = machineQrcodeBindDao.select(condition);
        if(response1.getResponseCode()==ResponseCode.RESPONSE_NULL&&
                response2.getResponseCode()==ResponseCode.RESPONSE_NULL){
            MachineQrcodeBind machineQrcodeBind = new MachineQrcodeBind();
            machineQrcodeBind.setCodeValue(preBindCode.getCodeValue());
            machineQrcodeBind.setMachineId(preBindCode.getMachineId());
            machineQrcodeBindDao.insert(machineQrcodeBind);
        }
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
    public ResultData fetchConsumerQRcodeBindView(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = consumerQRcodeBindDao.query_view(condition);
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

    @Override
    public ResultData queryMachineListView(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = consumerQRcodeBindDao.queryMachineListView(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Not found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find");
        }
        return result;
    }

    @Override
    public ResultData queryMachineSecondListView(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = consumerQRcodeBindDao.queryMachineListSecondView(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Not found from database.");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find");
        }
        return result;
    }
}
