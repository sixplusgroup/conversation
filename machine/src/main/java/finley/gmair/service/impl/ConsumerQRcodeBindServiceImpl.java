package finley.gmair.service.impl;

import finley.gmair.dao.ConsumerQRcodeBindDao;
import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.dao.PreBindDao;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.pool.MachinePool;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.GoodsModelDetailVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumerQRcodeBindServiceImpl implements ConsumerQRcodeBindService {

    private Logger logger = LoggerFactory.getLogger(ConsumerQRcodeBindServiceImpl.class);

    private static final String SELECTED_MACHINE_TYPE_ID = "GUO20180607ggxi8a96";

    @Autowired
    private ConsumerQRcodeBindDao consumerQRcodeBindDao;

    @Autowired
    private MachineQrcodeBindDao machineQrcodeBindDao;

    @Autowired
    private MachineFilterCleanService machineFilterCleanService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private MachineTurboVolumeService machineTurboVolumeService;

    @Autowired
    private ModelVolumeService modelVolumeService;

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

            //update table: machine_filter_clean
            updateMachineFilterClean(consumerQRcodeBind);

            //update table: machine_turbo_volume
            updateMachineTurboVolume(consumerQRcodeBind);
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

    //update table: machine_filter_clean
    @Override
    public void updateMachineFilterClean(ConsumerQRcodeBind consumerQRcodeBind){
        MachinePool.getMachinePool().execute(() ->{
            String newBindQRCode = consumerQRcodeBind.getCodeValue();
            ResultData checkMachineType = qrCodeService.profile(newBindQRCode);
            if (checkMachineType.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error(newBindQRCode + "check machine type failed");
            }
            else {
                GoodsModelDetailVo vo = (GoodsModelDetailVo) checkMachineType.getData();
                if (SELECTED_MACHINE_TYPE_ID.equals(vo.getGoodsId())) {
                    //判断是否存在
                    Map<String,Object> condition = new HashMap<>(1);
                    condition.put("qrcode",newBindQRCode);
                    ResultData isExist = machineFilterCleanService.fetch(condition);
                    if (isExist.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                        ResultData addRes = machineFilterCleanService.
                                addNewBindMachine(newBindQRCode);
                        if (addRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                            logger.error(newBindQRCode + ": update machine_filter_clean failed");
                        }
                    }
                    //重新绑定
                    else {
                        condition.clear();
                        condition.put("qrcode",newBindQRCode);
                        condition.put("blockFlag",false);
                        machineFilterCleanService.modify(condition);
                    }
                }
            }
        });
    }

    //update table: machine_turbo_volume
    @Override
    public void updateMachineTurboVolume(ConsumerQRcodeBind consumerQRcodeBind){
        MachinePool.getMachinePool().execute(() ->{
            String newBindQRCode = consumerQRcodeBind.getCodeValue();
            ResultData checkMachineType = modelVolumeService.isNeedTurboVolume(newBindQRCode);
            if (checkMachineType.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error(newBindQRCode + "check machine type failed");
            }
            else {
                //判断是否存在
                Map<String, Object> condition = new HashMap<>(1);
                condition.put("qrcode", newBindQRCode);
                ResultData isExist = machineTurboVolumeService.fetch(condition);
                if (isExist.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    ResultData addRes = machineTurboVolumeService.create(newBindQRCode);
                    if (addRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.error(newBindQRCode + ": update machine_turbo_volume failed");
                    }
                }
                //重新绑定
                else {
                    condition.clear();
                    condition.put("qrcode", newBindQRCode);
                    condition.put("blockFlag", false);
                    machineTurboVolumeService.modify(condition);
                }
            }
        });
    }
}
