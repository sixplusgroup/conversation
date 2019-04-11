package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.model.mqtt.Firmware;
import finley.gmair.model.mqtt.MachineType;
import finley.gmair.service.FirmwareService;
import finley.gmair.service.MachineService;
import finley.gmair.service.MachineTypeService;
import finley.gmair.util.PushCallback;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * achieve server to publish message, all the methods follow the steps as bellow
 * step：
 *    first：verify uid，action and qos
 *    second: produce topic with uid and action
 *    third: use json to build payload
 *    finally: publish(topic, message，qos)
 * */
@RestController
@RequestMapping("/message")
public class MessageController {
    private final static String host = "tcp://116.62.233.170:61613";
    private final static String clientId = "core-server";

    @Autowired
    private FirmwareService firmwareService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private MachineTypeService machineTypeService;

    /*
    * 服务端上报控制指令
    * 此条指令qos为2
    * */
    @PostMapping(value = "/com/config/cmd")
    public ResultData configPower(String uid, String action, int qos,
                                  int power, int level, int ptc, int mode,
                                  int newwind, int backwind, int childlock, int led) {
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
        if (!StringUtils.isEmpty(level)) {
            json.put("level", level);
        }
        if (!StringUtils.isEmpty(ptc)) {
            json.put("ptc", ptc);
        }
        if (!StringUtils.isEmpty(mode)) {
            json.put("mode", mode);
        }
        if (!StringUtils.isEmpty(newwind)) {
            json.put("newwind", newwind);
        }
        if (!StringUtils.isEmpty(backwind)) {
            json.put("backwind", backwind);
        }
        if (!StringUtils.isEmpty(childlock)) {
            json.put("childlock", childlock);
        }
        if (!StringUtils.isEmpty(led)) {
            json.put("led", led);
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
        json.put("time", System.currentTimeMillis());
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
    * */
    @PostMapping(value = "/com/update")
    public ResultData updateFirmware(String uid, String action,
                                     int qos, String newVersion, int force) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
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
        Firmware firmware = ((List<Firmware>)response.getData()).get(0);
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        json.put("newver", newVersion);
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
     * @param bottom //表示底层剩余寿命
     * @param middle //表示中层剩余寿命
     * @param top    //表示顶层剩余寿命
     * 三者可以选填
     * 此条指令qos为2
     * */
    @PostMapping(value = "/com/set/surplus")
    public ResultData setSurplus(String uid, String action, int qos,
                                 int bottom, int middle, int top) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)
                || StringUtils.isEmpty(qos)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String topic = produceTopic(uid, action);
        JSONObject json = new JSONObject();
        json.put("1", bottom);
        json.put("2", middle);
        json.put("3", top);
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
     * */
    @PostMapping(value = "/com/set/rfid")
    public ResultData setRFID(String uid, String action, int qos, int enabled) {
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
    * */
    @PostMapping(value = "/com/set/screen")
    public ResultData setScreen(String uid, String action, int qos, int valid) {
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
     *  1. surplus(滤芯剩余寿命)
     *  2. status(机器运行状态)
     *  3. sensor(传感器数据)
     * 此条指令qos为1
     * */
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
        JSONObject json = new JSONObject();
        List<String> list = new ArrayList<>();
        list.add("surplus"); //滤芯剩余寿命
        list.add("status");  //运行状态
        list.add("sensor");  //传感器数据
        json.put("item", list);
        try {
            publish(topic, json, qos);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Demand report message publishing error with: " + e.getMessage());
        }
        return result;
    }

    /**
     * 根据uid,action生成相应的topic
     * */
    private String produceTopic(String uid, String action) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(action)) {
            return "";
        }
        //根据machineId获取board version
        ResultData response = machineService.getBoardVersion(uid);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return "";
        }
        BoardVersion version = ((List<BoardVersion>) response.getData()).get(0);

        //根据board version获取machine type相关内容
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("boardVersion", version.getVersion());
        response = machineTypeService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return "";
        }
        MachineType type = ((List<MachineType>) response.getData()).get(0);
        return sb.append("/client").append(type.getDeviceName()).append("/").append(uid)
                .append("/").append(action).toString();
    }

    private MqttClient connect() throws MqttException{
        MqttClient client = new MqttClient(host, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setKeepAliveInterval(20);
        options.setConnectionTimeout(10);
        client.setCallback(new PushCallback());
        client.connect(options);
        return client;
    }

    private void publish(String topic, JSONObject object, int qos) throws MqttException,
            MqttPersistenceException, InterruptedException {
        MqttClient client = connect();
        MqttMessage message = getMessage(object, qos);
        client.publish(topic, message);
    }

    /**
     * 根据json数据build相应的MqttMessage
     * */
    private MqttMessage getMessage(JSONObject object, int qos) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(false);
        message.setPayload(object.toString().getBytes());
        return message;
    }
}
