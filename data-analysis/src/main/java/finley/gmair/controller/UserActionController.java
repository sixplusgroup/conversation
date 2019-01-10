package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.service.UserActionMongoService;
import finley.gmair.service.UserActionDailyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data/analysis/user/action")
public class UserActionController {

    @Autowired
    private UserActionMongoService userActionMongoService;

    private UserActionDailyService userActionDailyService;

    private Logger logger = LoggerFactory.getLogger(UserActionController.class);

    @GetMapping("/probe/statistical/lastday")
    public ResultData probeStatisticalData(){
        ResultData result = new ResultData();
        ResultData response = userActionMongoService.getDailyStatisticalData();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            return result;
        }
        return result;
    }

    @PostMapping("/schedule/statistical/daily")
    public ResultData statisticalDataDaily(){
        ResultData result = new ResultData();
        //ResultData re = probeStatisticalData();
        ResultData response = userActionMongoService.getDailyStatisticalData();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("ERROR");
            return result;
        }
        List<Object> statisticalDataList = (List<Object>) response.getData();
        List<JSONObject> dataList = statisticalDataList.stream().map(e -> JSONObject.parseObject(e.toString())).collect(Collectors.toList());
        List<UserActionDaily> userActionDailyList = dataList.stream().map(e -> new UserActionDaily(e.getString("recordId"),e.getString("userId"),e.getString("machineId"),e.getString("component"),e.getIntValue("componentTimes"))).collect(Collectors.toList());
        ResultData response1 = userActionDailyService.insertBatchDaily(userActionDailyList);

        List<Object> resultList = new ArrayList<>();
        resultList.add(response1.getData());
        result.setData(resultList);
        result.setDescription("success to insert statistical user action data daily into database");
        return result;
    }
}
