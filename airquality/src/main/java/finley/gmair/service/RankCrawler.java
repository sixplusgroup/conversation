package finley.gmair.service;

import finley.gmair.util.RegExpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.StringUtils;

public class RankCrawler {
    private final static String AIR_RANK = "http://pm25.in/rank";

    public void rank() {
        try {
            Document page = Jsoup.connect(AIR_RANK).get();
            Element time = page.select("div.time").first();
            if (!StringUtils.isEmpty(time)) {
                System.out.println(time.text());
            }
        } catch (Exception e) {
            //fail to connect to the page, have another try in 5 minutes
            try {
                Thread.sleep(300000);
            } catch (Exception te) {

            }
        }
    }
}
