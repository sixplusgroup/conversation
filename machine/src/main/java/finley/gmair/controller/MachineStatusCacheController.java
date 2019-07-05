package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.service.BoardVersionService;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.impl.RedisService;
import finley.gmair.util.MachineUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine")
public class MachineStatusCacheController {

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BoardVersionService boardVersionService;

    //通过uid获取缓存中v1或v2或v3的机器状态
    @RequestMapping(value = "/status/byuid", method = RequestMethod.GET)
    public ResultData machineStatus(String uid) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the uid can not be empty");
            return result;
        }
        if (redisService.exists(uid) == false) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can't find machine status in redis cache");
            return result;
        }
        LimitQueue<Object> statusQueue = (LimitQueue<Object>) redisService.get(uid);
        JSONObject json = MachineUtil.normalize(statusQueue.getLast());
        result.setData(json);
        result.setDescription("success to find machine status in redis cache");
        return result;
    }

    //通过qrcode获取缓存中的机器状态
    @RequestMapping(value = "/status/byqrcode", method = RequestMethod.GET)
    public ResultData getMachineStatusByQRcode(String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the qrcode can not be empty");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the machineId by qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the machineId by qrcode.");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the machineId by qrcode.");
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        return machineStatus(machineId);
    }
}
