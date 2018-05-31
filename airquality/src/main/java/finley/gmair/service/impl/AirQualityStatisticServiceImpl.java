package finley.gmair.service.impl;

import finley.gmair.dao.AirQualityStatisticDao;
import finley.gmair.dao.CityAirQualityDao;
import finley.gmair.model.air.CityAirQualityStatistic;
import finley.gmair.service.AirQualityStatisticService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityAirPm25Vo;
import finley.gmair.vo.air.CityAirQualityStatisticVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AirQualityStatisticServiceImpl implements AirQualityStatisticService{

    @Autowired
    private AirQualityStatisticDao airQualityStatisticDao;

    @Autowired
    private CityAirQualityDao cityAirQualityDao;

    @Override
    public ResultData handleAirQualityHourlyStatistic() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        Timestamp lastHour = new Timestamp((System.currentTimeMillis() - 300000) / (3600000) * 3600000);
        condition.put("recordTime", lastHour);
        ResultData response = cityAirQualityDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityAirPm25Vo> list = (List<CityAirPm25Vo>) response.getData();
            Map<String, Double> cityAqiMap = list.stream().collect(
                    Collectors.groupingBy(CityAirPm25Vo::getCityId,
                            Collectors.averagingDouble(CityAirPm25Vo::getPm25)));

            List<CityAirQualityStatistic> airQualityStatisticList = cityAqiMap.entrySet().stream()
                    .map(e-> new CityAirQualityStatistic(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            if (!airQualityStatisticList.isEmpty())
                response = airQualityStatisticDao.insertHourlyData(airQualityStatisticList);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            LocalDateTime localDateTime = lastHour.toLocalDateTime();
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no hourly data in city air quality, " + localDateTime.toString());
        } else  {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData handleAirQualityDailyStatistic() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        LocalDate lastDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate().minusDays(1);

        condition.put("createTimeGTE", LocalDateTime.of(lastDate, LocalTime.MIN));
        condition.put("createTimeLTE", LocalDateTime.of(lastDate, LocalTime.MAX));

        ResultData response = airQualityStatisticDao.fetchHourlyData(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityAirQualityStatisticVo> list = (List<CityAirQualityStatisticVo>) response.getData();
            Map<String, Double> cityAqiMap = list.stream().collect(
                    Collectors.groupingBy(CityAirQualityStatisticVo::getCityId,
                            Collectors.averagingDouble(CityAirQualityStatisticVo::getPm25)));

            List<CityAirQualityStatistic> airQualityStatisticList = cityAqiMap.entrySet().stream()
                    .map(e-> new CityAirQualityStatistic(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            if (!airQualityStatisticList.isEmpty())
                response = airQualityStatisticDao.insertDailyData(airQualityStatisticList);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no daily data in city air quality, " + lastDate.toString());
        } else  {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData handleAirQualityMonthlyStatistic() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        LocalDateTime localDateTime = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        LocalDateTime lastMonth = localDateTime.withMonth(localDateTime.getMonth().minus(1).getValue());

        condition.put("createTimeGTE", localDateTime);
        condition.put("createTimeLTE", localDateTime);
        ResultData response = airQualityStatisticDao.fetchDailyData(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityAirQualityStatisticVo> list = (List<CityAirQualityStatisticVo>) response.getData();
            Map<String, Double> cityAqiMap = list.stream().collect(
                    Collectors.groupingBy(CityAirQualityStatisticVo::getCityId,
                            Collectors.averagingDouble(CityAirQualityStatisticVo::getPm25)));

            List<CityAirQualityStatistic> airQualityStatisticList = cityAqiMap.entrySet().stream()
                    .map(e-> new CityAirQualityStatistic(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            if (!airQualityStatisticList.isEmpty())
                response = airQualityStatisticDao.insertMonthlyData(airQualityStatisticList);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no monthly data in city air quality, " + lastMonth.toString());
        } else  {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchAirQualityHourlyStatistic(Map<String, Object> condition) {
        return airQualityStatisticDao.fetchHourlyData(condition);
    }

    @Override
    public ResultData fetchAirQualityDailyStatistic(Map<String, Object> condition) {
        return airQualityStatisticDao.fetchDailyData(condition);
    }

    @Override
    public ResultData fetchAirQualityMonthlyStatistic(Map<String, Object> condition) {
        return airQualityStatisticDao.fetchMonthlyData(condition);
    }
}
