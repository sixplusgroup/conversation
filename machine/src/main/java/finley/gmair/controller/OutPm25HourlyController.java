package finley.gmair.controller;

import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.OutPm25HourlyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/latest/pm2_5")
public class OutPm25HourlyController {
    @Autowired
    private OutPm25HourlyService outPm25HourlyService;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;
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
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the machineId by qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the machineId by qrcode");
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
}
