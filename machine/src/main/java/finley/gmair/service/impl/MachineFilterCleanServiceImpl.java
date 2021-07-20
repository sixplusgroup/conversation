package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.MachineFilterCleanDao;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.GoodsModelDetailVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:32
 * @description: MachineFilterCleanServiceImpl
 */

@Service
public class MachineFilterCleanServiceImpl implements MachineFilterCleanService {

    private Logger logger = LoggerFactory.getLogger(MachineFilterCleanServiceImpl.class);

    //具有初效滤网的设备的goodsId
    private static final String[] MACHINE_FILTER_GOODS_ID = {"GUO20180607ggxi8a96"};

    @Autowired
    private MachineFilterCleanDao machineFilterCleanDao;

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
    private QRCodeService qrCodeService;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineFilterCleanDao.query(condition);
    }

    @Override
    public ResultData fetchOne(Map<String, Object> condition) {
        return machineFilterCleanDao.queryOne(condition);
    }

    @Override
    public ResultData fetchNeedRemind() {
        return machineFilterCleanDao.queryNeedRemind();
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
        return fetchOne(condition);
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        return machineFilterCleanDao.update(condition);
    }

    @Override
    public ResultData updateIsNeedClean(Map<String, Object> condition) {
        return machineFilterCleanDao.updateIsNeedClean(condition);
    }

    @Override
    public ResultData filterCleanCheck(String qrcode) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();

        //调用更新方法，更新isNeedClean字段
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        ResultData response = updateIsNeedClean(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error(qrcode + ": update isNeedClean failed");
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("update isNeedClean failed");
            return res;
        }

        //得到更新之后的结果
        response = fetchByQRCode(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error(qrcode + ": fetch machineFilterClean failed");
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("fetch machineFilterClean failed");
            return res;
        }
        MachineFilterClean oneAfterUpdate = (MachineFilterClean) response.getData();
        resData.put("qrcode", qrcode);
        resData.put("isNeedClean", oneAfterUpdate.isNeedClean());
        res.setData(resData);
        return res;
    }

    @Override
    public ResultData filterCleanDailyCheck() {
        ResultData res = new ResultData();
        //首先更新表中的isNeedClean字段
        Map<String, Object> condition = new HashMap<>();
        ResultData response = updateIsNeedClean(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("machine_filter_clean daily update failed");
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("machine_filter_clean daily update failed");
            return res;
        }

        //从更新之后的数据得到需要提醒的设备数据并发送消息提醒
        response = fetchNeedRemind();
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            logger.info("no machine needs to be reminded");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            logger.error("fetch machines that need to be reminded failed");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<MachineFilterClean> store = (List<MachineFilterClean>) response.getData();
            for (MachineFilterClean one : store) {
                ResultData sendRes = sendWeChatMessage(one.getQrcode());
                if (sendRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("send weChat message failed");
                }
                else {
                    Map<String, Object> modification = new HashMap<>();
                    modification.put("qrcode", one.getQrcode());
                    modification.put("isReminded", true);
                    ResultData modifyRes = modify(modification);
                    if (modifyRes.getResponseCode() != ResponseCode.RESPONSE_OK) {
                        logger.error(one.getQrcode() + ": modify isReminded failed");
                    }
                }
            }
        }
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

        //检测是否已存在此二维码
        ResultData response = fetchByQRCode(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode already exists");
            return res;
        }

        MachineFilterClean newBindMachine = new MachineFilterClean();
        newBindMachine.setQrcode(qrcode);
        newBindMachine.setCleanRemindStatus(true);
        newBindMachine.setLastConfirmTime(new Date());
        return machineFilterCleanDao.add(newBindMachine);
    }

    @Override
    public ResultData sendWeChatMessage(String qrcode) {
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
                keyword3.put("value", "初效滤网待清洁");
                keyword3.put("color", "#173177");

                data.put("keyword1",keyword1);
                data.put("keyword2",keyword2);
                data.put("keyword3",keyword3);

                jsonObject.put("data", data);
                String jsonString = jsonObject.toJSONString();

                ResultData resultWeChat = weChatService.sendFilterCleanMessage(jsonString,2);
            } catch (Exception ex) {
                logger.error("得到用户或发送消息错误");
                continue;
            }
        }
        return res;
    }

    @Override
    public boolean isCorrectGoods(String qrcode) {
        ResultData checkMachineType = qrCodeService.profile(qrcode);
        if (checkMachineType.getResponseCode() == ResponseCode.RESPONSE_OK) {
            GoodsModelDetailVo vo = (GoodsModelDetailVo) checkMachineType.getData();
            ArrayList<String> tmpStore = new ArrayList<>(Arrays.asList(MACHINE_FILTER_GOODS_ID));
            return tmpStore.contains(vo.getGoodsId());
        }
        return false;
    }
}
