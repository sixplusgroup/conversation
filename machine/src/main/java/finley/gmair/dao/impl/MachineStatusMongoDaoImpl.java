package finley.gmair.dao.impl;

import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
            query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createAt")));
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
        Aggregation aggregation = newAggregation(
                match(Criteria.where("createAt").gte(lastHour)),
                match(Criteria.where("createAt").lte(currentHour)),
                group("uid").avg("pm2_5").as("pm2_5"),
                project().andExpression("_id").as("uid")
                         .andExpression("pm2_5").as("pm2_5"));

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


    @Override
    public ResultData queryPartialLatestPm25(String uid,String name){
        ResultData result = new ResultData();
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
        Aggregation aggregation = newAggregation(
                match(Criteria.where("uid").is(uid)),
                match(Criteria.where("name").is(name)),
                match(Criteria.where("createAt").gte(lastHour)),
                sort(Sort.Direction.DESC,"createAt"),
                limit(1)
        );
        try {
            AggregationResults<MachinePartialStatus> data = mongoTemplate
                    .aggregate(aggregation, "machine_partial_status", MachinePartialStatus.class);
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

    @Override
    public ResultData queryPartialAveragePm25(){
        ResultData result = new ResultData();
        long last25Hour = (System.currentTimeMillis() - 1000 * 60 * 60 * 25) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long forTestHour = System.currentTimeMillis();
        Aggregation aggregation = newAggregation(
                match(Criteria.where("createAt").gte(last25Hour)),
                match(Criteria.where("createAt").lte(forTestHour)),
                group("uid").avg("data").as("data"),
                project().andExpression("_id").as("uid")
                        .andExpression("data").as("data"));
        try {
            AggregationResults<MachinePartialStatus> data = mongoTemplate
                    .aggregate(aggregation, "machine_partial_status", MachinePartialStatus.class);
            if (data.getMappedResults().isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find data in mongo");
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
