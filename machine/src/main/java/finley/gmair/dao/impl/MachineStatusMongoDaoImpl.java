package finley.gmair.dao.impl;

import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MachineStatusMongoDaoImpl implements MachineStatusMongoDao{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();

        Query query = new Query();

        if (condition.get("uid") != null) {
            query.addCriteria(Criteria.where("uid").is(condition.get("uid")));
        }

        if (condition.get("createAtGTE") != null) {
            query.addCriteria(Criteria.where("createAt").gte(condition.get("createAtGTE")));
        }

        try {
            MachineStatus machineStatus = mongoTemplate.findOne(query , MachineStatus.class);

            result.setData(machineStatus);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            e.printStackTrace();
        }
        return result;
    }
}
