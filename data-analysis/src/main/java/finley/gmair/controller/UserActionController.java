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
}
