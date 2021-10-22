package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.MachineService;
import finley.gmair.service.MqttService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备告警相关的交互接口
 *
 * @author lycheeshell
 * @date 2021/1/17 16:28
 */
@RestController
@RequestMapping("/maintenance/alert")
public class AlertController {

    @Resource
    private MqttService mqttService;

    @Resource
    private MachineService machineService;

    /**
     * 查询v3版本的设备现存告警信息
     *
     * @param qrcode 二维码
     * @param code   告警码
     * @return 设备告警列表
     */
    @GetMapping(value = "/getAlertList")
    public ResultData getAlertList(String qrcode, Integer code) {
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        // 检查二维码是不是v3版本
        ResultData boardVersionResponse = machineService.findBoardVersionByQRcode(qrcode);
        if (boardVersionResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return ResultData.error("根据二维码查找设备版本失败");
        }

        JSONArray versionArray = (JSONArray) JSONArray.toJSON(boardVersionResponse.getData());
        if (versionArray.size() == 0) {
            return ResultData.error("根据二维码查找设备版本失败");
        }
        String versionString = versionArray.get(0) + "";
        JSONObject versionJson = JSON.parseObject(versionString);
        int version = versionJson.getInteger("version");
        if (version < 3) {
            return ResultData.empty("设备版本过低，暂时无法查询告警信息");
        }

        // 1.根据二维码查询机器MAC
        ResultData response = machineService.findMachineIdByCodeValueFacetoConsumer(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return ResultData.error("根据二维码查找机器MAC失败");
        }
        MachineQrcodeBindVo machineQrcodeBindVo = JSONArray.parseArray(JSONObject.toJSONString(response.getData()), MachineQrcodeBindVo.class).get(0);
        String machineId = machineQrcodeBindVo.getMachineId();

        // 2.根据machineId查询告警信息
        return mqttService.getExistingAlert(machineId, code);
    }

    /**
     * 消除v3设备的警报
     *
     * @param machineId 设备mac
     * @param code      告警码
     * @return 消除操作的结果
     */
    @PostMapping(value = "/removeAlert")
    public ResultData removeAlert(String machineId, Integer code) {
        if (StringUtils.isEmpty(machineId)) {
            return ResultData.error("qrcode为空");
        }
        if (code == null) {
            return ResultData.error("code为空");
        }

        return mqttService.updateAlert(machineId, code);
    }

}
