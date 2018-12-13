package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.converters.Auto;
import finley.gmair.model.machine.Ownership;
import finley.gmair.pool.ReceptionPool;
import finley.gmair.service.AirqualityService;
import finley.gmair.service.LogService;
import finley.gmair.service.MachineService;
import finley.gmair.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;


@RestController
@RequestMapping("/reception/machine")
@PropertySource(value = "classpath:/resource.properties")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private LogService logService;

    @Autowired
    private AirqualityService airqualityService;

    @Value("${image_share_path}")
    private String path;

    @Value("${image_save_path}")
    private String fileSavePath;

    @Value("${image_upload_url}")
    private String imageUploadUrl;

    @GetMapping("/check/device/name/binded")
    public ResultData checkDeviceNameExist(String deviceName) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.checkDeviceNameExist(consumerId, deviceName);
    }

    @GetMapping("/check/device/binded")
    public ResultData checkDeviceBinded(String qrcode) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.checkDeviceBinded(consumerId, qrcode);
    }

    //设备初始化时 将qrcode和consumerId绑定
    @PostMapping("/deviceinit")
    public ResultData deviceInit(String qrcode, String deviceName) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.bindConsumerWithQRcode(consumerId, deviceName, qrcode, Ownership.OWNER.getValue());
    }

    @RequestMapping(value = "/consumer/qrcode/unbind", method = RequestMethod.POST)
    public ResultData unbindConsumerWithQRcode(String qrcode) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.unbindConsumerWithQRcode(consumerId, qrcode);
    }

    @PostMapping("/device/bind/share")
    public ResultData acquireControlOn(String qrcode, String deviceName) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.bindConsumerWithQRcode(consumerId, deviceName, qrcode, Ownership.SHARE.getValue());
    }

    /**
     * This method helps to check whether the qrcode is online or not
     *
     * @return online result, RESPONSE_OK & RESPONSE_NULL
     * @Param qrcode, the code value of the specified machine
     */
    @GetMapping("/checkonline")
    public ResultData checkOnline(String qrcode) {
        return machineService.checkOnline(qrcode);
    }

    @PostMapping("/qrcode/status")
    public ResultData findStatusByQRcode(String qrcode) {
        return machineService.checkQRcodeExist(qrcode);
    }

    //发送遥控信息
    @PostMapping("/operate/{component}/{operation}")
    public ResultData configComponentStatus(@PathVariable("component") String component, @PathVariable("operation") String operation, String qrcode, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserAction(consumerId, qrcode, component, new StringBuffer("User ").append(consumerId).append(" operate ").append(component).append(" set to ").append(operation).toString(), IPUtil.getIP(request))));
        return machineService.chooseComponent(qrcode, component, operation);
    }

    //配置风量
    @PostMapping("/config/speed")
    public ResultData configSpeed(String qrcode, int speed, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserAction(consumerId, qrcode, "speed", new StringBuffer("User ").append(consumerId).append(" operate ").append("speed").append(" set to ").append(speed).toString(), IPUtil.getIP(request))));
        return machineService.configSpeed(qrcode, speed);
    }

    @PostMapping("/config/light")
    public ResultData configLight(String qrcode, int light, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserAction(consumerId, qrcode, "light", new StringBuffer("User ").append(consumerId).append(" operate ").append("light").append(" set to ").append(light).toString(), IPUtil.getIP(request))));
        return machineService.configLight(qrcode, light);
    }

    //设置配置项
    @PostMapping("/control/option/create")
    public ResultData setControlOption(String optionName, String optionComponent, String modelId, String actionName, String actionOperator) {
        return machineService.setControlOption(optionName, optionComponent, modelId, actionName, actionOperator);
    }

    //根据modelId查询ControlOption
    @GetMapping("/control/option/probe")
    public ResultData probeControlOption(String modelId) {
        return machineService.probeControlOptionByModelId(modelId);
    }

    //根据当前的qrcode查询这台机器的各种值(co2,pm2.5等)
    @RequestMapping(value = "/info/probe", method = RequestMethod.GET)
    public ResultData getMachineInfo(String qrcode) {
        return machineService.getMachineStatusByQRcode(qrcode);
    }

    //根据consumerId获取用户的machine list
    @RequestMapping(value = "/devicelist", method = RequestMethod.GET)
    public ResultData getUserDeviceList() {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.getMachineListByConsumerId(consumerId);
    }


    @RequestMapping(value = "/probe/qrcode/byurl", method = RequestMethod.POST)
    public ResultData probeQRcodeByUrl(String codeUrl) {
        return machineService.probeQRcodeByUrl(codeUrl);
    }


    @RequestMapping(value = "/model/query/by/modelid", method = RequestMethod.GET)
    public ResultData queryModelByModelId(String modelId) {
        return machineService.queryModelByModelId(modelId);
    }

    @RequestMapping(value = "/probe/cityId/byqrcode", method = RequestMethod.GET)
    public ResultData probeCityIdByQRcode(String qrcode) {
        return machineService.probeCityIdByQRcode(qrcode);
    }

    @RequestMapping(value = "/probe/volume", method = RequestMethod.GET)
    public ResultData probeVolumeLimitationByModelId(String modelId) {
        return machineService.probeModelVolumeByModelId(modelId);
    }

    @RequestMapping(value = "/probe/light", method = RequestMethod.GET)
    public ResultData probeLightLimitationByModelId(String modelId) {
        return machineService.probeModelLightByModelId(modelId);
    }


    @RequestMapping(value = "/probe/board/version", method = RequestMethod.GET)
    public ResultData probeBoardVersionByQRcode(String qrcode) {
        return machineService.findBoardVersionByQRcode(qrcode);
    }

    @RequestMapping(value = "/probe/hourly/pm25", method = RequestMethod.GET)
    public ResultData probeMachineHourlyPm2_5(String qrcode) {
        return machineService.fetchMachineHourlyPm2_5(qrcode);
    }

    @RequestMapping(value = "/probe/daily/pm25", method = RequestMethod.GET)
    public ResultData probeMachineDailyPm2_5(String qrcode) {
        return machineService.fetchMachineDailyPm2_5(qrcode);
    }

    @RequestMapping(value = "/modify/bind/name", method = RequestMethod.POST)
    public ResultData modifyBindName(String qrcode, String bindName) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.modifyBindName(qrcode, bindName, consumerId);
    }

    @RequestMapping(value = "/consumer/bind/probe/byqrcode", method = RequestMethod.GET)
    public ResultData probeConsumerQRcodeBindInfo(String qrcode) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.probeBindByQRcode(qrcode, consumerId);
    }

    @RequestMapping(value = "/model/component/probe", method = RequestMethod.GET)
    public ResultData fetchModelEnabledComponent(String modelId, String componentName) {
        return machineService.fetchModelEnabledComponent(modelId, componentName);
    }

    //创建定时开关配置
    @PostMapping(value = "/confirm/timing/power")
    public ResultData confirmPower(String qrcode, int startHour, int startMinute, int endHour, int endMinute, boolean status, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserAction(consumerId, qrcode, "power", new StringBuffer("User ").append(consumerId).append(" operate ").append("power")
                .append(" set start to ").append(startHour).append(":").append(startMinute)
                .append(" and end to ").append(endHour).append(":").append(endMinute)
                .append(" status").append(status).toString(), IPUtil.getIP(request))));
        return machineService.confirmPowerOnoff(qrcode, startHour, startMinute, endHour, endMinute, status);
    }

    //获取当前机器定时开关机状态
    @GetMapping(value = "/probe/onoff/status/by/code")
    public ResultData probeStatus(String qrcode, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserAction(consumerId, qrcode, "status", new StringBuffer("User ").append(consumerId).append(" power ").append("status").toString(), IPUtil.getIP(request))));
        return machineService.getRecord(qrcode);
    }

    @PostMapping("/share")
    public ResultData share(String qrcode) {
        ResultData result = new ResultData();
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取机器的实时数据
        int version, pm2_5, temperature = 0, humidity = 0, co2 = 0;
        ResultData response = machineService.getMachineStatusByQRcode(qrcode);
        //如果能够获取到实时数据，则绘制实时数据部分
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前无法获取室内的数值信息");
            return result;
        }
        JSONObject machine = JSONObject.parseObject(JSON.toJSONString(response.getData()));
        pm2_5 = machine.getInteger("pm2_5");
        if (machine.containsKey("temp")) {
            version = 1;
            temperature = machine.getInteger("temp");
            humidity = machine.getInteger("humid");
        }
        if (machine.containsKey("temperature")) {
            version = 2;
            temperature = machine.getInteger("temperature");
            humidity = machine.getInteger("humidity");
            co2 = machine.getInteger("co2");
        }
        //获取机器的室外地址
        response = machineService.probeCityIdByQRcode(qrcode);
        //如果能够获取到室外配置，则显示室外城市信息
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            BufferedImage bufferedImageTemp = share(path, "果麦新风", pm2_5, temperature, humidity, co2);
            //把处理完的图片上传到服务器
            String fileName = String.format("%s/%s.jpg", fileSavePath, IDGenerator.generate("pic"));
            try {
                ImageIO.write(bufferedImageTemp, "jpg", new File(fileName));
                UploadUtil.uploadImage(imageUploadUrl, fileName, "file");
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前无法获取室外的城市信息");
            return result;
        }
        JSONObject location = JSON.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
        String cityId = location.getString("cityId");
        //获取当前室外的空气信息，包括AQI指数，主要污染物，PM2.5, PM10, 一氧化碳，二氧化氮，臭氧，二氧化硫
        response = airqualityService.getLatestCityAirQuality(cityId);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            BufferedImage bufferedImageTemp = share(path, "果麦新风", pm2_5, temperature, humidity, co2);
            //把处理完的图片上传到服务器
            String fileName = String.format("%s/%s.jpg", fileSavePath, IDGenerator.generate("pic"));
            try {
                ImageIO.write(bufferedImageTemp, "jpg", new File(fileName));
                UploadUtil.uploadImage(imageUploadUrl, fileName, "file");
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前无法获取最新的城市PM2.5信息");
            return result;
        }
        int outdoorPM2_5, aqi, pm10;
        double co, no2, o3, so2;
        JSONObject outdoor = JSONArray.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
        outdoorPM2_5 = outdoor.getInteger("pm2_5");
        aqi = outdoor.getInteger("aqi");
        pm10 = outdoor.getInteger("pm10");
        String primary = outdoor.getString("primePollution");
        co = outdoor.getDouble("co");
        no2 = outdoor.getDouble("no2");
        o3 = outdoor.getDouble("o3");
        so2 = outdoor.getDouble("so2");
        BufferedImage bufferedImage = share(path, "果麦新风", pm2_5, temperature, humidity, co2, outdoorPM2_5, aqi, primary, pm10, co, no2, o3, so2);
        //把处理完的图片上传到服务器
        String fileName = String.format("%s/%s.jpg", fileSavePath, IDGenerator.generate("pic"));
        try {
            ImageIO.write(bufferedImage, "jpg", new File(fileName));
            UploadUtil.uploadImage(imageUploadUrl, fileName, "file");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取室外的连续7天的空气数据
        //response = machineService.fetchMachineDailyPm2_5(qrcode);
        //获取室内的连续7天的空气数据
        //airqualityService.getDailyCityAqi(cityId);
        return result;
    }

    private BufferedImage share(String path, String name, int pm2_5, int temperature, int humidity, int co2) {
        return ImageShareUtil.share(path, name, pm2_5, temperature, humidity, co2);
    }

    private BufferedImage share(String path, String name, int pm2_5, int temperature, int humidity, int co2, int outPM2_5, int aqi, String primary, int pm10, double co, double no2, double o3, double so2) {
        return ImageShareUtil.share(path, name, pm2_5, temperature, humidity, co2, outPM2_5, aqi, primary, pm10, co, no2, o3, so2);
    }
}
