package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.model.openplatform.MachineSubscription;
import finley.gmair.service.AirQualityService;
import finley.gmair.service.CorpMachineSubsService;
import finley.gmair.service.CorpProfileService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MachineController
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/7 2:00 PM
 */
@RestController
@RequestMapping("/openplatform/machine")
public class MachineController {
    private Logger logger = LoggerFactory.getLogger(MachineController.class);

    @Autowired
    private MachineService machineService;

    @Autowired
    private AirQualityService airQualityService;

    @Autowired
    private CorpProfileService corpProfileService;

    @Autowired
    private CorpMachineSubsService corpMachineSubsService;


    /**
     * appid 订阅设备
     *
     * @param appid
     * @param qrcode
     * @return
     */
    @PostMapping("/subscribe")
    public ResultData subscribe(String appid, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid和qrcode");
            return result;
        }
        //检查appid的合法性
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid");
            return result;
        }
        CorpProfile corpProfile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = corpProfile.getProfileId();
        //检查qrcode的合法性
        response = machineService.indoor(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的qrcode");
            return result;
        }
        //检查是否已经订阅了该设备
        condition.clear();
        condition.put("corpId", corpId);
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        response = corpMachineSubsService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询与该设备的订阅关系失败");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("已订阅该设备");
            return result;
        }
        //订阅设备
        MachineSubscription machineSubscription = new MachineSubscription(corpId, qrcode);
        response = corpMachineSubsService.create(machineSubscription);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            response.setResponseCode(ResponseCode.RESPONSE_OK);
            response.setDescription("订阅成功");
        } else {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            response.setDescription("订阅失败");
        }
        return result;
    }

    /**
     * appid 取消设备订阅
     *
     * @param appid
     * @param qrcode
     * @return
     */
    @PostMapping("/unsubscribe")
    public ResultData unsubscribe(String appid, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid和qrcode");
            return result;
        }
        //检查appid的合法性
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid");
            return result;
        }
        CorpProfile corpProfile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = corpProfile.getProfileId();
        //检查是否订阅了该设备
        condition.clear();
        condition.put("corpId", corpId);
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        response = corpMachineSubsService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询与该设备的订阅关系失败");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未订阅该设备");
            return result;
        }
        //取关设备
        MachineSubscription machineSubscription = ((List<MachineSubscription>) response.getData()).get(0);
        String subscriptionId = machineSubscription.getSubscriptionId();
        response = corpMachineSubsService.remove(subscriptionId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("取消订阅设备失败");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("取消订阅设备成功");
        }
        return result;
    }

    /**
     * appid 获取订阅设备列表
     *
     * @param appid
     * @return
     */
    @GetMapping("/subscriptions")
    public ResultData subscription(String appid) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid");
            return result;
        }
        //检查appid合法性
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid");
            return result;
        }
        CorpProfile profile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = profile.getProfileId();
        //根据corpId查询订阅信息
        condition.clear();
        condition.put("corpId", corpId);
        condition.put("blockFlag", false);
        response = corpMachineSubsService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未查询到设备订阅信息");
            return result;
        }
        JSONArray data = new JSONArray();
        for (MachineSubscription subscription : (List<MachineSubscription>) response.getData()) {
            JSONObject temp = ((JSONObject) JSONObject.parse(JSON.toJSONString(subscription)));
            temp.remove("subscriptionId");
            temp.remove("corpId");
            temp.remove("blockFlag");
            data.add(temp);
        }
        result.setData(data);
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }


    /**
     * 获取设备的状态信息
     *
     * @param appid
     * @param qrcode
     * @return
     */
    @GetMapping("/indoor")
    public ResultData indoor(String appid, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid和qrcode");
            return result;
        }
        //检查appid和qrcode是否存在订阅关系
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅该设备二维码");
            return result;
        }
        //获取设备状态信息
        ResultData response = machineService.indoor(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取设备状态信息失败");
            return result;
        }
        //去除设备的UID信息，加入设备的二维码信息
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(response.getData()));
        json.remove("uid");
        json.put("qrcode", qrcode);
        result.setData(json);
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }

    /**
     * 获取设备所处城市的空气信息
     *
     * @param appid
     * @param qrcode
     * @return
     */
    @GetMapping("/outdoor")
    public ResultData outdoor(String appid, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid和qrcode");
            return result;
        }
        //检查appid和qrcode是否存在订阅关系
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅该设备二维码");
            return result;
        }
        //获取设备所处的城市
        ResultData response = machineService.outdoor(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取设备所处位置失败");
            return result;
        }
        JSONObject json = JSONArray.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
        String cityId = json.getString("cityId");
        //根据设备所处的城市id获取该城市的空气数据
        response = airQualityService.airquality(cityId); //从上述response结果中获取cityId
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询该城市空气数据失败");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到该设备城市的空气数据");
            return result;
        }
        json = JSONArray.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
        json.remove("blockFlag");
        json.remove("recordTime");
        json.remove("url");
        result.setData(json);
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }

    /**
     * 开关设备
     *
     * @param appid
     * @param qrcode
     * @param value
     * @return
     */
    @PostMapping("/power/{value}")
    public ResultData power(String appid, String qrcode, @PathVariable("value") String value) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供appid和qrcode");
            return result;
        }
        //检查appid和qrcode是否存在订阅关系
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅了该设备二维码");
            return result;
        }
        //开关机操作
        if ("on".equalsIgnoreCase(value)) {
            ResultData response = machineService.power(qrcode, "power", "on");
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("开机失败");
                return result;
            }
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else if ("off".equalsIgnoreCase(value)) {
            ResultData response = machineService.power(qrcode, "power", "off");
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("关机失败");
                return result;
            }
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            //若value值不为on或者off，提示无法操作
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("无法操作，请输入合法值");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("操作成功");
        return result;
    }

    /**
     * 调节风量
     *
     * @param appid
     * @param qrcode
     * @param speed
     * @return
     */
    @PostMapping("/speed")
    public ResultData speed(String appid, String qrcode, Integer speed) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid和qrcode");
            return result;
        }
        //检查appid和qrcode是否存在订阅关系
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅该设备二维码");
            return result;
        }
        //调节风量
        ResultData response = machineService.speed(qrcode, speed);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("已调节风量");
        }
        return result;
    }

    /**
     * 判断appid和qrcode是否存在订阅关系
     *
     * @param appid
     * @param qrcode
     * @return
     */
    private boolean prerequisities(String appid, String qrcode) {
        ResultData result = new ResultData();
        //判断appid是否合法
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("检查appid合法性失败");
            return false;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("请提供正确的appid");
            return false;
        }
        CorpProfile corpProfile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = corpProfile.getProfileId();
        //检查该appid和qrcode是否存在订阅关系
        Map<String, Object> con = new HashMap<>();
        con.put("corpId", corpId);
        con.put("qrcode", qrcode);
        con.put("blockFlag", false);
        response = corpMachineSubsService.fetch(con);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询与该设备的订阅关系失败");
            return false;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未订阅该设备");
            return false;
        }
        return true;
    }

    /**
     * 根据qrcode提供可调风量范围
     *
     * @param qrcode
     * @param appid
     * @return
     */
    @PostMapping("/speed/range")
    public ResultData speedRange(String qrcode, String appid) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供正确的appid和qrcode");
            return result;
        }
        //检查该appid是否订阅该qrcode
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅了该设备二维码");
            return result;
        }
        ResultData response = machineService.getModel(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未查询到该设备的model_id");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询该设备model_id失败");
            return result;
        }
        JSONObject json = JSONArray.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
        String modelId = json.getString("modelId");
        //根据modelId查 model_volume_config表，取风量范围
        response = machineService.probeModelVolumeByModelId(modelId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到风量可调范围");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询风量可调范围失败");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }

}
