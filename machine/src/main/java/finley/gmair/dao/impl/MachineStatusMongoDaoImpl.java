package finley.gmair.dao.impl;

import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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


    @Override
    public ResultData queryHourlyPm25() {
        ResultData result = new ResultData();
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long currentHour = (System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));
        Aggregation aggregation = newAggregation(group("uid").avg("pm2_5").as("pm25"),
                match(Criteria.where("createAt").gte(lastHour).lt(currentHour)),
                project("uid", "pm25")
        );

        try {
            AggregationResults<MachinePm2_5> data = mongoTemplate
                    .aggregate(aggregation, "machine_status", MachinePm2_5.class);

            if (data.getMappedResults().isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(data.getMappedResults());
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
