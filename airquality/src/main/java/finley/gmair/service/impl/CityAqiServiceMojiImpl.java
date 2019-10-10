//package finley.gmair.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import finley.gmair.dao.CityAirQualityDao;
//import finley.gmair.model.air.CityAirQuality;
//import finley.gmair.model.air.MojiRecord;
//import finley.gmair.model.district.City;
//import finley.gmair.model.district.District;
//import finley.gmair.model.district.Province;
//import finley.gmair.service.AirQualityCacheService;
//import finley.gmair.service.CityAQIService;
//import finley.gmair.service.ObscureCityService;
//import finley.gmair.service.ProvinceAirQualityService;
//import finley.gmair.service.feign.LocationFeign;
//import finley.gmair.util.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.File;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @ClassName: CityAqiServiceMojiImpl
// * @Description: TODO
// * @Author fan
// * @Date 2019/10/10 12:09 AM
// */
//@Service
//@PropertySource("classpath:moji.properties")
//public class CityAqiServiceMojiImpl implements CityAQIService {
//    private Logger logger = LoggerFactory.getLogger(CityAqiServiceMojiImpl.class);
//
//    @Autowired
//    private ObscureCityService obscureCityService;
//
//    @Autowired
//    private ProvinceAirQualityService provinceAirQualityService;
//
//    @Autowired
//    private CityAirQualityDao cityAirQualityDao;
//
//    @Autowired
//    private AirQualityCacheService airQualityCacheService;
//
//    @Value("${token}")
//    private String token;
//
//    @Value("${password}")
//    private String password;
//
//    @Value("${url}")
//    private String url;
//
//    @Value("classpath:cities.xml")
//    private Resource records;
//
//    @Autowired
//    private LocationFeign locationFeign;
//
//    private List<MojiRecord> list;
//
//    private Map<String, Province> provinces;
//
//    private Map<String, City> cities;
//
//    private Map<String, District> districts;
//
//    @PostConstruct
//    public void init() {
//        try {
//            File file = records.getFile();
//            list = MojiRecordUtil.list(file);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        ResultData response = locationFeign.province();
//        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//            logger.error("获取省份信息失败");
//            return;
//        }
//        JSONArray provinceArr = JSON.parseArray(JSON.toJSONString(response.getData()));
//        for (int i = 0; i < provinceArr.size(); i++) {
//            JSONObject item = provinceArr.getJSONObject(i);
//            String provinceId = item.getString("provinceId");
//            String provinceName = item.getString("provinceName");
//            String provincePinyin = item.getString("provincePinyin");
//            double longitude = item.getDouble("longitude");
//            double latitude = item.getDouble("latitude");
//            Province province = new Province(provinceId, provinceName, provincePinyin, longitude, latitude);
//            provinces.put(provinceId, province);
//            response = locationFeign.city(provinceId);
//            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                logger.error("未能获取省份: " + provinceName + "下的城市信息");
//                continue;
//            }
//            JSONArray cityArr = JSON.parseArray(JSON.toJSONString(response.getData()));
//            for (int j = 0; j < cityArr.size(); j++) {
//                item = cityArr.getJSONObject(j);
//                String cityId = item.getString("cityId");
//                String cityName = item.getString("cityName");
//                String cityPinyin = item.getString("cityPinyin");
//                longitude = item.getDouble("longitude");
//                latitude = item.getDouble("latitude");
//                City city = new City(cityId, cityName, cityPinyin, longitude, latitude);
//                cities.put(cityId, city);
//                response = locationFeign.district(cityId);
//                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                    logger.error("未能获取城市: " + cityName + "下的区县信息");
//                    continue;
//                }
//                JSONArray districtArr = JSON.parseArray(JSON.toJSONString(response.getData()));
//                for (int k = 0; k < districtArr.size(); k++) {
//                    item = districtArr.getJSONObject(k);
//                    String districtId = item.getString("districtId");
//                    String districtName = item.getString("districtName");
//                    District district = new District(districtId, districtName);
//                    districts.put(districtId, district);
//                }
//            }
//        }
//        rank();
//    }
//
//    public void rank() {
//        Map<String, CityAirQuality> map = new HashMap<>();
//        for (Map.Entry<String, Province> entry : provinces.entrySet()) {
//            String provinceId = entry.getKey();
//            Province province = entry.getValue();
//            CityAirQuality quality = fetch(provinceId, province.getLongitude(), province.getLatitude());
//            logger.info("Province aqi: " + JSON.toJSONString(quality));
//            map.put(provinceId, quality);
//        }
//        for (Map.Entry<String, City> entry : cities.entrySet()) {
//            String cityId = entry.getKey();
//            City city = entry.getValue();
//            CityAirQuality quality = fetch(cityId, city.getLongitude(), city.getLatitude());
//            logger.info("City aqi: " + JSON.toJSONString(quality));
//            map.put(cityId, quality);
//        }
//        for (Map.Entry<String, District> entry : districts.entrySet()) {
//            String districtId = entry.getKey();
//            District district = entry.getValue();
//            MojiRecord record = locate(district.getDistrictName());
//            CityAirQuality quality = fetch(districtId, record.getFid());
//            logger.info("District aqi: " + JSON.toJSONString(quality));
//            map.put(districtId, quality);
//        }
//        List<CityAirQuality> data = map.values().stream().collect(Collectors.toList());
//        insertCityAqiDetail(data);
//    }
//
//    @Override
//    public ResultData obtain() {
//        ResultData result = new ResultData();
//        rank();
//        return result;
//    }
//
//    //区使用该API进行获取
//    private CityAirQuality fetch(String lid, int cityId) {
//        String timestamp = "0";
//        StringBuffer sb = new StringBuffer(url).append("?timestamp=").append(timestamp).append("&cityId=").append(cityId).append("&token=").append(token).append("&key=").append(Encryption.md5(password + timestamp));
//        logger.info(sb.toString());
//        String result = HttpDeal.getResponse(sb.toString());
//        CityAirQuality quality = interpret(lid, result);
//        return quality;
//    }
//
//    //省份和城市使用该API进行获取
//    private CityAirQuality fetch(String lid, double longitude, double latitude) {
//        String timestamp = "0";
//        StringBuffer sb = new StringBuffer(url).append("?timestamp=").append(timestamp).append("&lat=").append(latitude).append("&lon=").append(longitude).append("&token=").append(token).append("&key=").append(Encryption.md5(password + timestamp));
//        logger.info(sb.toString());
//        String result = HttpDeal.getResponse(sb.toString());
//        CityAirQuality quality = interpret(lid, result);
//        return quality;
//    }
//
//    public CityAirQuality interpret(String id, String result) {
//        JSONObject json = JSON.parseObject(result);
//        int code = json.getInteger("code");
//        if (code != 0) {
//            logger.error(json.getString("msg"));
//        }
//        JSONObject data = json.getJSONObject("data").getJSONObject("aqi");
//        double aqi = data.getDouble("value");
//        String level = data.getString("level");
//        double pm2_5 = data.getDouble("pm25C");
//        double pm10 = data.getDouble("pm10C");
//        double co = data.getDouble("coC");
//        double no2 = data.getDouble("no2C");
//        double o3 = data.getDouble("o3C");
//        double so2 = data.getDouble("so2C");
//        CityAirQuality quality = new CityAirQuality();
//        quality.setCityId(id);
//        quality.setAqi(aqi);
//        quality.setAqiLevel(level);
//        quality.setPm2_5(pm2_5);
//        quality.setPm10(pm10);
//        quality.setCo(co);
//        quality.setNo2(no2);
//        quality.setO3(o3);
//        quality.setSo2(so2);
//        return quality;
//    }
//
//    private void insertCityAqiDetail(List<CityAirQuality> airQualityList) {
//        // step 1: update cache
//        try {
//            for (CityAirQuality cityAirQuality : airQualityList) {
//                airQualityCacheService.generate(cityAirQuality);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // step 2: update database
//        if (airQualityList.isEmpty())
//            return;
//        Timestamp timestamp = airQualityList.get(0).getRecordTime();
//        Map<String, Object> condition = new HashMap();
//        condition.put("recordTime", timestamp);
//        condition.put("blockFlag", false);
//        ResultData response = cityAirQualityDao.select(condition);
//        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
//            cityAirQualityDao.insertBatch(airQualityList);
//        }
//    }
//
//    private MojiRecord locate(String district) {
//        for (int i = 0; i < list.size(); i++) {
//            MojiRecord record = list.get(i);
//            if (record.getName().contains(district)) return record;
//        }
//        return null;
//    }
//}
