package finley.gmair.controller;

import finley.gmair.model.mqtt.ApiBound;
import finley.gmair.service.ApiBoundService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bound")
public class ApiBoundController {

    @Autowired
    private ApiBoundService apiBoundService;

    @PostMapping("/create")
    public ResultData create(String apiName, String apiUrl, String apiTopic, String apiDescription) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(apiName) || StringUtils.isEmpty(apiUrl) || StringUtils.isEmpty(apiTopic)
            || StringUtils.isEmpty(apiDescription)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        ApiBound bound = new ApiBound(apiName, apiUrl, apiTopic, apiDescription);
        ResultData response = apiBoundService.createApiBound(bound);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store api bound to database");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @GetMapping("/query")
    public ResultData query(String apiName) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (!StringUtils.isEmpty(apiName)) {
            condition.put("apiName", apiName);
        }
        ResultData response = apiBoundService.fetchApiBound(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No bound found from database");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to retrieve api bound");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    @PostMapping("/update")
    public ResultData update(String apiName, String apiUrl, String apiTopic, String apiDescription) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(apiName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("apiName", apiName);
        ResultData response = apiBoundService.fetchApiBound(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Wrong name, please inspect");
            return result;
        }
        ApiBound bound = ((List<ApiBound>)response.getData()).get(0);
        String boundId = bound.getBoundId();
        condition.clear();
        condition.put("boundId", boundId);
        if (!StringUtils.isEmpty(apiUrl)) {
            condition.put("apiUrl", apiUrl);
        }
        if (!StringUtils.isEmpty(apiTopic)) {
            condition.put("apiTopic", apiTopic);
        }
        if (!StringUtils.isEmpty(apiDescription)) {
            condition.put("apiDescription", apiDescription);
        }
        response = apiBoundService.modifyApiBound(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Update api related information unsuccessfully");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Update successfully");
        return result;
    }
}
