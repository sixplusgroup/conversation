package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.machine.MachineFilterInfoQuery;
import finley.gmair.service.CoreService;
import finley.gmair.service.MachineService;
import finley.gmair.service.MqttLoggerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.FilterUpdByFormulaConfig;
import finley.gmair.vo.machine.FilterUpdByMQTTConfig;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * This method is responsible to fetch response from machine-agent
 */
@CrossOrigin
@RestController
@RequestMapping("/management/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private CoreService coreService;

    @Autowired
    private MqttLoggerService mqttLoggerService;


    @GetMapping("/model/list")
    public ResultData modelList() {
        return machineService.modelList();
    }

    @GetMapping("/batch/list")
    public ResultData batchList(String modelId) {
        return machineService.batchList(modelId);
    }

    @PostMapping("/qrcode/check")
    public ResultData checkQrcode(String candidate) {
        return machineService.check(candidate);
    }

    @GetMapping("/core/repo/{machineId}/online")
    public ResultData online(@PathVariable("machineId") String machineId) {
        return coreService.isOnline(machineId);
    }

    @PostMapping("/config/control/option")
    public ResultData configControlOption(String optionName, String optionComponent, String modelId, String actions) {
        ResultData result = new ResultData();
        JSONArray actionList = JSONArray.parseArray(actions);
        for (int i = 0; i < actionList.size(); i++) {
            JSONObject action = actionList.getJSONObject(i);
            ResultData response = machineService.setControlOption(optionName, optionComponent, modelId,
                    action.getString("actionName"),
                    action.getString("actionOperator"),
                    action.getString("commandValue"));
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("some error happen when create control option");
                return result;
            }
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to create control option with its actions");
        return result;
    }

    @GetMapping("/probe/24hour/outPm25")
    public ResultData probeLast24HourOutPm25ByMachineId(String qrcode) {
        return machineService.probeLast24HourOutPm25ByQrcode(qrcode);
    }

    @GetMapping("/probe/24hour/indoorPm25")
    public ResultData probeLast24HourIndoorPm25ByMachineId(String qrcode) {
        return machineService.fetchMachineHourlyPm2_5(qrcode);
    }

    @GetMapping("/consumer/owner/machine/list")
    public ResultData getOwnerMachineList(int curPage, int pageSize, String qrcode, String phone, String createTimeGTE, String createTimeLTE, String online, String overCount, String overCountGTE, String overCountLTE) {
        return machineService.getOwnerMachineList(curPage, pageSize, qrcode, phone, createTimeGTE, createTimeLTE, online,overCount, overCountGTE, overCountLTE);
    }

    @PostMapping("/qrcode/status")
    public ResultData findStatusByQRcode(String qrcode) {
        return machineService.checkQRcodeExist(qrcode);
    }

    @RequestMapping(value = "/model/component/probe", method = RequestMethod.GET)
    public ResultData fetchModelEnabledComponent(String modelId, String componentName) {
        return machineService.fetchModelEnabledComponent(modelId, componentName);
    }

    @PostMapping("/config/screen")
    public ResultData configScreen(String qrcode,int screen) {
        return machineService.configScreen(qrcode,screen);
    }

    /**
     * 根据qrcode查绑定列表
     * @param search
     * @return
     */
    @GetMapping("/qrcode/bind/list")
    public ResultData bindList(String search){
        return machineService.qrcodeBindList(search);
    }

    /**
     * 设备解绑
     * @param qrcode
     * @param consumerId
     * @return
     */
    @RequestMapping(value = "/qrcode/unbind", method = RequestMethod.POST)
    public ResultData unbindConsumerWithQRcode(String qrcode,String consumerId) {
        return machineService.unbindConsumerWithQRcode(consumerId, qrcode);
    }

    /**
     * 根据qrcode查mqtt-logger
     * @param qrcode
     * @return
     */
    @GetMapping("/mqtt/logger/list")
    public ResultData getMqttLogger(String qrcode){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode");
            return result;
        }
        ResultData response = machineService.qrcodeGetMachineId(qrcode);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("根据二维码查找机器MAC失败");
            return result;
        }
        MachineQrcodeBindVo machineQrcodeBindVo = JSONArray.parseArray(JSONObject.toJSONString(response.getData()),MachineQrcodeBindVo.class).get(0);
        System.out.println(machineQrcodeBindVo);
        String machineId = machineQrcodeBindVo.getMachineId();
        response = mqttLoggerService.list(machineId);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("根据机器MAC查找log失败");
            return result;
        }
        result.setResponseCode(response.getResponseCode());
        result.setData(response.getData());
        return result;
    }

    /**
     * 查询设备滤网信息
     * @param query 查询条件
     * @return 结果集合
     */
    @PostMapping("/filter/info/query")
    public ResultData queryMachineFilterInfo(@RequestBody MachineFilterInfoQuery query) {
        return machineService.queryMachineFilterInfo(query);
    }

    /**
     * 查询设备型号名，如GM420
     * @return 设备型号名集合
     */
    @GetMapping("/filter/info/model/name")
    public ResultData queryMachineModelName() {
        return machineService.queryMachineModelName();
    }

    /**
     * 查询设备型号code，即子型号，如GM420-42A中的42A即为所求
     * @param modelName 设备型号名，如GM420
     * @return 设备型号code，如42A
     */
    @GetMapping("/filter/info/model/code")
    public ResultData queryMachineModelCode(String modelName) {
        return machineService.queryMachineModelCode(modelName);
    }

    /**
     * 获取所有具有高效滤网且是通过MQTT获取Remain来更新滤网状态的设备的滤网提醒配置
     * @return {@link FilterUpdByMQTTConfig}数组
     */
    @GetMapping("/filter/info/config/updatedByMQTT")
    public ResultData queryConfigUpdatedByMQTT() {
        return machineService.queryConfigUpdatedByMQTT();
    }

    /**
     * 更新通过MQTT获取Remain来更新滤网状态的设备的滤网提醒配置
     * @param config 更新值对象
     * @return 是否成功
     */
    @PostMapping("/filter/info/update/config/updatedByMQTT")
    public ResultData updateConfigUpdatedByMQTT(@RequestBody FilterUpdByMQTTConfig config) {
        return machineService.updateConfigUpdatedByMQTT(config);
    }

    /**
     * 获取所有具有高效滤网且是通过公式来更新滤网状态的设备的滤网提醒配置
     * @return {@link FilterUpdByFormulaConfig}数组
     */
    @GetMapping("/filter/info/config/updatedByFormula")
    public ResultData queryConfigUpdatedByFormula() {
        return machineService.queryConfigUpdatedByFormula();
    }

    /**
     * 更新通过公式来更新滤网状态的设备的滤网提醒配置
     * @param config 更新值对象
     * @return 是否成功
     */
    @PostMapping("/filter/info/update/config/updatedByFormula")
    public ResultData updateConfigUpdatedByFormula(@RequestBody FilterUpdByFormulaConfig config) {
        return machineService.updateConfigUpdatedByFormula(config);
    }

}
