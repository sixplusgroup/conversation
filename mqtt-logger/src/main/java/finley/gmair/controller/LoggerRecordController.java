package finley.gmair.controller;

import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.service.LoggerRecordService;
import finley.gmair.util.QueryPage;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.catalina.util.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logger")
public class LoggerRecordController {

    @Resource
    private LoggerRecordService loggerRecordService;

    @GetMapping(value = "/getById/{recordId}")
    public ResultData getById(@PathVariable String recordId){
        System.out.println(recordId);
        ResultData result = new ResultData();
        if(StringUtils.isBlank(recordId)){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("recordId",recordId);
        ResultData response = loggerRecordService.getById(condition);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("loggerRecordService run fail");
            return result;
        }
        result.setData(response.getData());
        result.setResponseCode(response.getResponseCode());
        return result;
    }

    @GetMapping(value = "/list/{machineId}")
    public ResultData list(@PathVariable String machineId){
        System.out.println(machineId);
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId",machineId);
        ResultData response = loggerRecordService.list(condition);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("loggerRecordService run fail");
            return result;
        }
        QueryPage queryPage = new QueryPage();
        List<LoggerRecord> recordList = (List<LoggerRecord>) response.getData();
        Long count = (Long) loggerRecordService.count(condition).getData();
        queryPage.setCount(count);
        queryPage.setData(recordList);
        queryPage.setLimit(10);
        result.setData(queryPage);
        result.setResponseCode(response.getResponseCode());
        return result;
    }
}
