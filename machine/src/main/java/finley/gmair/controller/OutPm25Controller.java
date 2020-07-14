package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.OutPm25DailyService;
import finley.gmair.service.OutPm25HourlyService;
import finley.gmair.service.PreBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/latest/pm2_5")
public class OutPm25Controller {
    @Autowired
    private OutPm25HourlyService outPm25HourlyService;

    @Autowired
    private OutPm25DailyService outPm25DailyService;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private PreBindService preBindService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createOutPm25Hourly(String machineId, int pm2_5) {

        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide machineId");
            return result;
        }

        //create boundary pm2.5
        OutPm25Hourly outPm25Hourly = new OutPm25Hourly(machineId, pm2_5, 0);
        ResultData response = outPm25HourlyService.create(outPm25Hourly);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create latest pm2.5");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setDescription("success to create latest pm2.5");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/probe/by/machineId", method = RequestMethod.GET)
    public ResultData probeOutPm25ByMachineId(String machineId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId");
            return result;
        }

        //probe pm2.5 by modelId
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        ResultData response = outPm25HourlyService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe latest pm2.5 by modelId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find latest pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to find latest pm2.5 by modelId");
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(value = "/24hour/probe/bycode",method = RequestMethod.GET)
    public ResultData probeLast24HourOutPm25ByQrcode(String qrcode) {
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the qrcode");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machineId是否已获取，如果没有则进行相应的处理
        response = preBindService.checkMachineId(response, result, qrcode);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>)response.getData()).get(0).getMachineId();
        return probeLast24HourOutPm25ByMachineId(machineId);
    }
    @RequestMapping(value = "/24hour/probe", method = RequestMethod.GET)
    public ResultData probeLast24HourOutPm25ByMachineId(String machineId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId");
            return result;
        }

        //probe pm2.5 by modelId
        Map<String, Object> condition = new HashMap<>();
        //获取当前时间整点时间戳
        Timestamp curHour = TimeUtil.getCurrentHourTimestamp();
        Timestamp last24Hour = new Timestamp(curHour.getTime() - 24 * 60 * 60 * 1000);
        condition.put("machineId", machineId);
        condition.put("createTimeGT", last24Hour);
        condition.put("blockFlag", false);
        ResultData response = outPm25HourlyService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe latest 24 hour pm2.5 by machineId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find latest 24 hour pm2.5 by machineId");
            return result;
        }
        List<OutPm25Hourly> list = (List<OutPm25Hourly>) response.getData();


        //格式化时间
        List<OutPm25Hourly> resultList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            resultList.add(new OutPm25Hourly(machineId, new Timestamp(last24Hour.getTime() + (i + 1) * 60 * 60 * 1000)));
        }
        for (int i = 0; i < list.size(); i++) {
            //获取这条记录时间戳整点的时间戳
            Timestamp thatHour = TimeUtil.getThatTimeStampHourTimestamp(list.get(i).getCreateAt());
            long hourDiff = (thatHour.getTime() - last24Hour.getTime()) / (60 * 60 * 1000) - 1;
            if (hourDiff < 0 || hourDiff > 23)
                continue;
            resultList.get((int) hourDiff).setPm2_5(list.get(i).getPm2_5());
        }

        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to find latest pm2.5 by machineId");
        result.setData(resultList);
        return result;
    }

    @RequestMapping(value = "/modify/by/machineId", method = RequestMethod.POST)
    public ResultData modifyOutPm25ByModelId(String machineId, int pm2_5) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId");
            return result;
        }

        //modify
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("pm2_5", pm2_5);
        condition.put("blockFlag", false);
        ResultData response = outPm25HourlyService.updateByMachineId(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to modify latest pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to modify latest pm2.5 by modelId");
        result.setData(response.getData());
        return result;
    }

    private ResultData getFormatedData(String machineId, JSONArray dataList, int listLength, int timeType) {
        Timestamp cur;
        Timestamp last;
        int timeInteval = 0;
        //0代表格式化daily data
        if (timeType == 0) {
            timeInteval = 24 * 60 * 60 * 1000;
            cur = TimeUtil.getTodayZeroTimestamp();
            last = new Timestamp(cur.getTime() - (listLength - 1) * timeInteval);
        }
        //其他代表格式化hourly data
        else {
            timeInteval = 60 * 60 * 1000;
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
                if (key.contains("average") || key.contains("over") ||key.contains("pm2_5")||key.contains("indexHour")) {
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

    //获取machine过去N天的pm2.5记录
    @GetMapping("/lastNday")
    public ResultData fetchLastNDayData(String qrcode, int lastNday) {
        ResultData result = new ResultData();
        //第一步，查找machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machineId是否已获取，如果没有则进行相应的处理
        response = preBindService.checkMachineId(response, result, qrcode);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>)response.getData()).get(0).getMachineId();
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(machineId) || lastNday < 1) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode or from your qrcode can't find a machineId or lastNday is wrong");
            return result;
        }

        //查询数据库获取室内的过去N天的数据
        Timestamp todayZero = TimeUtil.getTodayZeroTimestamp();
        Timestamp lastNDayZero = new Timestamp(todayZero.getTime() - (lastNday - 1) * 24 * 60 * 60 * 1000);
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("createTimeGTE", lastNDayZero);
        condition.put("blockFlag", false);
        response = outPm25DailyService.fetch(condition);
        //根据不同的数据类型查不同的表
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("can't get any data");
            return result;
        }
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(response.getData()));
        return getFormatedData(machineId, jsonArray, lastNday, 0);
    }

    //获取machine过去N小时的pm2.5记录
    @GetMapping("/lastNhour")
    public ResultData fetchLastNHourData(String qrcode, int lastNhour) {
        ResultData result = new ResultData();
        //第一步，查找machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machineId是否已获取，如果没有则进行相应的处理
        response = preBindService.checkMachineId(response, result, qrcode);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>)response.getData()).get(0).getMachineId();
        if (org.springframework.util.StringUtils.isEmpty(qrcode) || org.springframework.util.StringUtils.isEmpty(machineId) || lastNhour < 1) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode or from your qrcode can't find a machineId or lastNhour is wrong");
            return result;
        }
        Timestamp curHourZero = TimeUtil.getThatTimeStampHourTimestamp(new Timestamp(System.currentTimeMillis()));
        Timestamp lastNHourZero = new Timestamp(curHourZero.getTime() - (lastNhour - 1) * 60 * 60 * 1000);
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("createTimeGTE", lastNHourZero);
        condition.put("blockFlag", false);
        response = outPm25HourlyService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("can't get any data");
            return result;
        }
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(response.getData()));
        return getFormatedData(machineId, jsonArray, lastNhour, 1);
    }
}
