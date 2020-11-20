package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.air.CityWeatherCondition;
import finley.gmair.model.air.MojiRecord;
import finley.gmair.model.air.MojiToken;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.service.*;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@PropertySource("classpath:moji.properties")
public class CityWeatherConditionServiceImpl implements CityWeatherConditionService {
    private Logger logger = LoggerFactory.getLogger(CityWeatherConditionServiceImpl.class);

    @Value("${base}")
    private String base;

    @Autowired
    private LocationFeign locationFeign;

    @Autowired
    private MojiTokenService mojiTokenService;

    private List<MojiRecord> list;

    private Map<String, Province> provinces;

    private Map<String, City> cities;

    private Map<String, District> districts;

    private MojiToken mojiToken;

    @PostConstruct
    public void init() {
        try {
            File file = new File(base);
            list = MojiRecordUtil.list(file);
            logger.info("moji record: " + list.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        ResultData response = locationFeign.province();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("获取省份信息失败");
            return;
        }
        provinces = new HashMap<>();
        cities = new HashMap<>();
        districts = new HashMap<>();
        JSONArray provinceArr = JSON.parseArray(JSON.toJSONString(response.getData()));
        for (int i = 0; i < provinceArr.size(); i++) {
            JSONObject item = provinceArr.getJSONObject(i);
            String provinceId = item.getString("provinceId");
            String provinceName = item.getString("provinceName");
            String provincePinyin = item.getString("provincePinyin");
            double longitude = item.getDouble("longitude");
            double latitude = item.getDouble("latitude");
            Province province = new Province(provinceId, provinceName, provincePinyin, longitude, latitude);
            provinces.put(provinceId, province);
            response = locationFeign.city(provinceId);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("Fail to obtain city list under: " + provinceId);
                continue;
            }
            JSONArray cityArr = JSON.parseArray(JSON.toJSONString(response.getData()));
            for (int j = 0; j < cityArr.size(); j++) {
                item = cityArr.getJSONObject(j);
                String cityId = item.getString("cityId");
                String cityName = item.getString("cityName");
                String cityPinyin = item.getString("cityPinyin");
                longitude = item.getDouble("longitude");
                latitude = item.getDouble("latitude");
                City city = new City(cityId, cityName, cityPinyin, longitude, latitude);
                cities.put(cityId, city);
                response = locationFeign.district(cityId);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("Fail to obtain district list under:  " + cityId);
                    continue;
                }
                JSONArray districtArr = JSON.parseArray(JSON.toJSONString(response.getData()));
                for (int k = 0; k < districtArr.size(); k++) {
                    item = districtArr.getJSONObject(k);
                    String districtId = item.getString("districtId");
                    String districtName = item.getString("districtName");
                    District district = new District(districtId, districtName);
                    districts.put(districtId, district);
                }
            }
        }
        logger.info("province: " + provinces.size() + ", city: " + cities.size() + ", district: " + districts.size());
        rank();
    }

    private void rank() {
        new Thread(() -> {
            Map<String, CityWeatherCondition> map = new HashMap<>();
            for (Map.Entry<String, Province> entry : provinces.entrySet()) {
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
            for (Map.Entry<String, City> entry : cities.entrySet()) {
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
                for (Map.Entry<String, District> entry : districts.entrySet()) {
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
        }).start();
    }


    @Override
    public ResultData obtain() {
        ResultData result = new ResultData();
        MojiToken mojiToken = ((List<MojiToken>) selectToken().getData()).get(0);
        logger.info("Active moji token: " + JSON.toJSONString(mojiToken));
        this.mojiToken = mojiToken;
        rank();
        return result;
    }

    private CityWeatherCondition fetch(String lid, int cityId) {
        StringBuffer stringBuffer = new StringBuffer(mojiToken.getUrl()).append("?cityId=").append(cityId);
        String url = stringBuffer.toString();
        String subUrl = url.substring("https://api.mojicb.com".length(), url.indexOf("?"));
        logger.info(url);
        Long timeValue = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String timeStamp = timeValue.toString();
        String nonce = UUID.randomUUID().toString();
        String signature = Encryption.md5(mojiToken.getPassword() + "\n" + timeStamp + "\n" + nonce + "\n" + subUrl);
        Map<String, String> map = new HashMap<>();
        map.put("X-AC-Token", mojiToken.getToken());
        map.put("X-Date", timeStamp);
        map.put("X-AC-Nonce", nonce);
        map.put("X-AC-Signature", signature);
        String result = HttpDeal.getResponse(url, map);
        logger.info("Request params: " + JSON.toJSONString(map));
        CityWeatherCondition weatherCondition = interpret(lid, result);
        return weatherCondition;
    }

    private CityWeatherCondition fetch(String lid, double longitude, double latitude) {
//        MojiToken mojiToken = ((List<MojiToken>) selectToken().getData()).get(0);
        StringBuffer stringBuffer = new StringBuffer(mojiToken.getUrl()).append("?lat=").append(latitude).append("&lon=").append(longitude);
        String url = stringBuffer.toString();
        String subUrl = url.substring("https://api.mojicb.com".length(), url.indexOf("?"));
        logger.info(url);
        Long timeValue = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String timeStamp = timeValue.toString();
        String nonce = UUID.randomUUID().toString();
        String signature = Encryption.md5(mojiToken.getPassword() + "\n" + timeStamp + "\n" + nonce + "\n" + subUrl);
        Map<String, String> map = new HashMap<>();
        map.put("X-AC-Token", mojiToken.getToken());
        map.put("X-Date", timeStamp);
        map.put("X-AC-Nonce", nonce);
        map.put("X-AC-Signature", signature);
        String result = HttpDeal.getResponse(url, map);
        logger.info("Request params: " + JSON.toJSONString(map));
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
        CityWeatherCondition weatherCondition = new CityWeatherCondition();
        try {
            String condition = data.getString("value");
            String conditionId = data.getString("conditionId");
            String humidity = data.getString("humidity");
            String icon = data.getString("icon");
            String pressure = data.getString("pressure");
            String realFeel = data.getString("realFeel");
            String sunRise = data.getString("sunRise");
            String sunSet = data.getString("sunSet");
            String temp = data.getString("temp");
            String tips = data.getString("tips");
            String updatetime = data.getString("updatetime");
            String uvi = data.getString("uvi");
            String vis = data.getString("vis");
            String windDegrees = data.getString("windDegrees");
            String windDir = data.getString("windDir");
            String windLevel = data.getString("windLevel");
            String windSpeed = data.getString("windSpeed");
            weatherCondition.setCityId(id);
            weatherCondition.setCondition(condition);
            weatherCondition.setConditionId(conditionId);
            weatherCondition.setHumidity(humidity);
            weatherCondition.setIcon(icon);
            weatherCondition.setPressure(pressure);
            weatherCondition.setRealFeel(realFeel);
            weatherCondition.setSunRise(sunRise);
            weatherCondition.setSunSet(sunSet);
            weatherCondition.setTemp(temp);
            weatherCondition.setTips(tips);
            weatherCondition.setUpdatetime(updatetime);
            weatherCondition.setUvi(uvi);
            weatherCondition.setVis(vis);
            weatherCondition.setWindDegrees(windDegrees);
            weatherCondition.setWindDir(windDir);
            weatherCondition.setWindLevel(windLevel);
            weatherCondition.setWindSpeed(windSpeed);
            weatherCondition.setRecordTime(new Timestamp(System.currentTimeMillis() / (3600000) * 3600000));
        } catch (Exception e) {
            return null;
        }
        return weatherCondition;
    }

    private MojiRecord locate(String district) {
        logger.info("district: " + district + ", size: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            MojiRecord record = list.get(i);
            if (record.getName().contains(district)) {
                return record;
            }
        }
        return null;
    }

}



