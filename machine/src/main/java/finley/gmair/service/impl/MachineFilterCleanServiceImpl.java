package finley.gmair.service.impl;

import finley.gmair.dao.MachineFilterCleanDao;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.service.ConsumerQRcodeBindService;
import finley.gmair.service.MachineFilterCleanService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:32
 * @description: TODO
 */

@Service
public class MachineFilterCleanServiceImpl implements MachineFilterCleanService {

    private Logger logger = LoggerFactory.getLogger(MachineFilterCleanServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MachineFilterCleanDao machineFilterCleanDao;

    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineFilterCleanDao.query(condition);
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
        return fetch(condition);
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

    @Override
    public ResultData sendWeChatMessage(String qrcode) {
        ResultData res=new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        ResultData resultData = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        List<ConsumerQRcodeBind> consumerQRcodeBindList = (List<ConsumerQRcodeBind>) resultData.getData();
        for (ConsumerQRcodeBind consumerQRcodeBind:consumerQRcodeBindList){
            ResultData resultConsumer=restTemplate.getForObject("http://consumer-auth-agent/auth/consumer/profile?consumerId="+consumerQRcodeBind.getConsumerId(),ResultData.class);
            ConsumerVo consumerVo=(ConsumerVo)resultConsumer.getData();
            ResultData resultWeChat=restTemplate.getForObject("http://wechat-agent/wechat/message/sendMessage?openId="+consumerVo.getWechat(),ResultData.class);
        }
        return res;
    }
}
