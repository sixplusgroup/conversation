package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.mqtt.Firmware;
import finley.gmair.service.FirmwareService;
import finley.gmair.util.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.*;

/**
 * achieve server to publish message, all the methods follow the steps as bellow
 * step：
 * first：verify uid，action and qos
 * second: produce topic with uid and action
 * third: use json to build payload
 * finally: publish(topic, message，qos)
 */
@RestController
@RequestMapping("/mqtt/message")
public class MessageController {

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private FirmwareService firmwareService;

    /*
     * 服务端上报控制指令
     * led 表示屏显开关，light进行屏幕亮度调节
     * 此条指令qos为2
     * action = cmd
     * */
    @PostMapping(value = "/com/config/cmd")
    public ResultData configPower(String uid, String action, int qos, Integer power, Integer speed, Integer heat, Integer mode, Integer childlock, Integer led, Integer light) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        //根据uid生成对应的topic
        String topic = produceTopic(uid, action);

        //根据字段是否为空，向json push数据
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(power)) {
            json.put("power", power);
        }
        if (!StringUtils.isEmpty(speed)) {
            json.put("speed", speed);
        }
        if (!StringUtils.isEmpty(heat)) {
            json.put("heat", heat);
        }
        if (!StringUtils.isEmpty(mode)) {
            json.put("mode", mode);
        }
        if (!StringUtils.isEmpty(childlock)) {
            json.put("childlock", childlock);
        }
        if (!StringUtils.isEmpty(led)) {
            json.put("led", led);
        }
        if (!StringUtils.isEmpty(light)) {
            json.put("light", light);
        }
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Command message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /*
     * 服务端设置时间
     * 此条指令qos为2
     * action = set_time
     * */
    @PostMapping(value = "/com/config/time")
    public ResultData configTime(String uid, String action, int qos) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        json.put("time", System.currentTimeMillis() / 1000);
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Set time message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /*
     * 服务端固件更新
     * force为1，代表强制更新，force为0，代表非强制更新
     * 此条指令qos为2
     * action = update
     * */
    @PostMapping(value = "/com/update")
    public ResultData updateFirmware(String uid, String action, int qos, String newVersion, Integer force) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action) || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("firmwareVersion", newVersion);
        ResultData response = firmwareService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get firmware information");
            return result;
        }
        Firmware firmware = ((List<Firmware>) response.getData()).get(0);
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        json.put("newversion", newVersion);
        json.put("link", firmware.getFirmwareLink());
        json.put("force", force);
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Firmware update message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /**
     * 服务端重置滤芯寿命
     *
     * @param remain //表示滤芯剩余寿命
     *               此条指令qos为2
     *               action = set_surplus
     */
    @PostMapping(value = "/com/set/surplus")
    public ResultData setSurplus(String uid, String action, int qos, Integer remain) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        json.put("remain", remain);
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Reset surplus message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /**
     * 服务端rfid使能
     * enabled为0时，设备不读取RFID，enabled为1时，设备读取RFID
     * 此条指令qos为2
     * action = setrfid
     */
    @PostMapping(value = "/com/set/rfid")
    public ResultData setRFID(String uid, String action, int qos, Integer enabled) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        json.put("enabled", enabled);
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Set rfid message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /*
     * 服务端强制滤网有效无效
     * 1.当APP推送1时，不管设备的滤网是什么状态，强制写入1；
     * 2.当APP推送0X88时，不管设备的滤网还有多少寿命，一律作废
     * 此条指令qos为2
     * action = invalid
     * */
    @PostMapping(value = "/com/set/screen")
    public ResultData setScreen(String uid, String action, int qos, Integer valid) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        json.put("invalid", valid);
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Set screen message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /**
     * 服务端要求即刻上报数据命令
     * 主要有三种数据：
     * 1. surplus(滤芯剩余寿命)
     * 2. status(机器运行状态)
     * 3. sensor(传感器数据)
     * 此条指令qos为1
     * action = report
     */
    @PostMapping(value = "/com/demand/report")
    public ResultData demandReport(String uid, String action, int qos) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String topic = produceTopic(uid, action);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("surplus"); //滤芯剩余寿命
        jsonArray.add("status");  //运行状态
        jsonArray.add("sensor");  //传感器数据
        JSONObject json = new JSONObject();
        json.put("item", jsonArray);
        ;
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Demand report message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /**
     * 根据服务端需要，获取指定传感器数据
     * 与上面不同，此接口参数为uid, 可变的component(pm2.5a, pm2.5b...)，以及qos
     * 需要根据component构造相应的action
     */
    @PostMapping(value = "/com/require/component")
    public ResultData demandSingle(String uid, String component, int qos) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(component)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        //根据指定component构造相应的action
        String action = new StringBuffer("component/").append(component).toString();
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Require single sensor message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /**
     * 根据uid,action生成相应的topic
     */
    private String produceTopic(String uid, String action) {
        StringBuffer sb = new StringBuffer();
        return sb.append("/client/FA/").append(uid).append("/").append(action).toString();
    }

    /**
     * 创建mqtt client建立连接
     */
    private MqttClient connect() throws MqttException {
        MqttClient client = new MqttClient(mqttProperties.getOutbound().getUrls(), mqttProperties.getOutbound().getClientId(), new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setKeepAliveInterval(30);
        options.setConnectionTimeout(10);
        client.setCallback(new PushCallback());
        client.connect(options);
        return client;
    }

    /**
     * 利用已建立连接的client完成消息publish
     */
    private void publish(String topic, JSONObject object, int qos) throws MqttException {
        MqttClient client = connect();
        MqttMessage message = getMessage(object, qos);
        client.publish(topic, message);
    }

    /**
     * 根据json数据build相应的MqttMessage
     */
    private MqttMessage getMessage(JSONObject object, int qos) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(false);
        message.setPayload(object.toString().getBytes());
        return message;
    }
}
