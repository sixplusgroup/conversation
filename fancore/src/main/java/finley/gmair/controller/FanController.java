package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.MqttService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: FanController
 * @Description: TODO
 * @Author fan
 * @Date 2019/11/14 10:03 PM
 */
@RestController
@CrossOrigin
@RequestMapping("/core/com")
public class FanController {
    private Logger logger = LoggerFactory.getLogger(FanController.class);

    @Autowired
    private MqttService mqttService;

    @GetMapping("/profile")
    public ResultData profile(String mac) {
        ResultData result = new ResultData();
        JSONObject data = mqttService.obtain(mac);
        result.setData(data);
        if (data == null) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        return result;
    }

    @PostMapping("/setting/query")
    public ResultData setting(String model, String mac) {
        ResultData result = new ResultData();
        String topic = "/GM/FAN/".concat(model).concat("/").concat(mac).concat("/INFO/QUERY");
        JSONObject json = init(mac);
        mqttService.publish(topic, json);
        return result;
    }

    @PostMapping("/setting/config")
    public ResultData settingconfig(String model, String mac, String ip, Integer port) {
        ResultData result = new ResultData();
        String topic = "/GM/FAN/".concat(model).concat("/").concat(mac).concat("/INFO/CONFIG");
        JSONObject json = init(mac, true);
        if (!StringUtils.isEmpty(ip)) {
            json.put("serverip", ip);
        }
        if (!StringUtils.isEmpty(port)) {
            json.put("serverport", port);
        }
        mqttService.publish(topic, json);
        return result;
    }

    @PostMapping("/status/query")
    public ResultData info(String model, String mac) {
        ResultData result = new ResultData();
        String topic = "/GM/FAN/".concat(model).concat("/").concat(mac).concat("/STATUS/QUERY");
        JSONObject json = init(mac);
        mqttService.publish(topic, json);
        return result;
    }

    @PostMapping("/status/config")
    public ResultData infoconfig(String model, String mac, Integer power, Integer speed, Integer mode, Integer sweep, Integer heat, Integer countdown, Integer targettemp, Integer mutemode, Integer uv) {
        ResultData result = new ResultData();
        String topic = "/GM/FAN/".concat(model).concat("/").concat(mac).concat("/STATUS/CONFIG");
        JSONObject json = init(mac, true);
        if (!StringUtils.isEmpty(power)) {
            json.put("power", power);
        }
        if (!StringUtils.isEmpty(speed)) {
            json.put("speed", speed);
        }
        if (!StringUtils.isEmpty(mode)) {
            json.put("mode", mode);
        }
        if (!StringUtils.isEmpty(sweep)) {
            json.put("sweep", sweep);
        }
        if (!StringUtils.isEmpty(heat)) {
            json.put("heat", heat);
        }
        if (!StringUtils.isEmpty(countdown)) {
            json.put("countdown", countdown);
        }
        if (!StringUtils.isEmpty(targettemp)) {
            json.put("targettemp", targettemp);
        }
        if (!StringUtils.isEmpty(mutemode)) {
            json.put("mutemode", mutemode);
        }
        if (!StringUtils.isEmpty(uv)) {
            json.put("uv", uv);
        }
        mqttService.publish(topic, json);
        logger.info("[action] topic: " + topic + ", payload: " + json);
        return result;
    }

    @PostMapping("/upgrade")
    public ResultData upgrade(String model, String mac, String firmware, String source) {
        ResultData result = new ResultData();
        String topic = "/GM/FAN/".concat(model).concat("/").concat(mac).concat("/UPGRADE");
        JSONObject json = init(mac, true);
        json.put("firmware", firmware);
        json.put("source", source);
        mqttService.publish(topic, json);
        return result;
    }

    private JSONObject init(String mac) {
        JSONObject json = new JSONObject();
        json.put("mac", mac);
        json.put("timestamp", System.currentTimeMillis());
        return json;
    }

    private JSONObject init(String mac, boolean config) {
        JSONObject json = new JSONObject();
        json.put("mac", mac);
        json.put("timestamp", System.currentTimeMillis());
        if (config) {
            json.put("actioncode", IDGenerator.generate("ACD"));
        }
        return json;
    }
}
