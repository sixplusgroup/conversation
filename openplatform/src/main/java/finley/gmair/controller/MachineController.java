package finley.gmair.controller;

import finley.gmair.model.machine.MachineDefaultLocation;
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
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供appid和qrcode");
            return result;
        }
        //检查appid的合法性
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询appid失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("请提供正确的appid");
            return result;
        }
        CorpProfile corpProfile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = corpProfile.getProfileId();
        //检查qrcode的合法性
        response = machineService.indoor(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询qrcode失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("请提供正确的qrcode");
            return result;
        }
        //检查appid和qrcode是否已经存在订阅关系
        Map<String, Object> con = new HashMap<>();
        con.put("corpId", corpId);
        con.put("qrcode", qrcode);
        con.put("blockFlag", false);
        response = corpMachineSubsService.fetch(con);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订阅失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("已订阅该机器");
            return result;
        }
        MachineSubscription machineSubscription = new MachineSubscription(corpId, qrcode);
        response = corpMachineSubsService.create(machineSubscription);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            response.setDescription("订阅机器成功");
        } else {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            response.setDescription("订阅失败，请稍后尝试");
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
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供appid和qrcode");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        ResultData response = corpMachineSubsService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未订阅该机器");
            return result;
        }
        MachineSubscription machineSubscription = ((List<MachineSubscription>) response.getData()).get(0);
        String subscriptionId = machineSubscription.getSubscriptionId();
        response = corpMachineSubsService.remove(subscriptionId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("取消订阅设备失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("取消订阅设备成功");
        }
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
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅了该设备二维码");
            return result;
        }
        ResultData response = machineService.indoor(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return result;
        }
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
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅了该设备二维码");
            return result;
        }
        //获取设备所处的城市
        ResultData response = machineService.outdoor(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return result;
        }
        MachineDefaultLocation machineDefaultLocation = ((List<MachineDefaultLocation>) response.getData()).get(0);
        String cityId = machineDefaultLocation.getCityId();
        //根据设备所处的城市id获取该城市的空气数据
        response = airQualityService.airquality(cityId); //从上述response结果中获取cityId
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询空气数据失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到该设备城市的空气数据");
            return result;
        }
        return result;
    }

    @PostMapping("/power/{value}")
    public ResultData power(String appid, String qrcode, @PathVariable("value") String value) {
        ResultData result = new ResultData();
        if (!prerequisities(appid, qrcode)) {

        }

        if ("on".equalsIgnoreCase(value)) {

        }
        if ("off".equalsIgnoreCase(value)) {

        }
        //若value值不为on或者off，提示无法操作


        return result;
    }

    @PostMapping("/speed")
    public ResultData speed(String appid, String qrcode, Integer speed) {
        ResultData result = new ResultData();

        if (!prerequisities(appid, qrcode)) {

        }

        return result;
    }

    private boolean prerequisities(String appid, String qrcode) {
        ResultData result = new ResultData();
        //判断appid是否合法
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return false;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("请提供正确的appid");
            return false;
        }
        CorpProfile corpProfile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = corpProfile.getProfileId();
        //检查该appid是否可以查看该qrcode
        Map<String, Object> con = new HashMap<>();
        con.put("corpId", corpId);
        con.put("qrcode", qrcode);
        con.put("blockFlag", false);
        response = corpMachineSubsService.fetch(con);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return false;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("appid为" + appid + "的企业未订阅该设备");
            return false;
        }
        return true;
    }
}
