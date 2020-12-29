package finley.gmair.controller;

import finley.gmair.dto.MachineEfficientFilterInfo;
import finley.gmair.dto.MachinePrimaryFilterInfo;
import finley.gmair.form.machine.MachineFilterInfoQuery;
import finley.gmair.model.machine.MachineFilterType;
import finley.gmair.service.MachineEfficientFilterConfigService;
import finley.gmair.service.MachineFilterInfoService;
import finley.gmair.service.ModelEfficientConfigService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.FilterUpdByFormulaConfig;
import finley.gmair.vo.machine.FilterUpdByMQTTConfig;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/8 11:47
 * @description: 用于提供设备滤网状况概览功能接口
 */

@RestController
@RequestMapping("/machine/filter/info")
public class MachineFilterInfoController {

    @Resource
    private MachineFilterInfoService machineFilterInfoService;

    @Resource
    private ModelEfficientConfigService modelEfficientConfigService;

    @Resource
    private MachineEfficientFilterConfigService machineEfficientFilterConfigService;

    /**
     * 获取设备滤网信息
     * @param query 查询条件
     * @return {@link MachinePrimaryFilterInfo} or {@link MachineEfficientFilterInfo}数组
     */
    @PostMapping("/query")
    public ResultData queryMachineFilterInfo(@RequestBody MachineFilterInfoQuery query) {
        ResultData res = new ResultData();
        Map<String, Object> resData = new HashMap<>();

        if (!checkParams(query)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("params error");
        }
        else {
            MachineFilterType type = query.getMachineFilterType();
            if (type.equals(MachineFilterType.PRIMARY)) {
                resData.put("size", machineFilterInfoService.queryMachinePrimaryFilterInfoSize(query));
                resData.put("list", machineFilterInfoService.queryMachinePrimaryFilterInfo(query));
                res.setData(resData);
            }
            else if (type.equals(MachineFilterType.EFFICIENT)) {
                resData.put("size", machineFilterInfoService.queryMachineEfficientFilterInfoSize(query));
                resData.put("list", machineFilterInfoService.queryMachineEfficientFilterInfo(query));
                res.setData(resData);
            }
        }
        return res;
    }

    @GetMapping("/model/name")
    public ResultData queryMachineModelName() {
        ResultData res = new ResultData();
        List<String> store = machineFilterInfoService.queryMachineModelName();
        res.setData(store);
        return res;
    }

    @GetMapping("/model/code")
    public ResultData queryMachineModelCode(@RequestParam String modelName) {
        ResultData res = new ResultData();
        List<String> store = machineFilterInfoService.queryMachineModelCode(modelName);
        res.setData(store);
        return res;
    }

    /**
     * 获取所有具有高效滤网且是通过MQTT获取Remain来更新滤网状态的设备的滤网提醒配置
     * @return {@link FilterUpdByMQTTConfig}数组
     */
    @GetMapping("/config/updatedByMQTT")
    public ResultData queryConfigUpdatedByMQTT() {
        ResultData res = new ResultData();
        List<FilterUpdByMQTTConfig> store = modelEfficientConfigService.fetchHasFirstRemind();
        res.setData(store);
        return res;
    }

    /**
     * 更新通过MQTT获取Remain来更新滤网状态的设备的滤网提醒配置
     * @param config 更新值对象
     * @return 是否成功
     */
    @PostMapping("/update/config/updatedByMQTT")
    public ResultData updateConfigUpdatedByMQTT(@RequestBody FilterUpdByMQTTConfig config) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", config.getModelId());
        condition.put("firstRemind", config.getFirstRemind());
        condition.put("secondRemind", config.getSecondRemind());
        condition.put("resetHour", config.getResetHour());
        return modelEfficientConfigService.updateByModelId(condition);
    }

    /**
     * 获取所有具有高效滤网且是通过公式来更新滤网状态的设备的滤网提醒配置
     * @return {@link FilterUpdByFormulaConfig}数组
     */
    @GetMapping("/config/updatedByFormula")
    public ResultData queryConfigUpdatedByFormula() {
        ResultData res = new ResultData();
        List<FilterUpdByFormulaConfig> store = machineEfficientFilterConfigService.fetchConfigList();
        res.setData(store);
        return res;
    }

    /**
     * 更新通过公式来更新滤网状态的设备的滤网提醒配置
     * @param config 更新值对象
     * @return 是否成功
     */
    @PostMapping("/update/config/updatedByFormula")
    public ResultData updateConfigUpdatedByFormula(@RequestBody FilterUpdByFormulaConfig config) {
        ResultData res = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("totalTime", config.getTotalTime());
        condition.put("paramOne", config.getParamOne());
        condition.put("paramTwo", config.getParamTwo());
        condition.put("firstRemindThreshold", config.getFirstRemindThreshold());
        condition.put("secondRemindThreshold", config.getSecondRemindThreshold());
        condition.put("modelId", config.getModelId());
        condition.put("tRun", config.isTRun());
        boolean updRes = machineEfficientFilterConfigService.update(condition);
        if (!updRes) res.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return res;
    }

    private boolean checkParams(MachineFilterInfoQuery query) {
        if (query != null) {
            MachineFilterType type = query.getMachineFilterType();
            if (type == null) return false;
            if (type.equals(MachineFilterType.PRIMARY)
                    || type.equals(MachineFilterType.EFFICIENT)) {
                return query.getPageIndex() >= 1 && query.getPageSize() >= 1;
            }
        }
        return false;
    }

}
