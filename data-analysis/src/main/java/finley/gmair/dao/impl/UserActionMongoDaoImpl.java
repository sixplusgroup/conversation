package finley.gmair.dao.impl;

import finley.gmair.dao.UserActionMongoDao;
import finley.gmair.model.machine.UserAction;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserActionMongoDaoImpl implements UserActionMongoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        Query query = new Query();
        query.addCriteria(Criteria.where("createAt").gte(condition.get("start")).lt(condition.get("end")));
        List<UserAction> userActionList = new ArrayList<>();
        try {
            userActionList = mongoTemplate.find(query, UserAction.class);
            if (userActionList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(userActionList);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryUserActionByUserId(List<UserAction> list) {
        //fetch last day's user action list from mongodb
        ResultData result = new ResultData();

        if (list.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("input is null");
            return result;
        }

        List<List<UserAction>> resultList = new ArrayList<>();

        Map<String, List<UserAction>> map = list.stream().collect(Collectors.groupingBy(UserAction::getUserId));
        for (String key : map.keySet()) {
            resultList.add(map.get(key));
        }

        if (resultList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("can not collect user action with grouping by userId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(resultList);
        return result;
    }

    @Override
    public ResultData queryUserActionByComponent(List<UserAction> list) {
        ResultData result = new ResultData();
        if (list.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("input is null");
            return result;
        }

        List<List<UserAction>> resultList = new ArrayList<>();

        Map<String, List<UserAction>> map = list.stream().collect(Collectors.groupingBy(UserAction::getComponent));
        for (String key : map.keySet()) {
            resultList.add(map.get(key));
        }

        if (resultList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not collect user action with grouping by component");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(resultList);
        return result;
    }
}
