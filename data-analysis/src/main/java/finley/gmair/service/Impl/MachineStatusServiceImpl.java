package finley.gmair.service.Impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.*;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.dataAnalysis.*;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.service.MachineStatusService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MachineStatusServiceImpl implements MachineStatusService {
    @Autowired
    private MachineStatusRedisDao machineStatusRedisDao;

    @Autowired
    private Co2HourlyDao co2HourlyDao;

    @Autowired
    private Co2DailyDao co2DailyDao;

    @Autowired
    private HeatHourlyDao heatHourlyDao;

    @Autowired
    private HeatDailyDao heatDailyDao;

    @Autowired
    private HumidHourlyDao humidHourlyDao;

    @Autowired
    private HumidDailyDao humidDailyDao;

    @Autowired
    private IndoorPm25HourlyDao indoorPm25HourlyDao;

    @Autowired
    private IndoorPm25DailyDao indoorPm25DailyDao;

    @Autowired
    private ModeHourlyDao modeHourlyDao;

    @Autowired
    private ModeDailyDao modeDailyDao;

    @Autowired
    private PowerHourlyDao powerHourlyDao;

    @Autowired
    private PowerDailyDao powerDailyDao;

    @Autowired
    private TempHourlyDao tempHourlyDao;

    @Autowired
    private TempDailyDao tempDailyDao;

    @Autowired
    private VolumeHourlyDao volumeHourlyDao;

    @Autowired
    private VolumeDailyDao volumeDailyDao;

    private Logger logger = LoggerFactory.getLogger(MachineStatusServiceImpl.class);

    @Override
    public ResultData handleHourlyStatisticalData() {
        ResultData result = new ResultData();

        //1.从redis中获取前一小时的数据
        ResultData response = machineStatusRedisDao.queryHourlyStatus();
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            logger.debug("error happen when fetch machine status from redis");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        Map<String, Object> map = (HashMap) response.getData();
        //2.统计获取到的数据
        List<Object> statisticalDataList = new ArrayList<>();
        try {
            for (String machineId : map.keySet()) {
                Object queue = map.get(machineId);
                //若这个queue存了v1的status
                if (((LimitQueue<Object>) queue).getLast() instanceof MachineStatus) {
                    LinkedList<MachineStatus> list = new LinkedList<>();
                    for (int i = 0; i < ((LimitQueue) queue).size(); i++) {
                        list.add(((LimitQueue<MachineStatus>) queue).get(i));
                    }
                    V1MachineStatusHourly msh = countV1Status(list);
                    if (msh != null)
                        statisticalDataList.add(msh);
                }
                //若这个queue存了v2的status
                else if (((LimitQueue<Object>) queue).getLast() instanceof finley.gmair.model.machine.MachineStatus) {
                    LinkedList<finley.gmair.model.machine.MachineStatus> list = new LinkedList<>();
                    for (int i = 0; i < ((LimitQueue) queue).size(); i++) {
                        list.add(((LimitQueue<finley.gmair.model.machine.MachineStatus>) queue).get(i));
                    }
                    V2MachineStatusHourly msh = countV2Status(list);
                    if (msh != null)
                        statisticalDataList.add(msh);
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if (statisticalDataList.isEmpty()) {
            logger.debug("after statistical data,result is null");
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        //3.将统计结果插入到数据库
        List<JSONObject> dataList = statisticalDataList.stream().map(e -> JSONObject.parseObject(e.toString())).collect(Collectors.toList());
        List<IndoorPm25Hourly> pm25HourlyList = dataList.stream().map(e -> new IndoorPm25Hourly(e.getString("machineId"),e.getDouble("averagePm25"),e.getIntValue("maxPm25"),e.getIntValue("minPm25"))).collect(Collectors.toList());
        List<VolumeHourly> volumeHourlyList = dataList.stream().map(e -> new VolumeHourly(e.getString("machineId"),e.getDouble("averageVolume"),e.getIntValue("maxVolume"),e.getIntValue("minVolume"))).collect(Collectors.toList());
        List<TempHourly> tempHourlyList =  dataList.stream().map(e -> new TempHourly(e.getString("machineId"),e.getDouble("averageTemp"),e.getIntValue("maxTemp"),e.getIntValue("minTemp"))).collect(Collectors.toList());
        List<Co2Hourly>  co2HourlyList =  dataList.stream().filter(e -> e.getString("averageCo2")!= null).map(e -> new Co2Hourly(e.getString("machineId"),e.getDouble("averageCo2"),e.getIntValue("maxCo2"),e.getIntValue("minCo2"))).collect(Collectors.toList());
        List<HumidHourly> humidHourlyList =  dataList.stream().map(e -> new HumidHourly(e.getString("machineId"),e.getDouble("averageHumid"),e.getIntValue("maxHumid"),e.getIntValue("minHumid"))).collect(Collectors.toList());
        List<PowerHourly> powerHourlyList =  dataList.stream().map(e -> new PowerHourly(e.getString("machineId"),e.getIntValue("powerOnMinute"),e.getIntValue("powerOffMinute"))).collect(Collectors.toList());
        List<HeatHourly> heatHourlyList = dataList.stream().map(e -> new HeatHourly(e.getString("machineId"),e.getIntValue("heatOnMinute"),e.getIntValue("heatOffMinute"))).collect(Collectors.toList());
        List<ModeHourly> modeHourlyList = dataList.stream().map(e -> new ModeHourly(e.getString("machineId"),e.getIntValue("autoMinute"),e.getIntValue("manualMinute"),e.getIntValue("sleepMinute"))).collect(Collectors.toList());

        indoorPm25HourlyDao.insertBatch(pm25HourlyList);
        volumeHourlyDao.insertBatch(volumeHourlyList);
        tempHourlyDao.insertBatch(tempHourlyList);
        co2HourlyDao.insertBatch(co2HourlyList);
        humidHourlyDao.insertBatch(humidHourlyList);
        powerHourlyDao.insertBatch(powerHourlyList);
        heatHourlyDao.insertBatch(heatHourlyList);
        modeHourlyDao.insertBatch(modeHourlyList);
        result.setDescription("success to statistic data");
        return result;
    }


    private V1MachineStatusHourly countV1Status(LinkedList<MachineStatus> list) {
        int packetCountForOneMinute = 12;
        double averagePm25 = list.stream().mapToDouble(MachineStatus::getPm2_5).average().getAsDouble();
        int maxPm25 = list.stream().mapToInt(MachineStatus::getPm2_5).max().getAsInt();
        int minPm25 = list.stream().mapToInt(MachineStatus::getPm2_5).min().getAsInt();
        double averageVolume = list.stream().mapToDouble(MachineStatus::getVolume).average().getAsDouble();
        int maxVolume = list.stream().mapToInt(MachineStatus::getVolume).max().getAsInt();
        int minVolume = list.stream().mapToInt(MachineStatus::getVolume).min().getAsInt();
        double averageHumid = list.stream().mapToDouble(MachineStatus::getHumid).average().getAsDouble();
        int maxHumid = list.stream().mapToInt(MachineStatus::getHumid).max().getAsInt();
        int minHumid = list.stream().mapToInt(MachineStatus::getHumid).min().getAsInt();
        double averageTemp = list.stream().mapToDouble(MachineStatus::getTemp).average().getAsDouble();
        int maxTemp = list.stream().mapToInt(MachineStatus::getTemp).max().getAsInt();
        int minTemp = list.stream().mapToInt(MachineStatus::getTemp).min().getAsInt();
        int powerOffMinute = (int) list.stream().filter(a -> a.getPower() == 0).count() / packetCountForOneMinute;
        int powerOnMinute = (int) list.stream().filter(a -> a.getPower() == 1).count() / packetCountForOneMinute;
        int autoMinute = (int) list.stream().filter(a -> a.getMode() == 0).count() / packetCountForOneMinute;
        int manualMinute = (int) list.stream().filter(a -> a.getMode() == 1).count() / packetCountForOneMinute;
        int sleepMinute = (int) list.stream().filter(a -> a.getMode() == 2).count() / packetCountForOneMinute;
        int heatOffMinute = (int) list.stream().filter(a -> a.getHeat() == 0).count() / packetCountForOneMinute;
        int heatOnMinute = (int) list.stream().filter(a -> a.getHeat() == 1).count() / packetCountForOneMinute;
        String machineId = list.get(0).getUid();
        V1MachineStatusHourly msh = new V1MachineStatusHourly(machineId, averagePm25, maxPm25, minPm25, averageVolume, maxVolume, minVolume, averageTemp, maxTemp, minTemp, averageHumid, maxHumid, minHumid, powerOnMinute, powerOffMinute, autoMinute, manualMinute, sleepMinute, heatOnMinute, heatOffMinute);
        return msh;
    }

    private V2MachineStatusHourly countV2Status(LinkedList<finley.gmair.model.machine.MachineStatus> list) {
        int packetCountForOneMinute = 2;
        double averagePm25 = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getPm2_5).average().getAsDouble();
        int maxPm25 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getPm2_5).max().getAsInt();
        int minPm25 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getPm2_5).min().getAsInt();
        double averageVolume = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getVolume).average().getAsDouble();
        int maxVolume = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getVolume).max().getAsInt();
        int minVolume = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getVolume).min().getAsInt();
        double averageTemp = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getTemp).average().getAsDouble();
        int maxTemp = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getTemp).max().getAsInt();
        int minTemp = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getTemp).min().getAsInt();
        double averageHumid = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getHumid).average().getAsDouble();
        int maxHumid = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getHumid).max().getAsInt();
        int minHumid = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getHumid).min().getAsInt();
        double averageCo2 = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getCo2).average().getAsDouble();
        int maxCo2 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getCo2).max().getAsInt();
        int minCo2 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getCo2).min().getAsInt();
        int powerOffMinute = (int) list.stream().filter(a -> a.getPower() == 0).count() / packetCountForOneMinute;
        int powerOnMinute = (int) list.stream().filter(a -> a.getPower() == 1).count() / packetCountForOneMinute;
        int autoMinute = (int) list.stream().filter(a -> a.getMode() == 0).count() / packetCountForOneMinute;
        int manualMinute = (int) list.stream().filter(a -> a.getMode() == 1).count() / packetCountForOneMinute;
        int sleepMinute = (int) list.stream().filter(a -> a.getMode() == 2).count() / packetCountForOneMinute;
        int heatOffMinute = (int) list.stream().filter(a -> a.getHeat() == 0).count() / packetCountForOneMinute;
        int heatOnMinute = (int) list.stream().filter(a -> a.getHeat() == 1).count() / packetCountForOneMinute;
        String machineId = list.get(0).getUid();
        V2MachineStatusHourly msh = new V2MachineStatusHourly(machineId, averagePm25, maxPm25, minPm25, averageVolume, maxVolume, minVolume, averageTemp, maxTemp, minTemp, averageHumid, maxHumid, minHumid, averageCo2, maxCo2, minCo2, powerOnMinute, powerOffMinute, autoMinute, manualMinute, sleepMinute, heatOnMinute, heatOffMinute);
        return msh;
    }

    @Override
    public ResultData handleDailyStatisticalData() {
        ResultData result = new ResultData();
        try {
            LocalDate lastDay = LocalDateTime.now().minusDays(1).toLocalDate();
            LocalDate today = LocalDateTime.now().toLocalDate();
            Map<String, Object> condition = new HashMap<>();
            condition.put("createTimeGTE", lastDay);
            condition.put("createTimeLT", today);
            condition.put("blockFlag", 0);

            //查过去24小时的co2，并统计到daily表中
            ResultData response = co2HourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<Co2Hourly> list = (List<Co2Hourly>) response.getData();
                Map<String, Double> averageCo2Map = list.stream().collect(Collectors.groupingBy(Co2Hourly::getMachineId, Collectors.averagingDouble(Co2Hourly::getAverageCo2)));
                Map<String, Optional<Co2Hourly>> maxCo2Map = list.stream().collect(Collectors.groupingBy(Co2Hourly::getMachineId, Collectors.maxBy(Comparator.comparing(Co2Hourly::getMaxCo2))));
                Map<String, Optional<Co2Hourly>> minCo2Map = list.stream().collect(Collectors.groupingBy(Co2Hourly::getMachineId, Collectors.minBy(Comparator.comparing(Co2Hourly::getMinCo2))));
                List<Co2Hourly> dailyData = averageCo2Map.entrySet().stream().map(e -> new Co2Hourly(e.getKey(), e.getValue(), maxCo2Map.get(e.getKey()).map(Co2Hourly::getMaxCo2).get(), minCo2Map.get(e.getKey()).map(Co2Hourly::getMinCo2).get())).collect(Collectors.toList());
                co2DailyDao.insertBatch(dailyData);
            }
            //heat
            response = heatHourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<HeatHourly> list = (List<HeatHourly>) response.getData();
                Map<String, Integer> heatOnMinute = list.stream().collect(Collectors.groupingBy(HeatHourly::getMachineId, Collectors.summingInt(HeatHourly::getHeatOnMinute)));
                Map<String, Integer> heatOffMinute = list.stream().collect(Collectors.groupingBy(HeatHourly::getMachineId, Collectors.summingInt(HeatHourly::getHeatOffMinute)));
                List<HeatHourly> dailyData = heatOnMinute.entrySet().stream().map(e -> new HeatHourly(e.getKey(), e.getValue(), heatOffMinute.get(e.getKey()).intValue())).collect(Collectors.toList());
                heatDailyDao.insertBatch(dailyData);
            }
            //humid
            response = humidHourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<HumidHourly> list = (List<HumidHourly>) response.getData();
                Map<String, Double> averageHumidMap = list.stream().collect(Collectors.groupingBy(HumidHourly::getMachineId, Collectors.averagingDouble(HumidHourly::getAverageHumid)));
                Map<String, Optional<HumidHourly>> maxHumidMap = list.stream().collect(Collectors.groupingBy(HumidHourly::getMachineId, Collectors.maxBy(Comparator.comparing(HumidHourly::getMaxHumid))));
                Map<String, Optional<HumidHourly>> minHumidMap = list.stream().collect(Collectors.groupingBy(HumidHourly::getMachineId, Collectors.minBy(Comparator.comparing(HumidHourly::getMinHumid))));
                List<HumidHourly> dailyData = averageHumidMap.entrySet().stream().map(e -> new HumidHourly(e.getKey(), e.getValue(), maxHumidMap.get(e.getKey()).map(HumidHourly::getMaxHumid).get(), minHumidMap.get(e.getKey()).map(HumidHourly::getMinHumid).get())).collect(Collectors.toList());
                humidDailyDao.insertBatch(dailyData);
            }
            //indoor_pm25
            response = indoorPm25HourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<IndoorPm25Hourly> list = (List<IndoorPm25Hourly>) response.getData();
                Map<String, Double> averageIndoorPm25Map = list.stream().collect(Collectors.groupingBy(IndoorPm25Hourly::getMachineId, Collectors.averagingDouble(IndoorPm25Hourly::getAveragePm25)));
                Map<String, Optional<IndoorPm25Hourly>> maxIndoorPm25Map = list.stream().collect(Collectors.groupingBy(IndoorPm25Hourly::getMachineId, Collectors.maxBy(Comparator.comparing(IndoorPm25Hourly::getMaxPm25))));
                Map<String, Optional<IndoorPm25Hourly>> minIndoorPm25Map = list.stream().collect(Collectors.groupingBy(IndoorPm25Hourly::getMachineId, Collectors.minBy(Comparator.comparing(IndoorPm25Hourly::getMinPm25))));
                List<IndoorPm25Hourly> dailyData = averageIndoorPm25Map.entrySet().stream().map(e -> new IndoorPm25Hourly(e.getKey(), e.getValue(), maxIndoorPm25Map.get(e.getKey()).map(IndoorPm25Hourly::getMaxPm25).get(), minIndoorPm25Map.get(e.getKey()).map(IndoorPm25Hourly::getMinPm25).get())).collect(Collectors.toList());
                indoorPm25DailyDao.insertBatch(dailyData);
            }
            //mode
            response = modeHourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<ModeHourly> list = (List<ModeHourly>) response.getData();
                Map<String, Integer> autoMinute = list.stream().collect(Collectors.groupingBy(ModeHourly::getMachineId, Collectors.summingInt(ModeHourly::getAutoMinute)));
                Map<String, Integer> manualMinute = list.stream().collect(Collectors.groupingBy(ModeHourly::getMachineId, Collectors.summingInt(ModeHourly::getManualMinute)));
                Map<String, Integer> sleepMinute = list.stream().collect(Collectors.groupingBy(ModeHourly::getMachineId, Collectors.summingInt(ModeHourly::getSleepMinute)));
                List<ModeHourly> dailyData = autoMinute.entrySet().stream().map(e -> new ModeHourly(e.getKey(), e.getValue(), manualMinute.get(e.getKey()).intValue(), sleepMinute.get(e.getKey()).intValue())).collect(Collectors.toList());
                modeDailyDao.insertBatch(dailyData);
            }
            //power
            response = powerHourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<PowerHourly> list = (List<PowerHourly>) response.getData();
                Map<String, Integer> powerOnMinute = list.stream().collect(Collectors.groupingBy(PowerHourly::getMachineId, Collectors.summingInt(PowerHourly::getPowerOnMinute)));
                Map<String, Integer> powerOffMinute = list.stream().collect(Collectors.groupingBy(PowerHourly::getMachineId, Collectors.summingInt(PowerHourly::getPowerOffMinute)));
                List<PowerHourly> dailyData = powerOnMinute.entrySet().stream().map(e -> new PowerHourly(e.getKey(), e.getValue(), powerOffMinute.get(e.getKey()).intValue())).collect(Collectors.toList());
                powerDailyDao.insertBatch(dailyData);
            }
            //temp
            response = tempHourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<TempHourly> list = (List<TempHourly>) response.getData();
                Map<String, Double> averageTempMap = list.stream().collect(Collectors.groupingBy(TempHourly::getMachineId, Collectors.averagingDouble(TempHourly::getAverageTemp)));
                Map<String, Optional<TempHourly>> maxTempMap = list.stream().collect(Collectors.groupingBy(TempHourly::getMachineId, Collectors.maxBy(Comparator.comparing(TempHourly::getMaxTemp))));
                Map<String, Optional<TempHourly>> minTempMap = list.stream().collect(Collectors.groupingBy(TempHourly::getMachineId, Collectors.minBy(Comparator.comparing(TempHourly::getMinTemp))));
                List<TempHourly> dailyData = averageTempMap.entrySet().stream().map(e -> new TempHourly(e.getKey(), e.getValue(), maxTempMap.get(e.getKey()).map(TempHourly::getMaxTemp).get(), minTempMap.get(e.getKey()).map(TempHourly::getMinTemp).get())).collect(Collectors.toList());
                tempDailyDao.insertBatch(dailyData);
            }

            //volume
            response = volumeHourlyDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<VolumeHourly> list = (List<VolumeHourly>) response.getData();
                Map<String, Double> averageVolumeMap = list.stream().collect(Collectors.groupingBy(VolumeHourly::getMachineId, Collectors.averagingDouble(VolumeHourly::getAverageVolume)));
                Map<String, Optional<VolumeHourly>> maxVolumeMap = list.stream().collect(Collectors.groupingBy(VolumeHourly::getMachineId, Collectors.maxBy(Comparator.comparing(VolumeHourly::getMaxVolume))));
                Map<String, Optional<VolumeHourly>> minVolumeMap = list.stream().collect(Collectors.groupingBy(VolumeHourly::getMachineId, Collectors.minBy(Comparator.comparing(VolumeHourly::getMinVolume))));
                List<VolumeHourly> dailyData = averageVolumeMap.entrySet().stream().map(e -> new VolumeHourly(e.getKey(), e.getValue(), maxVolumeMap.get(e.getKey()).map(VolumeHourly::getMaxVolume).get(), minVolumeMap.get(e.getKey()).map(VolumeHourly::getMinVolume).get())).collect(Collectors.toList());
                volumeDailyDao.insertBatch(dailyData);
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return result;
    }
}
