package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import finley.gmair.agent.CoreService;
import finley.gmair.agent.MachineService;

import finley.gmair.model.mode.MachineMode;
import finley.gmair.model.mode.ModeType;
import finley.gmair.service.MachineModeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/mode")
public class MachineModeController {

    @Autowired
    private MachineModeService machineModeService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private CoreService coreService;

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

        //第一步,从表中找出开着节能模式的机器,并转换成machineIdList
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

        //第二步,凭借machineIdList向 machine模块 获取在线机器的 machine status(存储在缓存中).
        response = machineService.fetchLastHourStatusListByMachineIdList(json);
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        List<Object> statusList =  (List<Object>)response.getData();

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        //第三步,对第一代机器做处理.
        List<LinkedHashMap<String,Object>> machineV1StatusList = (List<LinkedHashMap<String, Object>>)statusList.get(0);
        for (LinkedHashMap<String,Object> lhm:machineV1StatusList) {
            String uid = (String)lhm.get("uid");
            int pm2_5 = (int)lhm.get("pm2_5");
            int co2 = (int)lhm.get("co2");
            if(pm2_5 > 25 || co2 >1000){
                coreService.configPower(uid,1,1);
                coreService.configSpeed(uid,60,1);
                if((month==10&&day>=15)||month>=11||month<=2||(month==3&&day<=15))
                    coreService.configHeat(uid,2,1);
            }
            else if(pm2_5 <= 25 && co2 <= 1000){
                coreService.configPower(uid,0,1);
            }
        }

        //第四步,对第二代机器做处理.
        List<LinkedHashMap<String,Object>> machineV2StatusList = (List<LinkedHashMap<String, Object>>)statusList.get(1);
        for (LinkedHashMap<String,Object> lhm:machineV2StatusList) {
            String uid = (String)lhm.get("uid");
            int pm2_5 = (int)lhm.get("pm2_5");
            int co2 = (int)lhm.get("co2");
            if(pm2_5 > 25 || co2 >1000){
                coreService.configPower(uid,1,2);
                coreService.configSpeed(uid,60,2);
                if((month==10&&day>=15)||month>=11||month<=2||(month==3&&day<=15))
                    coreService.configHeat(uid,2,2);
            }
            else if(pm2_5 <= 25 && co2 <= 1000){
                coreService.configPower(uid,0,2);
            }
        }

        return result;
    }

}
