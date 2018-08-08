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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        //fetch last hour's machine_status list from mongodb
        ResultData result = new ResultData();
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long currentHour = (System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));
        long forTestHour = System.currentTimeMillis();

        Query query = new Query();
        query.addCriteria(Criteria.where("createAt").gte(lastHour).lte(forTestHour));
        List<MachineStatus> machineStatusList = new ArrayList<>();
        try {
             machineStatusList = mongoTemplate.find(query, MachineStatus.class);
        }catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }


        //group by uid and compute the average pm2.5
        List<MachinePm2_5> resultList = new ArrayList<>();
        Map<String, List<MachineStatus>> groupByUid =
                machineStatusList.stream().collect(Collectors.groupingBy(MachineStatus::getUid));
        Iterator iter = groupByUid.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            String uid = (String)entry.getKey();
            List<MachineStatus> list = (List<MachineStatus>)entry.getValue();
            if(list.isEmpty())
                continue;
            double sumPm2_5 = 0.0;
            for (MachineStatus machineStatus:list) {
                sumPm2_5 += machineStatus.getPm2_5();
            }
            MachinePm2_5 machinePm2_5 = new MachinePm2_5();
            machinePm2_5.setUid(uid);
            machinePm2_5.setPm2_5(sumPm2_5/list.size());
            resultList.add(machinePm2_5);
        }
        result.setData(resultList);
        return result;

//        Aggregation aggregation = newAggregation(
//                match(Criteria.where("createAt").gte(lastHour)),
//                match(Criteria.where("createAt").lte(currentHour)),
//                group("uid").avg("pm2_5").as("pm2_5"),
//                project().andExpression("_id").as("uid")
//                         .andExpression("pm2_5").as("pm2_5"));
//
//        try {
//            AggregationResults<MachinePm2_5> data = mongoTemplate
//                    .aggregate(aggregation, "machine_status", MachinePm2_5.class);
//
//            if (data.getMappedResults().isEmpty()) {
//                result.setResponseCode(ResponseCode.RESPONSE_NULL);
//            } else {
//                result.setData(data.getMappedResults());
//            }
//        } catch (Exception e) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription(e.getMessage());
//            e.printStackTrace();
//        }
//
//        return result;
    }


    @Override
    public ResultData queryPartialLatestPm25(String uid,String name){
        ResultData result = new ResultData();
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
        Query query = new Query();
        if(uid != null)
            query.addCriteria(Criteria.where("uid").is(uid));
        if(name != null)
            query.addCriteria(Criteria.where("name").is(name));
        query.addCriteria(Criteria.where("createAt").gte(lastHour));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createAt")));
        try {
            MachinePartialStatus machinePartialStatus = mongoTemplate.findOne(query, MachinePartialStatus.class);
            result.setData(machinePartialStatus);
        }catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
//        Aggregation aggregation = newAggregation(
//                match(Criteria.where("uid").is(uid)),
//                match(Criteria.where("name").is(name)),
//                match(Criteria.where("createAt").gte(lastHour)),
//                sort(Sort.Direction.DESC,"createAt"),
//                limit(1)
//        );
//        try {
//            AggregationResults<MachinePartialStatus> data = mongoTemplate
//                    .aggregate(aggregation, "machine_partial_status", MachinePartialStatus.class);
//            if (data.getMappedResults().isEmpty()) {
//                result.setResponseCode(ResponseCode.RESPONSE_NULL);
//            } else {
//                result.setData(data.getMappedResults());
//            }
//        } catch (Exception e) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription(e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
    }

    @Override
    public ResultData queryPartialAveragePm25(){
        ResultData result = new ResultData();
        long last25Hour = (System.currentTimeMillis() - 1000 * 60 * 60 * 25) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);

        Query query = new Query();
        query.addCriteria(Criteria.where("createAt").gte(last25Hour).lt(lastHour));
        List<MachinePartialStatus> machinePartialStatusList = new ArrayList<>();
        try {
            machinePartialStatusList = mongoTemplate.find(query, MachinePartialStatus.class);
        }catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //group and compute average
        List<MachinePartialStatus> resultList = new ArrayList<>();
        Map<String, List<MachinePartialStatus>> groupByUid =
                machinePartialStatusList.stream().collect(Collectors.groupingBy(MachinePartialStatus::getUid));
        Iterator iter = groupByUid.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            String uid = (String)entry.getKey();
            List<MachinePartialStatus> list = (List<MachinePartialStatus>)entry.getValue();
            if(list.isEmpty())
                continue;
            int sumPm2_5 = 0;
            for (MachinePartialStatus machinePartialStatus:list) {
                sumPm2_5 += (int)machinePartialStatus.getData();
            }
            MachinePartialStatus mps = new MachinePartialStatus();
            mps.setUid(uid);
            mps.setData((double)sumPm2_5/(double)list.size());
            resultList.add(mps);
        }
        result.setData(resultList);
        return result;

//        //long forTestHour = System.currentTimeMillis();
//        Aggregation aggregation = newAggregation(
//                match(Criteria.where("createAt").gte(last25Hour)),
//                match(Criteria.where("createAt").lte(lastHour)),
//                group("uid").avg("data").as("data"),
//                project().andExpression("_id").as("uid")
//                        .andExpression("data").as("data"));
//        try {
//            AggregationResults<MachinePartialStatus> data = mongoTemplate
//                    .aggregate(aggregation, "machine_partial_status", MachinePartialStatus.class);
//            if (data.getMappedResults().isEmpty()) {
//                result.setResponseCode(ResponseCode.RESPONSE_NULL);
//                result.setDescription("not find data in mongo");
//            } else {
//                result.setData(data.getMappedResults());
//            }
//        } catch (Exception e) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription(e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
    }
}
