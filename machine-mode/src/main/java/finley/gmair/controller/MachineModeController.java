package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.netflix.discovery.converters.Auto;
import finley.gmair.agent.MachineService;
import finley.gmair.model.mode.MachineMode;
import finley.gmair.model.mode.ModeType;
import finley.gmair.service.MachineModeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachinePm2_5Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/mode")
public class MachineModeController {

    @Autowired
    private MachineModeService machineModeService;

    @Autowired
    private MachineService machineService;

    @PostMapping("/create/or/update")
    public ResultData createOrUpdateMachineMode(String machineId, int type, boolean modeStatus) {

        //check empty
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId");
            return result;
        }

        //将输入的 int 转换成 Enum
        ModeType modeType;
        String modeName;
        switch (type) {
            case 0:
                modeType = ModeType.POWER_SAVING;
                modeName = "power-saving";
                break;
            case 1:
                modeType = ModeType.NORMAL;
                modeName = "normal";
                break;
            case 2:
                modeType = ModeType.HIGH;
                modeName = "high";
                break;
            default:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("no such mode type");
                return result;
        }

        //根据数据库中的 machineId-modeType是否存在,来判断插入或更新
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId",machineId);
        condition.put("modeType",modeType.getValue());
        condition.put("blockFlag",false);
        ResultData response = machineModeService.fetch(condition);

        //不存在的 machineId-modeType,则创建一条
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            response = machineModeService.create(new MachineMode(machineId, modeType, modeName, modeStatus));
            switch (response.getResponseCode()) {
                case RESPONSE_ERROR:
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("fail to create machine mode");
                    return result;
                case RESPONSE_OK:
                    result.setResponseCode(ResponseCode.RESPONSE_OK);
                    result.setDescription("success to create machine mode");
                    return result;
            }
        }

        //存在的 machineId-modeType,则更新这条
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            condition.clear();
            condition.put("modeStatus",modeStatus);
            condition.put("modeId",((List<MachineMode>)response.getData()).get(0).getModeId());
            response = machineModeService.update(condition);
            switch (response.getResponseCode()) {
                case RESPONSE_ERROR:
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("fail to update machine mode");
                    return result;
                case RESPONSE_OK:
                    result.setResponseCode(ResponseCode.RESPONSE_OK);
                    result.setDescription("success to update machine mode");
                    return result;
            }
        }

        return result;
    }

    @PostMapping("/hourly/power-saving")
    public ResultData handleHourlyPowerSaving(){
        ResultData result = new ResultData();

        //第一步,从表中选出  开着节能模式的机器,并转换成JSON字符串
        Map<String, Object> condition = new HashMap<>();
        condition.put("modeType",ModeType.POWER_SAVING.getValue());
        condition.put("modeStatus",1);
        condition.put("blockFlag",false);
        ResultData response = machineModeService.fetch(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch data");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find any data");
            return result;
        }

        List<MachineMode> machineModeList = (List<MachineMode>)response.getData();
        JSONArray jsonArray = new JSONArray();
        for (MachineMode machineMode: machineModeList) {
            jsonArray.add(machineMode.getMachineId());
        }
        String json = jsonArray.toJSONString();

        //从machine模块获取在线的前一个小时有pm2.5记录的 machineId-pm2.5记录
        response = machineService.fetchLastHourPM25ListByMachineIdList(json);
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        List<MachinePm2_5Vo> pm25List = (List<MachinePm2_5Vo>) response.getData();
        return result;
    }

}
