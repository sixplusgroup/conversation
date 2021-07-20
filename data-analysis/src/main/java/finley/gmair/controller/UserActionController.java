package finley.gmair.controller;

import finley.gmair.model.analysis.ComponentMean;
import finley.gmair.model.analysis.UserActionDaily;
import finley.gmair.model.machine.UserAction;
import finley.gmair.service.ComponentMeanService;
import finley.gmair.service.UserActionMongoService;
import finley.gmair.service.UserActionDailyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    @Autowired
    private ComponentMeanService componentMeanService;


    @PostMapping("/schedule/statistical/userId/daily")
    public ResultData probeByUserId2Component() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        long lastDay = (System.currentTimeMillis() - 1000 * 60 * 60 * 24) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long currentDay = (System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));
        condition.put("start", lastDay);
        condition.put("end", currentDay);
        ResultData response = userActionMongoService.fetchData(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System Error");
            return result;
        }

        //获取mongo数据，首先进行userId group
        List<UserAction> actionList = (List<UserAction>) response.getData();
        response = userActionMongoService.getDataGroupByUserId(actionList);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("retrieve by userId error");
            return result;
        }

        //获取group by userId list,继续进行component group
        List<List<UserAction>> userIdLists = (List<List<UserAction>>) response.getData();
        List<UserActionDaily> actionDailyList = new ArrayList<>();
        for (List<UserAction> list : userIdLists) {
            String userId = list.get(0).getUserId();
            String qrcode = list.get(0).getQrcode();
            Map<String, Long> map = list.stream().collect(Collectors.groupingBy(UserAction::getComponent, Collectors.counting()));
            for (String componentKey : map.keySet()) {
                int componentTimes = map.get(componentKey).intValue();
                UserActionDaily userActionDaily = new UserActionDaily(userId, qrcode, componentKey, componentTimes);
                actionDailyList.add(userActionDaily);
            }
        }
        if (actionDailyList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("retrieve action daily list error");
            return result;
        }

        //list不为空，批量插入
        response = userActionDailyService.insertBatchDaily(actionDailyList);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("store user action daily error");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("store successfully");
        return result;
    }

    @PostMapping("/schedule/statistical/component/daily")
    public ResultData probeByComponent() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        long lastDay = (System.currentTimeMillis() - 1000 * 60 * 60 * 24) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long currentDay = (System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("0.00");
        condition.put("start", lastDay);
        condition.put("end", currentDay);
        ResultData response = userActionMongoService.fetchData(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System Error");
            return result;
        }

        //获取mongo数据，进行component group
        List<UserAction> actionList = (List<UserAction>) response.getData();
        response = userActionMongoService.getDataGroupByComponent(actionList);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("group user action by component error");
            return result;
        }
        List<List<UserAction>> componentLists = (List<List<UserAction>>) response.getData();
        List<ComponentMean> componentDailyList = new ArrayList<>();
        for (List<UserAction> componentList : componentLists) {
            Map<String, Long> map = componentList.stream().collect(Collectors.groupingBy(UserAction::getUserId, Collectors.counting()));
            int componentSize = componentList.size();
            int userNum = map.size();
            double component_mean = Double.valueOf(df.format((double) componentSize / (double) userNum));
            String date_index = sdf.format(lastDay);
            String component = componentList.get(0).getComponent();
            ComponentMean componentMean = new ComponentMean(date_index, component, componentSize, component_mean);
            componentDailyList.add(componentMean);
        }
        if (componentDailyList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("retrieve component list error");
            return result;
        }

        //list不为空，批量插入
        response = componentMeanService.insertBatchDaily(componentDailyList);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("store component data error");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("store component data successfully");
        return result;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResultData queryUserAction(String userId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(userId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("userId", userId);
        condition.put("blockFlag", false);
        ResultData response = userActionDailyService.fetchDaily(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get user action");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find user action record");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
