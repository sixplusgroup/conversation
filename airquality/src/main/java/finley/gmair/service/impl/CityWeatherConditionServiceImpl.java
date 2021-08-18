package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.CityWeatherConditionDao;
import finley.gmair.model.air.CityWeatherCondition;
import finley.gmair.model.air.MojiRecord;
import finley.gmair.model.air.MojiToken;
import finley.gmair.model.air.WeatherCondition;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.pool.CrawlerPool;
import finley.gmair.service.CityWeatherConditionService;
import finley.gmair.service.MojiLocationService;
import finley.gmair.service.MojiTokenService;
import finley.gmair.service.WeatherConditionCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CityWeatherConditionServiceImpl implements CityWeatherConditionService {
    private Logger logger = LoggerFactory.getLogger(CityWeatherConditionServiceImpl.class);

    @Autowired
    private MojiTokenService mojiTokenService;

    @Resource
    private MojiLocationService mojiLocationService;

    @Autowired
    private WeatherConditionCacheService weatherConditionCacheService;

    @Autowired
    private CityWeatherConditionDao cityWeatherConditionDao;

    private void rank() {
        CrawlerPool.getCrawlerPool().execute(() -> {
            Map<String, CityWeatherCondition> map = new HashMap<>();
            for (Map.Entry<String, Province> entry : mojiLocationService.getProvinces().entrySet()) {
                String provinceId = entry.getKey();
                Province province = entry.getValue();
                CityWeatherCondition weatherCondition = fetch(provinceId, province.getLongitude(), province.getLatitude());
                if (weatherCondition == null) {
                    continue;
                }
                logger.info("Province weather condition: " + JSON.toJSONString(weatherCondition));
                map.put(provinceId, weatherCondition);
            }
            logger.info("province weather condition complete");
            for (Map.Entry<String, City> entry : mojiLocationService.getCities().entrySet()) {
                String cityId = entry.getKey();
                City city = entry.getValue();
                CityWeatherCondition weatherCondition = fetch(cityId, city.getLongitude(), city.getLatitude());
                if (weatherCondition == null) {
                    continue;
                }
                logger.info("City weather condition: " + JSON.toJSONString(weatherCondition));
                map.put(cityId, weatherCondition);
            }
            logger.info("city weather condition complete");
            try {
                for (Map.Entry<String, District> entry : mojiLocationService.getDistricts().entrySet()) {
                    String districtId = entry.getKey();
                    District district = entry.getValue();
                    MojiRecord record = locate(district.getDistrictName());
                    if (record == null) {
                        logger.info("Fail to find district: " + districtId);
                        continue;
                    }
                    logger.info("district fid: " + record.getFid());
                    CityWeatherCondition weatherCondition = fetch(districtId, record.getFid());
                    if (weatherCondition == null) {
                        continue;
                    }
                    logger.info("District weather condition: " + JSON.toJSONString(weatherCondition));
                    map.put(districtId, weatherCondition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("district weather condition complete");

            List<CityWeatherCondition> data = map.values().stream().collect(Collectors.toList());
            insertCityWeatherConditionDetail(data);
        });
    }


    @Override
    public ResultData obtain() {
        ResultData result = new ResultData();
        MojiToken mojiToken = ((List<MojiToken>) selectToken().getData()).get(0);
        logger.info("Active moji token: " + JSON.toJSONString(mojiToken));
        rank();
        return result;
    }

    private CityWeatherCondition fetch(String lid, int cityId) {
        String result = mojiLocationService.fetch(cityId);
        CityWeatherCondition weatherCondition = interpret(lid, result);
        return weatherCondition;
    }

    private CityWeatherCondition fetch(String lid, double longitude, double latitude) {
        String result = mojiLocationService.fetch(longitude, latitude);
        CityWeatherCondition weatherCondition = interpret(lid, result);
        return weatherCondition;
    }

    ResultData selectToken() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 2);
        return mojiTokenService.fetch(condition);
    }

    public CityWeatherCondition interpret(String id, String result) {
        JSONObject json = JSON.parseObject(result);
        int code = json.getInteger("code");
        // 先解析相应数据的响应代码，目前正确返回的数据code为200
        if (code != 200) {
            logger.error(json.toJSONString());
            return null;
        }
        if (!json.containsKey("data")) {
            logger.error("Current result is invalid: " + json.toJSONString());
            return null;
        }
        if (!json.getJSONObject("data").containsKey("condition")) {
            logger.error("Current data is invalid: " + json.toJSONString());
            return null;
        }
        JSONObject data = json.getJSONObject("data").getJSONObject("condition");
        CityWeatherCondition weatherCondition = null;
        try {
            String condition = data.getString("condition");
            int conditionId = data.getIntValue("conditionId");
            int humidity = data.getIntValue("humidity");
            int icon = data.getIntValue("icon");
            int pressure = data.getIntValue("pressure");
            int realFeel = data.getIntValue("realFeel");
            Timestamp sunRise = data.getTimestamp("sunRise");
            Timestamp sunSet = data.getTimestamp("sunSet");
            int temp = data.getIntValue("temp");
            String tips = data.getString("tips");
            int uvi = data.getIntValue("uvi");
            int vis = data.getIntValue("vis");
            int windDegrees = data.getIntValue("windDegrees");
            String windDir = data.getString("windDir");
            int windLevel = data.getIntValue("windLevel");
            double windSpeed = data.getDouble("windSpeed");
            Timestamp updateTime = data.getTimestamp("updatetime");
            weatherCondition = new CityWeatherCondition(id, condition, conditionId, humidity, icon, pressure, realFeel, sunRise, sunSet, temp, tips, uvi, vis, windDegrees, windDir, windLevel, windSpeed, updateTime);
        } catch (Exception e) {
            return null;
        }
        return weatherCondition;
    }

    private void insertCityWeatherConditionDetail(List<CityWeatherCondition> weatherConditionList) {
        // step 1: update cache
        try {
            for (CityWeatherCondition cityWeatherCondition : weatherConditionList) {
                weatherConditionCacheService.generate(cityWeatherCondition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // step 2: update database
        if (weatherConditionList.isEmpty()) {
            return;
        }
        Timestamp timestamp = weatherConditionList.get(0).getUpdateTime();
        Map<String, Object> condition = new HashMap();
        condition.put("updateTime", timestamp);
        condition.put("blockFlag", false);
        ResultData response = cityWeatherConditionDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            cityWeatherConditionDao.insertBatch(weatherConditionList);
        }
    }

    private MojiRecord locate(String district) {
        return mojiLocationService.locate(district);
    }

}



