package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.AirQualityDao;
import finley.gmair.dao.CityUrlDao;
import finley.gmair.dao.MonitorStationDao;
import finley.gmair.model.air.AirQuality;
import finley.gmair.model.air.CityUrl;
import finley.gmair.model.air.MonitorStation;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityUrlVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankCrawler {
    private final static String AIR_RANK = "http://pm25.in/rank";
    private final static String AIR_URL = "http://pm25.in";

    @Autowired
    LocationFeign locationFeign;

    @Autowired
    ObscureCityCacheService obscureCityCacheService;

    @Autowired
    ProvinceCityCacheService provinceCityCacheService;

    @Autowired
    CityUrlDao cityUrlDao;

    @Autowired
    AirQualityDao airQualityDao;

    @Autowired
    MonitorStationDao monitorStationDao;

    /**
     * get city rank and
     * every hour on half
     */
    @Scheduled(cron = "* 0/30 * * * *")
    public void rank() {
        Map<String, AirQuality> map = new HashMap<>();
        int count = 1;
        while (count > 0) {
            count--;
            try {
                Document page = Jsoup.connect(AIR_RANK).get();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis() / (3600000) * 3600000);

                Element rankTable = page.select("div.table").first();
                Element tableBody = rankTable.getElementsByTag("tbody").first();
                Elements trs = tableBody.getElementsByTag("tr");
                for (Element tr : trs) {
                    Elements tds = tr.getElementsByTag("td");
                    AirQuality airQuality = new AirQuality();
                    try {
                        Element cityHref = tds.get(1).getElementsByTag("a").first();
                        String obscureCityName = cityHref.text();
                        ResultData response = obscureCityCacheService.fetch(obscureCityName);
                        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            // if we have city name in the cache
                            String cityId = (String) response.getData();
                            airQuality.setCityId(cityId);
                        } else {
                            // if we do not have city in cache, we need to fetch using feign invoke
                            response = locationFeign.geocoder(obscureCityName);
                            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                JSONObject jsonObject = new JSONObject((LinkedHashMap<String, Object>) response.getData());
                                if (!StringUtils.isEmpty(jsonObject.get("address_components"))) {
                                    String accurateCity = jsonObject.getJSONObject("address_components").getString("city");
                                    response = provinceCityCacheService.fetch(accurateCity);
                                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                        String cityId = (String) response.getData();
                                        airQuality.setCityId(cityId);
                                        obscureCityCacheService.generate(obscureCityName, cityId);
                                    } else {
                                        System.out.println(accurateCity + " has been processed");
                                    }
                                }
                            } else {
                                System.out.println(response.getDescription() + obscureCityName);
                                //the api endpoint can only be access 60 time in a second, so sleep 50 ms
                            }
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        airQuality.setUrl(cityHref.attr("href"));
                        airQuality.setAqi(Double.parseDouble(tds.get(2).text()));
                        airQuality.setAqiLevel(tds.get(3).text());
                        airQuality.setPrimePollution(tds.get(4).text());
                        airQuality.setPm25(Double.parseDouble(tds.get(5).text()));
                        airQuality.setPm10(Double.parseDouble(tds.get(6).text()));
                        airQuality.setCo(Double.parseDouble(tds.get(7).text()));
                        airQuality.setNo2(Double.parseDouble(tds.get(8).text()));
                        airQuality.setO3(Double.parseDouble(tds.get(9).text()));
                        airQuality.setSo2(Double.parseDouble(tds.get(11).text()));
                        airQuality.setRecordTime(timestamp);
                        if (airQuality.getCityId() != null) {
                            map.put(cityHref.text(), airQuality);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                //another try in 1 minutes for 4 times
                try {
                    Thread.sleep(60000);
                } catch (Exception te) {

                }
            }
        }
        List<CityUrl> cityUrlList = map.values().stream()
                .map(e -> new CityUrl(e.getCityId(), AIR_URL + e.getUrl()))
                .collect(Collectors.toList());
        List<AirQuality> airQualityList = map.values().stream().collect(Collectors.toList());
        insertCityAqiDetail(airQualityList);
        updateCityUrl(cityUrlList);
    }

    private void insertCityAqiDetail(List<AirQuality> airQualityList) {
        if (!airQualityList.isEmpty())
            airQualityDao.insertBatch(airQualityList);
    }

    private void updateCityUrl(List<CityUrl> cityUrlList) {
        cityUrlDao.replaceBatch(cityUrlList);
    }

    /**
     * update city monitor station, every month
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void updateCityStation() {
        Map<String, Object> condition = new HashMap<>();
        ResultData response = cityUrlDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityUrlVo> cityUrlVoList = (List<CityUrlVo>) response.getData();
            for (CityUrlVo cityUrlVo : cityUrlVoList) {
                List<MonitorStation> monitorStations = fetchCityStation(cityUrlVo.getCityId(), cityUrlVo.getCityUrl());
                if (!monitorStations.isEmpty())
                    monitorStationDao.insertBatch(monitorStations);
            }
        }
    }


    public List<MonitorStation> fetchCityStation(String cityId, String url) {
        List<MonitorStation> stations = new ArrayList<>();
        try {
            Document page = Jsoup.connect(url).get();
            Element table = page.getElementById("detail-data");
            Element tableBody = table.getElementsByTag("tbody").first();
            Elements trs = tableBody.getElementsByTag("tr");
            for (Element tr: trs) {
                MonitorStation station = new MonitorStation();
                Elements tds = tr.getElementsByTag("td");
                station.setBelongCityId(cityId);
                station.setStationName(tds.get(0).text());
                stations.add(station);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //fail to connect to the page, have another try in 1 seconds
            try {
                Thread.sleep(1000);
            } catch (Exception te) {

            }
        }

        return stations;
    }
}
