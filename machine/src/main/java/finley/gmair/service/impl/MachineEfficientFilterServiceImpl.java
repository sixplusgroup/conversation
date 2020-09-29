package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.MachineEfficientFilterDao;
import finley.gmair.dao.MachineEfficientInformationDao;
import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.dao.ModelEfficientConfigDao;
import finley.gmair.model.machine.*;
import finley.gmair.service.*;
import finley.gmair.util.CalendarUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: Bright Chan
 * @date: 2020/7/26 11:25
 * @description: MachineEfficientFilterServiceImpl
 */

@Service
public class MachineEfficientFilterServiceImpl implements MachineEfficientFilterService {

    private Logger logger = LoggerFactory.getLogger(MachineEfficientFilterServiceImpl.class);

    private static final int TOTAL_TIME = 2160;

    @Autowired
    private MachineEfficientFilterDao machineEfficientFilterDao;

    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

    @Autowired
    private MachineDefaultLocationService machineDefaultLocationService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private AuthConsumerService authConsumerService;

    @Autowired
    private MachineQrcodeBindDao machineQrcodeBindDao;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private ModelEfficientConfigDao modelEfficientConfigDao;

    @Autowired
    private MachineEfficientInformationDao machineEfficientInformationDao;

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
        ResultData fetchRes = fetch(condition);
        if (fetchRes.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<MachineEfficientFilter> tmpStore = (List<MachineEfficientFilter>) fetchRes.getData();
            res.setData(tmpStore.get(0));
        }
        else {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return res;
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

    @Override
    public ResultData sendWeChatMessage(String qrcode, int number) {
        ResultData res=new ResultData();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return res;
        }

        //得到绑定用户
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag",false);
        ResultData resultData = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        List<ConsumerQRcodeBind> consumerQRcodeBindList = (List<ConsumerQRcodeBind>) resultData.getData();

        //无绑定用户
        if(resultData.getResponseCode()==ResponseCode.RESPONSE_NULL){
            res.setResponseCode(ResponseCode.RESPONSE_NULL);
            return res;
        }

        //得到地址
        ResultData location = machineDefaultLocationService.fetch(condition);
        String locationName="";
        if(location.getResponseCode()==ResponseCode.RESPONSE_OK) {
            MachineDefaultLocation machineDefaultLocation = ((List<MachineDefaultLocation>) location.getData()).get(0);
            if ("null".equals(machineDefaultLocation.getCityId())){
                locationName = "地址暂未绑定";
            }
            else {
                try {
                    ResultData resultConsumer = locationService.idProfile(machineDefaultLocation.getCityId());
                    if (resultConsumer.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        Map<String, String> resultMap = (Map<String, String>) resultConsumer.getData();
                        locationName = resultMap.get("name");
                    }
                } catch (Exception ex) {
                    logger.error("得到地址错误");
                    locationName = "地址暂无";
                }
            }
        }

        for (ConsumerQRcodeBind consumerQRcodeBind:consumerQRcodeBindList){
            String name = qrcode+"("+consumerQRcodeBind.getBindName()+")";
            try {
                ResultData resultConsumer = authConsumerService.profile(consumerQRcodeBind.getConsumerId());
                Map<String,Object> consumerVo = (Map<String,Object>)resultConsumer.getData();

                //未绑定微信
                if ((resultConsumer.getResponseCode()!=ResponseCode.RESPONSE_OK)||(StringUtils.isEmpty((String)consumerVo.get("wechat")))||
                        ("null".equals(consumerVo.get("wechat")))){
                    continue;
                }

                //构造json
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("touser", (String)consumerVo.get("wechat"));   // openid

                JSONObject data = new JSONObject();
                JSONObject keyword1 = new JSONObject();
                keyword1.put("value", name);
                keyword1.put("color", "#173177");
                JSONObject keyword2 = new JSONObject();
                keyword2.put("value", locationName);
                keyword2.put("color", "#173177");
                JSONObject keyword3 = new JSONObject();
                //判断第几次
                if(number == 1){
                    keyword3.put("value", "主滤网（高效滤网）快要到期了，需更换");
                }
                else {
                    keyword3.put("value", "主滤网（高效滤网）快要到期了，急需更换");
                }
                keyword3.put("color", "#173177");

                data.put("keyword1",keyword1);
                data.put("keyword2",keyword2);

                jsonObject.put("data", data);
                String jsonString = jsonObject.toJSONString();

                ResultData resultWeChat = weChatService.sendFilterReplaceMessage(jsonString,2, number);
            } catch (Exception ex) {
                logger.error("得到用户或发送消息错误");
                continue;
            }
        }
        return res;
    }

    @Override
    public ResultData efficientFilterHourlyCheck() {
        ResultData res = new ResultData();

        //根据isRemindedStatus发送WeChat消息
        sendFirstRemind();
        sendSecondRemind();
        return res;
    }

    @Override
    public ResultData specifiedMachineFilterStatusDailyUpdate() {
        ResultData res = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData allMachinesRes = fetch(condition);
        if (allMachinesRes.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<MachineEfficientFilter> allMachinesList =
                                                (List<MachineEfficientFilter>) allMachinesRes.getData();
            for (MachineEfficientFilter one : allMachinesList) {
                String oneQrcode = one.getQrcode();
                if (isSpecifiedMachine(oneQrcode)) {
                    condition.clear();
                    condition.put("qrcode", oneQrcode);
                    condition.put("blockFlag", false);
                    ResultData oneRes = machineEfficientInformationDao.query(condition);
                    if (oneRes.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        MachineEfficientInformation oneInfo =
                                ((List<MachineEfficientInformation>) oneRes.getData()).get(0);
                        EfficientFilterStatus updatedStatus = checkSpecifiedFilterStatus(oneInfo);

                        condition.clear();
                        condition.put("qrcode",oneQrcode);
                        condition.put("replaceStatus",updatedStatus.getValue());
                        ResultData updateRes = machineEfficientFilterDao.update(condition);
                        if (updateRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                            logger.error("update specified machine efficient filter status failed!");
                            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                        }
                    }
                    else
                        res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                }
            }
        }
        else
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return res;
    }

    @Override
    public ResultData updateByRemain(int remain, String uid) {
        ResultData resultData = new ResultData();
        Map<String,Object> condition = new HashMap<>();
        condition.put("machineId",uid);
        condition.put("blockFlag",0);
        ResultData machineQRcodeResult = machineQrcodeBindDao.select(condition);
        String qrcode = "";
        String modelId = "";
        if (machineQRcodeResult.getResponseCode() == ResponseCode.RESPONSE_OK){
            MachineQrcodeBindVo machineQrcodeBindVo = ((List<MachineQrcodeBindVo>)machineQRcodeResult.getData()).get(0);
            qrcode = machineQrcodeBindVo.getCodeValue();
            if (!isGM280OrGM420S(qrcode)) {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                resultData.setDescription("wrong model");
                return resultData;
            }else {
                //得到modelId
                condition.clear();
                condition.put("codeValue", qrcode);
                condition.put("blockFlag",false);
                ResultData response = qrCodeService.fetch(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    QRCode code = ((List<QRCode>) response.getData()).get(0);
                    modelId = code.getModelId();
                }
            }
        }
        else {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            return resultData;
        }


        EfficientFilterStatus status = checkEfficientFilterStatus(remain, modelId);
        condition.clear();
        condition.put("qrcode",qrcode);
        condition.put("replaceStatus",status.getValue());
        resultData = machineEfficientFilterDao.update(condition);
        return resultData;
    }

    @Override
    public ResultData getEfficientModelInfo(String qrcode) {
        ResultData res = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag",false);
        ResultData response = qrCodeService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            QRCode code = ((List<QRCode>) response.getData()).get(0);
            String modelId = code.getModelId();
            //查看是否符合
            condition.clear();
            condition.put("modelId", modelId);
            condition.put("blockFlag",false);
            res = modelEfficientConfigDao.query(condition);
        }
        else {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return res;
    }

    /**
     * 检查设备是否具有高效滤网，且不是GM280和GM420S类型
     * @param qrcode 设备二维码
     * @return 判断结果
     */
    private boolean isSpecifiedMachine(String qrcode) {
        ResultData res = getEfficientModelInfo(qrcode);
        if (res.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ModelEfficientConfig one = ((List<ModelEfficientConfig>) res.getData()).get(0);
            return one.getFirstRemind() == 0;
        }
        return false;
    }

    /**
     * 检查设备是否具有高效滤网，且是GM280和GM420S类型
     * 注意：此方法不是上一个方法(isSpecifiedMachine)的简单取反，需要考虑到设备是否具有高效滤网
     * 当设备不具有高效滤网时，两个方法都返回false.
     * @param qrcode 设备二维码
     * @return 判断结果
     */
    private boolean isGM280OrGM420S(String qrcode) {
        ResultData res = getEfficientModelInfo(qrcode);
        if (res.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ModelEfficientConfig one = ((List<ModelEfficientConfig>) res.getData()).get(0);
            return one.getFirstRemind() != 0;
        }
        return false;
    }

    /**
     * 发送第一次提醒的微信消息
     */
    private void sendFirstRemind() {
        ResultData response = fetchNeedRemindFirst();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("fetchNeedRemindFirst failed");
            return;
        }
        List<MachineEfficientFilter> store = (List<MachineEfficientFilter>) response.getData();
        for (MachineEfficientFilter one : store) {
            String oneQRCode = one.getQrcode();
            ResultData sendRes = sendWeChatMessage(oneQRCode, 1);
            if (sendRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error(oneQRCode + ": send first remind failed");
            }
            else {
                Map<String, Object> modification = new HashMap<>();
                modification.put("qrcode", oneQRCode);
                modification.put("isRemindedStatus", 1);
                ResultData modifyRes = modify(modification);
                if (modifyRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error(oneQRCode + ": modify isRemindedStatus failed");
                }
            }
        }
    }

    /**
     * 发送第二次提醒的微信消息
     */
    private void sendSecondRemind() {
        ResultData response = fetchNeedRemindSecond();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("fetchNeedRemindSecond failed");
            return;
        }
        List<MachineEfficientFilter> store = (List<MachineEfficientFilter>) response.getData();
        for (MachineEfficientFilter one : store) {
            String oneQRCode = one.getQrcode();
            ResultData sendRes = sendWeChatMessage(oneQRCode, 2);
            if (sendRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error(oneQRCode + ": send second remind failed");
            }
            else {
                Map<String, Object> modification = new HashMap<>();
                modification.put("isRemindedStatus", 2);
                modification.put("qrcode", oneQRCode);
                ResultData modifyRes = modify(modification);
                if (modifyRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error(oneQRCode + ": modify isRemindedStatus failed");
                }
            }
        }
    }

    /**
     * 针对GM280和GM420S设备，通过当前滤芯剩余寿命判断设备滤网处于什么状态
     * @param remain 滤芯剩余寿命
     * @return 滤芯状态
     */
    private EfficientFilterStatus checkEfficientFilterStatus(int remain, String modelId) {
        Map<String,Object> condition = new HashMap<>();
        condition.put("modelId",modelId);
        condition.put("blockFlag",false);
        ResultData resultData = modelEfficientConfigDao.query(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK){
            ModelEfficientConfig modelEfficientConfig = ((List<ModelEfficientConfig>)resultData.getData()).get(0);
            int firstRemind = modelEfficientConfig.getFirstRemind();
            int secondRemind = modelEfficientConfig.getSecondRemind();
            if (remain >= firstRemind) {
                return EfficientFilterStatus.NO_NEED;
            }
            else if (remain >= secondRemind) {
                return EfficientFilterStatus.NEED;
            } else {
                return EfficientFilterStatus.URGENT_NEED;
            }
        }
        else {
            return EfficientFilterStatus.NO_NEED;
        }
    }

    /**
     * 针对非GM280和GM420S类型且具有高效滤网的设备，通过给定的逻辑(见machine模块文档)判断设备滤网处于什么状态
     * @param information 该设备的相关信息
     * @return 滤网状态
     */
    private EfficientFilterStatus checkSpecifiedFilterStatus(MachineEfficientInformation information) {
        // 自上次确认更换到当前的时间（小时）
        int substi = (int) CalendarUtil.hoursBetween(information.getLastConfirmTime(), new Date());
        // 设备在线的运行时间
        int running = information.getRunning();
        // 对于420设备，根据PM2.5B（舱内）数值超过25的连续天数(已改为统计PM2.5A)
        int conti = information.getConti();
        // 设备所处城市PM2.5在当前周期内大于75的天数
        int abnormal = information.getAbnormal();
        // 剩余可用时间
        int tAvail = TOTAL_TIME - substi - abnormal * 24 - conti * 24;
        // 剩余可运行时间
        int tRun = TOTAL_TIME - running;

        if (tRun == 0) {
            double res = 0.8 * (1 + Math.abs(tAvail) / (double) TOTAL_TIME);
            if (res >= 1) {
                return EfficientFilterStatus.URGENT_NEED;
            }
            else if (res >= 0.8) {
                return EfficientFilterStatus.NEED;
            }
            else
                return EfficientFilterStatus.NO_NEED;
        }
        else {
            double res = 0.5 * (1 + Math.abs(tAvail) / (double) TOTAL_TIME) +
                    0.3 * (1 + Math.abs(running - TOTAL_TIME) / (double) TOTAL_TIME);
            if (res >= 1) {
                return EfficientFilterStatus.URGENT_NEED;
            }
            else if (res >= 0.8) {
                return EfficientFilterStatus.NEED;
            }
            else
                return EfficientFilterStatus.NO_NEED;
        }
    }
}
