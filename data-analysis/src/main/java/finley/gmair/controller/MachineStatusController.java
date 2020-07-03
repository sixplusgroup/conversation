package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import finley.gmair.utils.DAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/data/analysis/machine/status")
public class MachineStatusController {

    @Autowired
    private MachineStatusService machineStatusService;

    @Autowired
    private IndoorPm25Service indoorPm25Service;

    @Autowired
    private VolumeService volumeService;

    @Autowired
    private Co2Service co2Service;

    @Autowired
    private HumidService humidService;

    @Autowired
    private TempService tempService;

    @Autowired
    private PowerService powerService;

    @Autowired
    private ModeService modeService;

    @Autowired
    private HeatService heatService;

    @Autowired
    private MachineAgent machineAgent;

    @PostMapping("/schedule/statistical/hourly")
    public ResultData statisticalDataHourly() {
        ResultData response = machineStatusService.handleHourlyStatisticalData();
        return response;
    }

    @PostMapping("/schedule/statistical/daily")
    public ResultData statisticalDataDaily() {
        ResultData response = machineStatusService.handleDailyStatisticalData();
        return response;
    }

    //根据qrcode查询machineId
    private String getMachineIdByQRcode(String qrcode) {
        ResultData response = machineAgent.findMachineIdByCodeValueFacetoConsumer(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
            return "";
        return ((ArrayList<LinkedHashMap<String, String>>) response.getData()).get(0).get("machineId");
    }

    //格式化daily 和 hourly的数据
    private ResultData getFormatedData(String machineId, JSONArray dataList, int listLength, int timeType) {
        Timestamp cur;
        Timestamp last;
        long timeInteval = 0;
        //0代表格式化daily data
        if (timeType == 0) {
            timeInteval = DAUtils.DAY_INTERVAL_IN_SECONDS * 1000;
            cur = TimeUtil.getTodayZeroTimestamp();
            last = new Timestamp(cur.getTime() - (listLength - 1) * timeInteval);
        }
        //其他代表格式化hourly data
        else {
            timeInteval = DAUtils.HOUR_INTERVAL_IN_SECONDS * 1000;
            cur = TimeUtil.getThatTimeStampHourTimestamp(new Timestamp(System.currentTimeMillis()));
            last = new Timestamp(cur.getTime() - (listLength - 1) * timeInteval);
        }
        ResultData result = new ResultData();
        JSONArray resultList = new JSONArray();
        for (int i = 0; i < listLength; i++) {
            JSONObject temp = new JSONObject();
            temp.put("machineId", machineId);
            temp.put("createTime", new Timestamp(last.getTime() + i * timeInteval).toString());
            resultList.add(temp);
        }
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject curObj = dataList.getJSONObject(i);
            Timestamp thatHour = TimeUtil.getThatTimeStampHourTimestamp(curObj.getTimestamp("createAt"));
            long diff = (thatHour.getTime() - last.getTime()) / timeInteval;
            if (diff < 0 || diff >= listLength)
                continue;
            for (String key : curObj.keySet()) {
                if (key.contains("average") || key.contains("max") || key.contains("min") || key.contains("powerOn") || key.contains("powerOff") || key.contains("auto") || key.contains("manual") || key.contains("sleep") || key.contains("heatOn") || key.contains("heatOff")) {
                    resultList.getJSONObject((int) diff).put(key, curObj.get(key));
                }
            }
        }
        for (int i = 0; i < listLength; i++) {
            JSONObject jsonObject = resultList.getJSONObject(i);
            for (String key : dataList.getJSONObject(0).keySet()) {
                if (!jsonObject.containsKey(key)) {
                    jsonObject.put(key, 0);
                }
            }
        }
        result.setData(resultList);
        return result;
    }

    //获取machine过去N天的statusType记录
    @GetMapping("/{statusType}/lastNday")
    public ResultData fetchLastNDayData(String qrcode, int lastNday, @PathVariable("statusType") String statusType) {
        ResultData result = new ResultData();
        ResultData response = new ResultData();
        String machineId = getMachineIdByQRcode(qrcode);
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(machineId) || lastNday < 1) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode or from your qrcode can't find a machineId or lastNday is wrong");
            return result;
        }

        //查询数据库获取室内的过去N天的数据
        Timestamp todayZero = TimeUtil.getTodayZeroTimestamp();
        Timestamp lastNDayZero = new Timestamp(todayZero.getTime() - (lastNday - 1) * 24 * 60 * 60 * 1000);
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("createTimeGTE", lastNDayZero);
        condition.put("blockFlag", false);
        //根据不同的数据类型查不同的表
        switch (statusType) {
            case "pm25":
                response = indoorPm25Service.fetchDaily(condition);
                break;
            case "volume":
                response = volumeService.fetchDaily(condition);
                break;
            case "co2":
                response = co2Service.fetchDaily(condition);
                break;
            case "humid":
                response = humidService.fetchDaily(condition);
                break;
            case "temp":
                response = tempService.fetchDaily(condition);
                break;
            case "heat":
                response = heatService.fetchDaily(condition);
                break;
            case "mode":
                response = modeService.fetchDaily(condition);
                break;
            case "power":
                response = powerService.fetchDaily(condition);
                break;
            default:
                result.setDescription("statusType error!");
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return result;
        }
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("can't get any data");
            return result;
        }
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(response.getData()));
        return getFormatedData(machineId, jsonArray, lastNday, 0);
    }

    //获取machine过去N小时的statusType记录
    @GetMapping("/{statusType}/lastNhour")
    public ResultData fetchLastNHourData(String qrcode, int lastNhour, @PathVariable("statusType") String statusType) {
        ResultData result = new ResultData();
        String machineId = getMachineIdByQRcode(qrcode);
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(machineId) || lastNhour < 1) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode or from your qrcode can't find a machineId or lastNhour is wrong");
            return result;
        }
        Timestamp curHourZero = TimeUtil.getThatTimeStampHourTimestamp(new Timestamp(System.currentTimeMillis()));
        Timestamp lastNHourZero = new Timestamp(curHourZero.getTime() - (lastNhour - 1) * 60 * 60 * 1000);
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("createTimeGTE", lastNHourZero);
        condition.put("blockFlag", false);
        ResultData response = new ResultData();
        switch (statusType) {
            case "pm25":
                response = indoorPm25Service.fetchHourly(condition);
                break;
            case "volume":
                response = volumeService.fetchHourly(condition);
                break;
            case "co2":
                response = co2Service.fetchHourly(condition);
                break;
            case "humid":
                response = humidService.fetchHourly(condition);
                break;
            case "temp":
                response = tempService.fetchHourly(condition);
                break;
            case "heat":
                response = heatService.fetchHourly(condition);
                break;
            case "mode":
                response = modeService.fetchHourly(condition);
                break;
            case "power":
                response = powerService.fetchHourly(condition);
                break;
            default:
                result.setDescription("statusType error!");
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return result;
        }
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("can't get any data");
            return result;
        }
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(response.getData()));
        return getFormatedData(machineId, jsonArray, lastNhour, 1);
    }
}
