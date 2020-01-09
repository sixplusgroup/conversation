package finley.gmair.controller;

import finley.gmair.model.mqtt.LoggerRecord;
import finley.gmair.service.LoggerRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.catalina.util.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/logger/")
public class LoggerRecordController {

    @Resource
    private LoggerRecordService loggerRecordService;

    @PostMapping(value = "getById")
    public ResultData getById(String recordId){
        ResultData result = new ResultData();
        if(StringUtils.isBlank(recordId)){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        ResultData response = loggerRecordService.getById(recordId);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("loggerRecordService run fail");
            return result;
        }
        result.setData(response.getData());
        result.setResponseCode(response.getResponseCode());
        return result;
    }
}
