package finley.gmair.controller;

import finley.gmair.model.mqtt.SurplusPayload;
import finley.gmair.service.impl.SurplusMsgReceiver;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/29 18:51
 * @description: TODO
 */

@RestController
@RequestMapping("/surplus")
public class SurplusController {

    //最大重试次数
    private static final int MAX_RETRY_TIMES = 1000;

    @Autowired
    private SurplusMsgReceiver surplusMsgReceiver;

    @Autowired
    private MessageController messageController;

    @GetMapping("")
    public ResultData getSurplus(@RequestParam String uid) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();
        if (StringUtils.isEmpty(uid)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("Please make sure you fill all the required fields");
            return res;
        }

        //要求设备立即上报数据
        messageController.demandReport(uid);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //得到设备上报的数据
        SurplusPayload surplusPayload = surplusMsgReceiver.getLatestSurplus();
//        int retryTimes = 0;
//        //当获得的当前的surplus不是指定设备的数据时，重复发送上报请求
//        while ((surplusPayload == null || !uid.equals(surplusPayload.getMachineId()))
//                && retryTimes < MAX_RETRY_TIMES) {
//            messageController.demandReport(uid);
//            surplusPayload = mqMsgReceiver.getLatestSurplus();
//            retryTimes++;
//        }

        if (surplusPayload == null || !uid.equals(surplusPayload.getMachineId())) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription(uid + ": get surplus failed");
            return res;
        }

        resData.put("surplus", surplusPayload.getRemain());
        res.setData(resData);
        return res;
    }
}
