package finley.gmair.service;

import finley.gmair.model.air.AirQuality;
import finley.gmair.model.air.MonitorStation;
import finley.gmair.util.RegExpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankCrawler {
    private final static String AIR_RANK = "http://pm25.in/rank";
    private final static String AIR_URL = "http://pm25.in";

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
                    airQuality.setRank(Integer.parseInt(tds.get(0).text()));
                    Element cityHref = tds.get(1).getElementsByTag("a").first();
                    airQuality.setCity(cityHref.text());
                    airQuality.setUrl(cityHref.attr("href"));
                    airQuality.setAqi(Double.parseDouble(tds.get(2).text()));
                    airQuality.setClassification(tds.get(3).text());
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
