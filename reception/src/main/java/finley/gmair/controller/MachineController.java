package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.machine.Ownership;
import finley.gmair.pool.ReceptionPool;
import finley.gmair.service.*;
import finley.gmair.util.*;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

@CrossOrigin
@RestController
@RequestMapping("/reception/machine")
@PropertySource(value = "classpath:/resource.properties")
public class MachineController {

    private Logger logger = LoggerFactory.getLogger(MachineController.class);

    @Autowired
    private MachineService machineService;

    @Autowired
    private LogService logService;

    @Autowired
    private AirqualityService airqualityService;

    @Autowired
    private WechatFormService wechatFormService;

    @Autowired
    private AuthConsumerService authConsumerService;

    @Autowired
    private LocationService locationService;

    @Value("${image_share_path}")
    private String path;

    @Value("${image_save_path}")
    private String fileSavePath;

    /**
     * 用该接口校验二维码是否存在，该请求是用户在扫码绑定阶段第一个需要请求的接口
     *
     * @param qrcode
     * @return
     */
    @PostMapping("/qrcode/status")
    public ResultData findStatusByQRcode(String qrcode) {
        return machineService.checkQRcodeExist(qrcode);
    }

    /**
     * 检查当前二维码是否已被绑定
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/check/device/binded")
    public ResultData checkDeviceBinded(String qrcode) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.checkDeviceBinded(consumerId, qrcode);
    }

    /**
     * 检查待使用的设备别名是否已经存在，同一用户不能够有相同别名的多台设备
     *
     * @param deviceName
     * @return
     */
    @GetMapping("/check/device/name/binded")
    public ResultData checkDeviceNameExist(String deviceName) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.checkDeviceNameExist(consumerId, deviceName);
    }

    /**
     * 设备初始化，将设备与主控用户进行绑定
     *
     * @param qrcode
     * @param deviceName
     * @param request
     * @return
     */
    @PostMapping("/deviceinit")
    public ResultData deviceInit(String qrcode, String deviceName, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> {
            logService.createUserMachineOperationLog(consumerId, qrcode, "bind", new StringBuffer("User:").append(consumerId).append(" bind device with name ").append(deviceName).toString(), IPUtil.getIP(request), "bind");
        }));
        return machineService.bindConsumerWithQRcode(consumerId, deviceName, qrcode, Ownership.OWNER.getValue());
    }

    /**
     * 获取用户的设备绑定列表（旧）
     * 后续将会逐步用下面的接口完全替换掉
     *
     * @return
     */
    @RequestMapping(value = "/devicelist", method = RequestMethod.GET)
    public ResultData getUserDeviceList() {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前用户登录状态信息失效，请重新登录");
            return result;
        }
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.getMachineListByConsumerId(consumerId);
    }

    /**
     * 获取用户的设备绑定列表(新)
     *
     * @return
     */
    @GetMapping("/list")
    public ResultData machineList() {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前用户登录状态信息失效，请重新登录");
            return result;
        }
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        result = machineService.obtainMachineList(consumerId);
        return result;
    }

    /**
     * 根据qrcode查询当前设备的运行状态信息（旧）
     *
     * @param qrcode
     * @return
     */
    @RequestMapping(value = "/info/probe", method = RequestMethod.GET)
    public ResultData getMachineInfo(String qrcode) {
        return machineService.getMachineStatusByQRcode(qrcode);
    }


    /**
     * 根据qrcode获取设备的运行状态信息(新)
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/running/status")
    public ResultData status(String qrcode) {
        ResultData result = machineService.runningStatus(qrcode);
        return result;
    }

    /**
     * 用户控制设备
     *
     * @param component
     * @param operation
     * @param qrcode
     * @param request
     * @return
     */
    @PostMapping("/operate/{component}/{operation}")
    public ResultData configComponentStatus(@PathVariable("component") String component, @PathVariable("operation") String operation, String qrcode, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserMachineOperationLog(consumerId, qrcode, component, new StringBuffer("User ").append(consumerId).append(" operate ").append(component).append(" set to ").append(operation).toString(), IPUtil.getIP(request), operation)));
        return machineService.chooseComponent(qrcode, component, operation);
    }

    /**
     * 设备解绑
     *
     * @param qrcode
     * @param request
     * @return
     */
    @RequestMapping(value = "/consumer/qrcode/unbind", method = RequestMethod.POST)
    public ResultData unbindConsumerWithQRcode(String qrcode, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> {
            logService.createUserMachineOperationLog(consumerId, qrcode, "unbind",
                    new StringBuffer("User:").append(consumerId).append(" unbind device with qrcode ").append(qrcode).toString(), IPUtil.getIP(request), "unbind");
        }));
        return machineService.unbindConsumerWithQRcode(consumerId, qrcode);
    }

    /**
     * 设备分享
     *
     * @param qrcode
     * @param deviceName
     * @param request
     * @return
     */
    @PostMapping("/device/bind/share")
    public ResultData acquireControlOn(String qrcode, String deviceName, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> {
            logService.createUserMachineOperationLog(consumerId, qrcode, "shareBind", new StringBuffer("User:").append(consumerId).append(" share device binding with ").append(deviceName).toString(), IPUtil.getIP(request), "share");
        }));
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

    //配置风量
    @PostMapping("/config/speed")
    public ResultData configSpeed(String qrcode, int speed, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserMachineOperationLog(consumerId, qrcode, "speed", new StringBuffer("User ").append(consumerId).append(" operate ").append("speed").append(" set to ").append(speed).toString(), IPUtil.getIP(request), String.valueOf(speed))));
        return machineService.configSpeed(qrcode, speed);
    }

    @PostMapping("/config/light")
    public ResultData configLight(String qrcode, int light, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserMachineOperationLog(consumerId, qrcode, "light", new StringBuffer("User ").append(consumerId).append(" operate ").append("light").append(" set to ").append(light).toString(), IPUtil.getIP(request), String.valueOf(light))));
        return machineService.configLight(qrcode, light);
    }

    /**
     * 用户控制设备的目标运行温度
     *
     * @param qrcode
     * @param temp
     * @param request
     * @return
     */
    @PostMapping("/config/temp")
    public ResultData configTemp(String qrcode, int temp, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserMachineOperationLog(consumerId, qrcode, "temp", new StringBuffer("User ").append(consumerId).append(" operate ").append("temp").append(" set to ").append(temp).toString(), IPUtil.getIP(request), String.valueOf(temp))));
        return machineService.configTemp(qrcode, temp);
    }


    @PostMapping("/config/timing")
    public ResultData configTiming(String qrcode, int timing, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserMachineOperationLog(consumerId, qrcode, "timing", new StringBuffer("User ").append(consumerId).append(" operate ").append("timing").append(" set to ").append(timing).toString(), IPUtil.getIP(request), String.valueOf(timing))));
        return machineService.configTiming(qrcode, timing);
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
    public ResultData modifyBindName(String qrcode, String bindName, HttpServletRequest request) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReceptionPool.getLogExecutor().execute(new Thread(() -> {
            logService.createUserMachineOperationLog(consumerId, qrcode, "modifyBindName", new StringBuffer("User:").append(consumerId).append(" modify device bind name to ").append(bindName).toString(), IPUtil.getIP(request), "modifyBind");
        }));
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
        ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserMachineOperationLog(consumerId, qrcode, "timing_machine", new StringBuffer("User ").append(consumerId).append(" operate ").append("timing switch machine").append(" set start to ").append(startHour).append(":").append(startMinute).append(" and end to ").append(endHour).append(":").append(endMinute).toString(), IPUtil.getIP(request), status ? "on" : "off")));
        return machineService.confirmPowerOnoff(qrcode, startHour, startMinute, endHour, endMinute, status);
    }

    //获取当前机器定时开关机状态
    @GetMapping(value = "/probe/onoff/status/by/code")
    public ResultData probeStatus(String qrcode) {
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineService.getRecord(qrcode);
    }

    /**
     * @Description: TODO
     * city和pastlist参数未传
     * @Date 2019/5/31 3:27 PM
     */
    @PostMapping("/share")
    public ResultData share(String qrcode, HttpServletRequest request) {
        ResultData result = new ResultData();
        try {
            String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String name = "果麦新风";
            ResultData response = machineService.findModel(qrcode);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("当前二维码: " + qrcode + "未能找到响应的型号");
                return result;
            }
            JSONObject model = JSON.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
            if (model.containsKey("modelName")) name = name.concat(model.getString("modelName"));
            //获取机器的实时数据
            int pm2_5, temperature = 0, humidity = 0, co2 = 0;
            response = machineService.getMachineStatusByQRcode(qrcode);
            //如果能够获取到实时数据，则绘制实时数据部分
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("当前无法获取室内的数值信息");
                return result;
            }
            JSONObject machine = JSONObject.parseObject(JSON.toJSONString(response.getData()));
            pm2_5 = machine.getInteger("pm2_5");
            if (machine.containsKey("temp")) {
                temperature = machine.getInteger("temp");
                humidity = machine.getInteger("humid");
                if (machine.containsKey("co2")) {
                    co2 = machine.getInteger("co2");
                }
            }
            if (machine.containsKey("temperature")) {
                temperature = machine.getInteger("temperature");
                humidity = machine.getInteger("humidity");
                if (machine.containsKey("co2")) co2 = machine.getInteger("co2");
            }
            String city = "未选择";
            //获取机器的室外地址
            response = machineService.probeCityIdByQRcode(qrcode);
            //如果能够获取到室外配置，则显示室外城市信息
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                BufferedImage bufferedImage = share(path, name, city, pm2_5, temperature, humidity, co2);
                savaAndUpload(bufferedImage);
                result.setDescription("当前无法获取室外的城市信息");
                return result;
            }
            JSONObject location = JSON.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
            String cityId = location.getString("cityId");
            //获取城市名称
            response = locationService.profile(cityId);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                BufferedImage bufferedImage = share(path, name, city, pm2_5, temperature, humidity, co2);
                savaAndUpload(bufferedImage);
                result.setDescription("未找到行政区域编号为: " + cityId + "的城市");
                return result;
            }
            JSONObject json = JSON.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
            if (json.containsKey("cityName")) city = json.getString("cityName");
            //获取当前室外的空气信息，包括AQI指数，主要污染物，PM2.5, PM10, 一氧化碳，二氧化氮，臭氧，二氧化硫
            response = airqualityService.getLatestCityAirQuality(cityId);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                //根据城市获取省份
                response = locationService.probeProvinceIdByCityId(cityId);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    BufferedImage bufferedImage = share(path, name, city, pm2_5, temperature, humidity, co2);
                    savaAndUpload(bufferedImage);
                    result.setDescription("当前无法获取最新的城市空气数据");
                    return result;
                }
                location = JSON.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
                String provinceId = location.getString("provinceId");
                response = airqualityService.getLatestCityAirQuality(provinceId);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    BufferedImage bufferedImage = share(path, name, city, pm2_5, temperature, humidity, co2);
                    savaAndUpload(bufferedImage);
                    result.setResponseCode(ResponseCode.RESPONSE_OK);
                    result.setDescription("当前无法获取最新的城市PM2.5信息");
                    return result;
                }
            }
            JSONObject outdoor = JSONArray.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
            int outdoorPM2_5 = outdoor.containsKey("pm2_5") ? outdoor.getInteger("pm2_5") : 0;
            int aqi = outdoor.containsKey("aqi") ? outdoor.getInteger("aqi") : 0;
            int pm10 = outdoor.containsKey("pm10") ? outdoor.getInteger("pm10") : 0;
            String primary = outdoor.containsKey("primePollution") ? outdoor.getString("primePollution") : "无";
            double co = outdoor.containsKey("co") ? outdoor.getDouble("co") : 0;
            double no2 = outdoor.containsKey("no2") ? outdoor.getDouble("no2") : 0;
            double o3 = outdoor.containsKey("o3") ? outdoor.getDouble("o3") : 0;
            double so2 = outdoor.containsKey("so2") ? outdoor.getDouble("so2") : 0;
            BufferedImage bufferedImage = share(path, "果麦新风", city, pm2_5, temperature, humidity, co2, outdoorPM2_5, aqi, primary, pm10, co, no2, o3, so2);
            ReceptionPool.getLogExecutor().execute(new Thread(() -> logService.createUserMachineOperationLog(consumerId, qrcode, "share", new StringBuffer("User:").append(consumerId).append(" share machine image with qrcode ").append(qrcode).toString(), IPUtil.getIP(request), "image")));
            savaAndUpload(bufferedImage);
            //获取设备的连续7天的空气数据
            //response = machineService.fetchMachineDailyPm2_5(qrcode);
        } catch (Exception e) {
            logger.error("[Error] Share image error: " + e.getMessage());
        }
        return result;
    }

    private BufferedImage share(String path, String name, String city, int pm2_5, int temperature, int humidity, int co2) {
        return ImageShareUtil.share(path, name, city, pm2_5, temperature, humidity, co2);
    }

    private BufferedImage share(String path, String name, String city, int pm2_5, int temperature, int humidity, int co2, int[] pastlist) {
        return ImageShareUtil.share(path, name, city, pm2_5, temperature, humidity, co2, pastlist);
    }

    private BufferedImage share(String path, String name, String city, int pm2_5, int temperature, int humidity, int co2, int outPM2_5, int aqi, String primary, int pm10, double co, double no2, double o3, double so2) {
        return ImageShareUtil.share(path, name, city, pm2_5, temperature, humidity, co2, outPM2_5, aqi, primary, pm10, co, no2, o3, so2);
    }

    private BufferedImage share(String path, String name, String city, int pm2_5, int temperature, int humidity, int co2, int outPM2_5, int aqi, String primary, int pm10, double co, double no2, double o3, double so2, int[] pastlist) {
        return ImageShareUtil.share(path, name, city, pm2_5, temperature, humidity, co2, outPM2_5, aqi, primary, pm10, co, no2, o3, so2, pastlist);
    }

    private void savaAndUpload(BufferedImage bufferedImage) {
        //把处理完的图片上传到服务器
        String fileName = String.format("%s/%s.jpg", fileSavePath, IDGenerator.generate("pic"));
        ReceptionPool.getPicExecutor().execute(() -> {
            try {
                File file = new File(fileName);
                ImageIO.write(bufferedImage, "jpg", file);
                FileInputStream fileInputStream = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                ResultData result = authConsumerService.profile(consumerId);
                if (result.getResponseCode() != ResponseCode.RESPONSE_OK)
                    return;
                String openId = (String) ((LinkedHashMap) result.getData()).get("wechat");
                if (StringUtils.isEmpty(openId))
                    return;
                wechatFormService.uploadAndReply(openId, multipartFile);
                file.delete();
            } catch (Exception e) {
                e.getMessage();
            }
        });
    }
}
