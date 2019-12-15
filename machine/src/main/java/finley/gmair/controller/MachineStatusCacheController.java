package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.service.BoardVersionService;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.QRCodeService;
import finley.gmair.service.impl.RedisService;
import finley.gmair.util.MachineUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.GoodsModelDetailVo;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine")
public class MachineStatusCacheController {

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private RedisService redisService;

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

    /**
     * 通过设备的二维码获取设备的运行状态
     *
     * @param qrcode
     * @return
     */
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
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        return machineStatus(machineId);
    }

    @GetMapping("/{qrcode}/status")
    public ResultData runningStatus(@PathVariable("qrcode") String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供待查询设备的二维码");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        //查询设备二维码对应的MAC绑定信息
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能找到设备二维码与MAC的绑定信息");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        //查询二维码的详细信息
        condition.clear();
        condition.put("codeValue", qrcode);
        response = qrCodeService.profile(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查询到设备二维码对应的产品及型号信息");
            return result;
        }
        String goodsId = ((GoodsModelDetailVo) response.getData()).getGoodsId();
        result = machineStatus(machineId);
        return result;
    }
}
