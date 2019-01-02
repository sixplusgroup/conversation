package finley.gmair.dao.impl;

import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MachineStatusMongoDaoImpl implements MachineStatusMongoDao {

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
            MachineStatus machineStatus = mongoTemplate.findOne(query, MachineStatus.class);
            result.setData(machineStatus);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public ResultData queryHourlyPm25() {

        //fetch last hour's v2 machine_status list from mongodb
        ResultData result = new ResultData();
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long currentHour = (System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));

        Query query = new Query();
        query.addCriteria(Criteria.where("createAt").gte(lastHour).lt(currentHour));
        List<MachineStatus> machineStatusList = new ArrayList<>();
        try {
            machineStatusList = mongoTemplate.find(query, MachineStatus.class);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        List<MachinePm2_5> resultList = new ArrayList<>();
        if (!machineStatusList.isEmpty()) {
            //group by uid and compute the average pm2.5
            Map<String, List<MachineStatus>> groupByUid =
                    machineStatusList.stream().collect(Collectors.groupingBy(MachineStatus::getUid));
            Iterator iter = groupByUid.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String uid = (String) entry.getKey();
                List<MachineStatus> list = (List<MachineStatus>) entry.getValue();
                if (list.isEmpty())
                    continue;
                double sumPm2_5 = 0.0;
                for (MachineStatus machineStatus : list) {
                    sumPm2_5 += machineStatus.getPm2_5();
                }
                MachinePm2_5 machinePm2_5 = new MachinePm2_5();
                machinePm2_5.setUid(uid);
                machinePm2_5.setPm2_5(sumPm2_5 / list.size());
                resultList.add(machinePm2_5);
            }
        }

        //fetch last hour's v1 machine_status list from mongodb
        List<MachineV1Status> machineV1StatusList = new ArrayList<>();
        try {
            machineV1StatusList = mongoTemplate.find(query, MachineV1Status.class);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //group by uid and compute the average pm2.5
        if (!machineV1StatusList.isEmpty()) {
            Map<String, List<MachineV1Status>> groupByMachineId =
                    machineV1StatusList.stream().collect(Collectors.groupingBy(MachineV1Status::getMachineId));
            Iterator iter2 = groupByMachineId.entrySet().iterator();
            while (iter2.hasNext()) {
                Map.Entry entry = (Map.Entry) iter2.next();
                String uid = (String) entry.getKey();
                List<MachineV1Status> list = (List<MachineV1Status>) entry.getValue();
                if (list.isEmpty())
                    continue;
                double sumPm2_5 = 0.0;
                for (MachineV1Status machineStatus : list) {
                    sumPm2_5 += machineStatus.getPm25();
                }
                MachinePm2_5 machinePm2_5 = new MachinePm2_5();
                machinePm2_5.setUid(uid);
                machinePm2_5.setPm2_5(sumPm2_5 / list.size());
                resultList.add(machinePm2_5);
            }
        }

        if (resultList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("we can not collect any data in mongo to compute at this time");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(resultList);
        result.setDescription("success to collect and compute the average pm2.5");
        return result;
    }


    @Override
    public ResultData queryPartialLatestPm25(String uid, String name) {
        ResultData result = new ResultData();
        long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
        Query query = new Query();
        if (uid != null)
            query.addCriteria(Criteria.where("uid").is(uid));
        if (name != null)
            query.addCriteria(Criteria.where("name").is(name));
        query.addCriteria(Criteria.where("createAt").gte(lastHour));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createAt")));
        try {
            MachinePartialStatus machinePartialStatus = mongoTemplate.findOne(query, MachinePartialStatus.class);
            result.setData(machinePartialStatus);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    //查询最近24小时的滤网pm2.5平均值,整理出一个List(machineId,averagePM24)
    @Override
    public ResultData queryPartialAveragePm25() {
        ResultData result = new ResultData();
        long last24Hour = (System.currentTimeMillis() - 1000 * 60 * 60 * 24) / (1000 * 60 * 60) * (1000 * 60 * 60);
        //long now = (System.currentTimeMillis()) / (1000 * 60 * 60) * (1000 * 60 * 60);
        long now = System.currentTimeMillis();
        Query query = new Query();
        query.addCriteria(Criteria.where("createAt").gte(last24Hour).lt(now));
        List<MachinePartialStatus> machinePartialStatusList = new ArrayList<>();
        try {
            machinePartialStatusList = mongoTemplate.find(query, MachinePartialStatus.class);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //group and compute average
        List<MachinePartialStatus> resultList = new ArrayList<>();
        Map<String, List<MachinePartialStatus>> groupByUid =
                machinePartialStatusList.stream().collect(Collectors.groupingBy(MachinePartialStatus::getUid));
        Iterator iter = groupByUid.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String uid = (String) entry.getKey();
            List<MachinePartialStatus> list = (List<MachinePartialStatus>) entry.getValue();
            if (list.isEmpty())
                continue;
            int sumPm2_5 = 0;
            for (MachinePartialStatus machinePartialStatus : list) {
                sumPm2_5 += (int) machinePartialStatus.getData();
            }
            MachinePartialStatus mps = new MachinePartialStatus();
            mps.setUid(uid);
            mps.setData((double) sumPm2_5 / (double) list.size());
            resultList.add(mps);
        }
        result.setData(resultList);
        return result;
    }


    public ResultData queryMachineV1Status(Map<String, Object> condition) {
        ResultData result = new ResultData();
        Query query = new Query();
        if (condition.get("machineId") != null) {
            query.addCriteria(Criteria.where("uid").is(condition.get("machineId")));
        }
        if (condition.get("power") != null) {
            query.addCriteria(Criteria.where("power").is(condition.get("power")));
        }
        if (condition.get("createAtGTE") != null && condition.get("createAtLT") != null) {
            query.addCriteria(Criteria.where("createAt").gte(condition.get("createAtGTE")).lt(condition.get("createAtLT")));
        } else if (condition.get("createAtGTE") != null) {
            query.addCriteria(Criteria.where("createAt").gte(condition.get("createAtGTE")));
        } else if (condition.get("createAtLT") != null) {
            query.addCriteria(Criteria.where("createAt").lt(condition.get("createAtLT")));
        }
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "createAt")));

        try {
            List<MachineV1Status> machineStatusList = mongoTemplate.find(query, MachineV1Status.class);
            if (machineStatusList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            } else {
                result.setData(machineStatusList);
                return result;
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            e.printStackTrace();
        }
        return result;
    }

    public ResultData queryMachineV2Status(Map<String, Object> condition) {
        ResultData result = new ResultData();
        Query query = new Query();
        if (condition.get("machineId") != null) {
            query.addCriteria(Criteria.where("uid").is(condition.get("machineId")));
        }
        if (condition.get("power") != null) {
            query.addCriteria(Criteria.where("power").is(condition.get("power")));
        }
        if (condition.get("createAtGTE") != null && condition.get("createAtLT") != null) {
            query.addCriteria(Criteria.where("createAt").gte(condition.get("createAtGTE")).lt(condition.get("createAtLT")));
        } else if (condition.get("createAtGTE") != null) {
            query.addCriteria(Criteria.where("createAt").gte(condition.get("createAtGTE")));
        } else if (condition.get("createAtLT") != null) {
            query.addCriteria(Criteria.where("createAt").lt(condition.get("createAtLT")));
        }
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "createAt")));

        try {
            List<MachineStatus> machineStatusList = mongoTemplate.find(query, MachineStatus.class);
            if (machineStatusList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            } else {
                result.setData(machineStatusList);
                return result;
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            e.printStackTrace();
        }
        return result;
    }
}
