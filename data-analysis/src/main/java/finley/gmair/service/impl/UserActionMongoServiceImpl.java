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
        Map<String, Object> map = (HashMap) response.getData();

        //统计
        List<Object> resultList = new ArrayList<>();

        if (resultList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("empty list");
            return result;
        }
        result.setData(resultList);
        result.setDescription("success to statistic data");
        return result;
    }

    private List<UserActionDaily> countUserAction(LinkedList<UserAction> list) {
        List<UserActionDaily> result = new ArrayList<>();
        String recordId = list.get(0).getLogId();
        String machineId = list.get(2).getQrcode();
        Map<String, List<UserAction>> useridList = list.stream().collect(Collectors.groupingBy(UserAction::getUserId));
        for (String key1: useridList.keySet()) {
            String userId = key1;
            List<UserAction> userActionList = useridList.get(userId);
            Map<String, Long> componentList = list.stream().collect(Collectors.groupingBy(UserAction::getComponent,Collectors.counting()));
            for (String key2: componentList.keySet()) {
                String component = key2;
                int componentTimes = componentList.get(key2).intValue();
                UserActionDaily userActionDaily = new UserActionDaily(recordId, userId, machineId, component, componentTimes);
                result.add(userActionDaily);
            }
        }
        return result;
    }
}
