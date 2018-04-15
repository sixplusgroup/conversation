package finley.gmair.service;

import finley.gmair.model.air.AirQuality;
import finley.gmair.model.air.MonitorStation;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.RegExpUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

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

    public void rank() {
        Map<String, AirQuality> map = new HashMap<>();
        try {
            Document page = Jsoup.connect(AIR_RANK).get();
            Element time = page.select("div.time").first();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (!StringUtils.isEmpty(time)) {
                timestamp = RegExpUtil.extractDateTime(time.text());
                System.out.println(time.text());
            }
            Element rankTable = page.select("div.table").first();
            Element tableBody = rankTable.getElementsByTag("tbody").first();
            Elements trs = tableBody.getElementsByTag("tr");
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                AirQuality airQuality = new AirQuality();
                try {
                    Element cityHref = tds.get(1).getElementsByTag("a").first();
                    //todo use city name to get city id
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
                            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) response.getData();
                            LinkedHashMap<String, String> addressComponents =
                                    (LinkedHashMap<String, String>) linkedHashMap.get("address_components");
                            String accurateCity = addressComponents.get("city");
                            response = provinceCityCacheService.fetch(accurateCity);
                            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                String cityId = (String) response.getData();
                                airQuality.setCityId(cityId);
                                obscureCityCacheService.generate(obscureCityName, cityId);
                            } else {
                                System.out.println(accurateCity);
                            }
                        } else {
                            continue;
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
                    airQuality.setCreateAt(timestamp);
                    map.put(cityHref.text(), airQuality);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //fail to connect to the page, have another try in 5 minutes
            try {
                Thread.sleep(300000);
            } catch (Exception te) {

            }
        }
    }

    public List<MonitorStation> cityDetail(String cityId, String url) {
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
