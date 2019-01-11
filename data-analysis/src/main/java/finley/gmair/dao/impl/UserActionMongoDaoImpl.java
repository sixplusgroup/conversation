package finley.gmair.dao.impl;

import finley.gmair.dao.UserActionMongoDao;
import finley.gmair.model.machine.UserAction;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        if (condition.get("logId") != null){
            query.addCriteria(Criteria.where("logId").is(condition.get("logId")));
        }
        if (condition.get("time") != null){
            query.addCriteria(Criteria.where("time").is(condition.get("time")));
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"time")));
        }

        try{
            UserAction userAction = mongoTemplate.findOne(query,UserAction.class);
            result.setData(userAction);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData queryUserAction() {
        //fetch last day's user action list from mongodb
        ResultData result = new ResultData();
        long lastDay = (System.currentTimeMillis() - 1000 * 60 * 60 * 24) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long currentDay = (System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));

        Query query = new Query();
        query.addCriteria(Criteria.where("createAt").gte(lastDay).lt(currentDay));
        List<UserAction> userActionList = new ArrayList<>();
        try {
            userActionList = mongoTemplate.find(query, UserAction.class);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        List<List<UserAction>> resultList = new ArrayList<>();
        if (!userActionList.isEmpty()) {
            Map<String, List<UserAction>> map = userActionList.stream().collect(Collectors.groupingBy(UserAction::getUserId));
            for (String key : map.keySet()) {
                resultList.add(map.get(key));
            }
        }

        if (resultList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("we can not collect any data in mongo to compute at this time");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(resultList);
        result.setDescription("success to collect");
        return result;
    }
}
