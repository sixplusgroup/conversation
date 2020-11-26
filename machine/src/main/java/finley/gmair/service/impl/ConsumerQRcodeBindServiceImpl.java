package finley.gmair.service.impl;

import finley.gmair.dao.ConsumerQRcodeBindDao;
import finley.gmair.dao.MachineEfficientInformationDao;
import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.dao.PreBindDao;
import finley.gmair.model.machine.*;
import finley.gmair.pool.MachinePool;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConsumerQRcodeBindServiceImpl implements ConsumerQRcodeBindService {

    private Logger logger = LoggerFactory.getLogger(ConsumerQRcodeBindServiceImpl.class);

    @Autowired
    private ConsumerQRcodeBindDao consumerQRcodeBindDao;

    @Autowired
    private MachineQrcodeBindDao machineQrcodeBindDao;

    @Autowired
    private MachineFilterCleanService machineFilterCleanService;

    @Autowired
    private MachineEfficientFilterService machineEfficientFilterService;

    @Autowired
    private MachineTurboVolumeService machineTurboVolumeService;

    @Autowired
    private ModelVolumeService modelVolumeService;

    @Autowired
    private PreBindDao preBindDao;

    @Autowired
    private MachineEfficientInformationDao machineEfficientInformationDao;

    @Autowired
    private AuthConsumerService authConsumerService;

    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

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

            //update table: machine_efficient_filter
            updateMachineEfficientFilter(consumerQRcodeBind);
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
    public void updateMachineFilterClean(ConsumerQRcodeBind consumerQRcodeBind) {
        MachinePool.getMachinePool().execute(() ->{
            String newBindQRCode = consumerQRcodeBind.getCodeValue();
            if (machineFilterCleanService.isCorrectGoods(newBindQRCode)) {
                //判断是否存在
                Map<String,Object> condition = new HashMap<>(1);
                condition.put("qrcode",newBindQRCode);
                ResultData isExist = machineFilterCleanService.fetchOne(condition);
                if (isExist.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    ResultData addRes = machineFilterCleanService.
                            addNewBindMachine(newBindQRCode);
                    if (addRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.error(newBindQRCode + ": update machine_filter_clean failed");
                    }
                }
                //重新绑定
                else if(isExist.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    condition.clear();
                    condition.put("codeValue",newBindQRCode);
                    condition.put("consumerId",consumerQRcodeBind.getConsumerId());
                    //判断是否曾经绑定
                    ResultData consumer = consumerQRcodeBindDao.query(condition);
                    //新绑定，更新时间
                    if (consumer.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        condition.clear();
                        condition.put("qrcode", newBindQRCode);
                        condition.put("blockFlag", false);
                        condition.put("lastConfirmTime",new Date());
                        machineFilterCleanService.modify(condition);
                    }
                    //曾经绑定，不更新时间
                    else {
                        condition.clear();
                        condition.put("qrcode", newBindQRCode);
                        condition.put("blockFlag", false);
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
                else if(isExist.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    condition.clear();
                    condition.put("qrcode", newBindQRCode);
                    condition.put("blockFlag", false);
                    machineTurboVolumeService.modify(condition);
                }
            }
        });
    }

    //update table: machine_efficient_filter
    @Override
    public void updateMachineEfficientFilter(ConsumerQRcodeBind consumerQRcodeBind) {
        MachinePool.getMachinePool().execute(() ->{
            String newBindQRCode = consumerQRcodeBind.getCodeValue();
            ResultData modelInfoRes = machineEfficientFilterService.getEfficientModelInfo(newBindQRCode);
            // 当返回码为ResponseCode.RESPONSE_OK时，则此设备具有高效滤网
            if (modelInfoRes.getResponseCode() == ResponseCode.RESPONSE_OK) {
                ModelEfficientConfig one = ((List<ModelEfficientConfig>) modelInfoRes.getData()).get(0);
                // 无firstRemind字段，说明不是GM280或者GM420S设备，需要更新machine_efficient_information表
                if (one.getFirstRemind() == 0) {
                    MachineEfficientInformation oneInfo = new MachineEfficientInformation(
                            newBindQRCode, new Date(), 0, 0, 0);

                    updateMachineEfficientInformation(oneInfo, consumerQRcodeBind);
                }
                // 判断machine_efficient_filter表中是否已存在对应字段
                Map<String,Object> condition = new HashMap<>(1);
                condition.put("qrcode",newBindQRCode);
                ResultData isExist = machineEfficientFilterService.fetch(condition);
                if (isExist.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    ResultData addRes = machineEfficientFilterService.
                            addNewBindMachine(newBindQRCode);
                    if (addRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.error(newBindQRCode + ": update machine_efficient_filter failed");
                    }
                }
                // 重新绑定
                else if(isExist.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    condition.clear();
                    condition.put("qrcode",newBindQRCode);
                    condition.put("blockFlag",false);
                    machineEfficientFilterService.modify(condition);
                }
            }
        });
    }

    //update table: machine_efficient_information
    @Override
    public void updateMachineEfficientInformation(MachineEfficientInformation machineEfficientInformation, ConsumerQRcodeBind consumerQRcodeBind) {
        MachinePool.getMachinePool().execute(() ->{
            String newBindQRCode = machineEfficientInformation.getQrcode();
            // 判断是否存在
            Map<String, Object> condition = new HashMap<>(1);
            condition.put("qrcode", newBindQRCode);
            ResultData isExist = machineEfficientInformationDao.query(condition);
            // 如果不存在则新增一条记录
            if (isExist.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                ResultData addRes = machineEfficientInformationDao.add(machineEfficientInformation);
                if (addRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error(newBindQRCode + ": update machine_efficient_information failed");
                }
            }
            // 如果存在则重新绑定
            else if(isExist.getResponseCode() == ResponseCode.RESPONSE_OK) {
                condition.clear();
                condition.put("codeValue",newBindQRCode);
                condition.put("consumerId",consumerQRcodeBind.getConsumerId());
                //判断是否曾经绑定
                ResultData consumer = consumerQRcodeBindDao.query(condition);
                //新绑定，更新时间
                if (consumer.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    condition.clear();
                    condition.put("qrcode", newBindQRCode);
                    condition.put("blockFlag", false);
                    condition.put("lastConfirmTime",new Date());
                    machineEfficientInformationDao.update(condition);
                }
                //曾经绑定，不更新时间
                else {
                    condition.clear();
                    condition.put("qrcode", newBindQRCode);
                    condition.put("blockFlag", false);
                    machineEfficientInformationDao.update(condition);
                }
            }
        });
    }

    @Override
    public ResultData queryShare(String codeValue) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("codeValue", codeValue);
        //只查看分享的列表 不包括自己
        condition.put("ownership", 1);
        ResultData resultData = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        List<ConsumerQRcodeBind> consumerQRcodeBindList = (List<ConsumerQRcodeBind>) resultData.getData();
        if(consumerQRcodeBindList==null){
            result.setData(null);
            result.setDescription("没有分享给他人的设备");
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        Map<String,Object> lastMap= new HashMap<>();
        List<Map<String,Object>> infoList= new ArrayList<>();
        for(ConsumerQRcodeBind cb:consumerQRcodeBindList){
            ResultData resultConsumer = authConsumerService.profile(cb.getConsumerId());
            if(resultConsumer.getData()==null){
                result.setData(null);
                result.setDescription("没有对应的用户信息，请重试");
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return result;
            }
            Map<String,Object> consumerVo = (Map<String,Object>)resultConsumer.getData();
            Map<String,Object> neededInfo= new HashMap<>();
            neededInfo.put("bindId",cb.getConsumerId());
            neededInfo.put("name", consumerVo.get("name"));
            neededInfo.put("createAt", consumerVo.get("createAt"));
            infoList.add(neededInfo);
        }
        lastMap.put("size",infoList.size());
        lastMap.put("userList",infoList);
        result.setData(lastMap);
        result.setDescription("查询成功");
        return result;
    }


}
