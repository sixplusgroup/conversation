package finley.gmair.service.impl;

import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.dao.MachineStatusStatisticsDao;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.service.MachinePm25Service;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachinePm2_5Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MachinePm25ServiceImpl implements MachinePm25Service{

    @Autowired
    private MachineStatusMongoDao machineStatusMongoDao;

    @Autowired
    private MachineStatusStatisticsDao machineStatusStatisticsDao;

    @Override
    public ResultData handleHourly() {
        int lastHour = LocalDateTime.now().getHour() - 1;
        ResultData result = new ResultData();
        ResultData response = machineStatusMongoDao.queryHourlyPm25();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            List<MachinePm2_5> list = (List<MachinePm2_5>) response.getData();
            for (MachinePm2_5 machinePm2_5 : list) {
                machinePm2_5.setIndex(lastHour);
            }
            response = machineStatusStatisticsDao.insertHourlyBatch(list);
        }
        return result;
    }

    @Override
    public ResultData handleDaily() {
        ResultData result = new ResultData();
        LocalDate lastDay = LocalDateTime.now().minusDays(1).toLocalDate();
        LocalDate today = LocalDateTime.now().toLocalDate();
        int day = lastDay.getDayOfMonth();
        Map<String, Object> condition = new HashMap<>();
        condition.put("createTimeGTE", lastDay);
        condition.put("createTimeLTE", today);
        condition.put("blockFlag", 0);
        ResultData response = machineStatusStatisticsDao.selectHourly(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            List<MachinePm2_5Vo> list = (List<MachinePm2_5Vo>) response.getData();
            Map<String, Double> map = list.stream().collect(Collectors.groupingBy(MachinePm2_5Vo::getUid,
                    Collectors.averagingDouble(MachinePm2_5Vo::getPm2_5)));

            List<MachinePm2_5> dailyData = map.entrySet().stream()
                    .map(e -> new MachinePm2_5(e.getKey(), day, e.getValue())).collect(Collectors.toList());

            if (!dailyData.isEmpty()) {
                response = machineStatusStatisticsDao.insertDailyBatch(dailyData);
            }
        }
        return result;
    }

    @Override
    public ResultData handleMonthly() {
        ResultData result = new ResultData();
        LocalDate lastMonth = LocalDateTime.now().minusMonths(1).toLocalDate();
        LocalDate currentMonth = LocalDateTime.now().toLocalDate();
        int month = lastMonth.getMonth().getValue();
        Map<String, Object> condition = new HashMap<>();
        condition.put("createTimeGTE", lastMonth);
        condition.put("createTimeLTE", currentMonth);
        condition.put("blockFlag", false);
        ResultData response = machineStatusStatisticsDao.selectDaily(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            List<MachinePm2_5Vo> list = (List<MachinePm2_5Vo>) response.getData();
            Map<String, Double> map = list.stream().collect(Collectors.groupingBy(MachinePm2_5Vo::getUid,
                    Collectors.averagingDouble(MachinePm2_5Vo::getPm2_5)));

            List<MachinePm2_5> monthlyData = map.entrySet().stream()
                    .map(e -> new MachinePm2_5(e.getKey(), month, e.getValue())).collect(Collectors.toList());

            if (!monthlyData.isEmpty()) {
                response = machineStatusStatisticsDao.insertMonthlyBatch(monthlyData);
            }
        }
        return result;
    }

    @Override
    public ResultData fetchPartialLatestPm25(String uid,String name) {
        ResultData result = new ResultData();
        ResultData response = machineStatusMongoDao.queryPartialLatestPm25(uid,name);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            List<MachinePartialStatus> list = (List<MachinePartialStatus>) response.getData();
            result.setData(list.get(0));
            result.setDescription("success to fetch latest pm2.5 in machine_partial_status collection");
        }
        return result;
    }
}
