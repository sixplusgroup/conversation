package finley.gmair.service.impl;

import finley.gmair.dao.UserActionMongoDao;
import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.model.machine.UserAction;
import finley.gmair.service.UserActionMongoService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserActionMongoServiceImpl implements UserActionMongoService {
    @Autowired
    private UserActionMongoDao userActionMongoDao;

    public ResultData getDailyStatisticalData(){
        ResultData result = new ResultData();

        //从mongo获取前一天小时数据
        ResultData response = userActionMongoDao.queryUserAction();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error");
            return result;
        }

        //统计
        List<List<UserAction>> resultList = (List<List<UserAction>>) response.getData();

        if (resultList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("empty list");
            return result;
        }
        result.setData(resultList);
        result.setDescription("success to statistic data");
        return result;
    }

    @Override
    public List<UserActionDaily> dealUserAction2Component(List<UserAction> list) {
        List<UserActionDaily> result = new ArrayList<>();
        String machineId = list.get(0).getQrcode();
        String userId = list.get(0).getUserId();

        Map<String, Long> componentList = list.stream().collect(Collectors.groupingBy(UserAction::getComponent,Collectors.counting()));
        for (String key: componentList.keySet()) {
            String component = key;
            int componentTimes = componentList.get(key).intValue();
            UserActionDaily userActionDaily = new UserActionDaily(userId, machineId, component, componentTimes);
            result.add(userActionDaily);
        }
        return result;
    }
}
