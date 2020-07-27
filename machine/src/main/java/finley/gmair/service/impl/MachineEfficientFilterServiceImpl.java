package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.MachineEfficientFilterDao;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/26 11:25
 * @description: TODO
 */

@Service
public class MachineEfficientFilterServiceImpl implements MachineEfficientFilterService {

    private Logger logger = LoggerFactory.getLogger(MachineEfficientFilterServiceImpl.class);

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

    /**
     * 发送微信公众号清洗提醒
     * @param qrcode 设备二维码, number提醒次数（1/2）
     * @return 发送结果
     */
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
                    keyword3.put("value", "主滤网（高效滤网）剩余使用时间少于120小时，需更换");
                }
                else {
                    keyword3.put("value", "主滤网（高效滤网）剩余使用时间少于60小时，急需更换");
                }
                keyword3.put("color", "#173177");

                data.put("keyword1",keyword1);
                data.put("keyword2",keyword2);
                data.put("keyword3",keyword3);

                jsonObject.put("data", data);
                String jsonString = jsonObject.toJSONString();

                ResultData resultWeChat = weChatService.sendFilterReplaceMessage(jsonString,2);
            } catch (Exception ex) {
                logger.error("得到用户或发送消息错误");
                continue;
            }
        }
        return res;
    }
}
