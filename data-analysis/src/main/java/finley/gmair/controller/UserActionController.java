package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.model.machine.UserAction;
import finley.gmair.service.UserActionMongoService;
import finley.gmair.service.UserActionDailyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data/analysis/user/action")
public class UserActionController {

    @Autowired
    private UserActionMongoService userActionMongoService;

    @Autowired
    private UserActionDailyService userActionDailyService;


    @PostMapping("/schedule/statistical/daily")
    public ResultData statisticalDataDaily() {
        ResultData result = new ResultData();
        ResultData response = userActionMongoService.getDailyStatisticalData();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error");
            return result;
        }

        List<List<UserAction>> list = (List<List<UserAction>>) response.getData();
        List<UserActionDaily> dailyList = new ArrayList<>();
        for (List<UserAction> itemList : list) {
            dailyList = userActionMongoService.dealUserAction2Component(itemList);
            if (!dailyList.isEmpty()) {
                response = userActionDailyService.insertBatchDaily(dailyList);
            }
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("store successfully");
        return result;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResultData queryUserAction(String userId){
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(userId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("userId",userId);
        condition.put("blockFlag", false);
        ResultData response = userActionDailyService.fetchDaily(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get user action");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find user action record");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
