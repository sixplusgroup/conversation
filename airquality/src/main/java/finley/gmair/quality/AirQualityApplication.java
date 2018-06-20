package finley.gmair.quality;

import finley.gmair.service.MonitorStationCrawler;
import finley.gmair.service.RankCrawler;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller"})
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableScheduling
@EnableCaching
public class AirQualityApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirQualityApplication.class, args);
    }

    @Autowired
    private RankCrawler rankCrawler;

    @Autowired
    private MonitorStationCrawler monitorStationCrawler;

    @RequestMapping("/airquality/crawler")
    public ResultData crawler() {
        rankCrawler.updateCityStation();
        return new ResultData();
    }

    @RequestMapping("/airquality/monitorStationCrawler")
    public ResultData monitorStationCrawler() {
        monitorStationCrawler.craw();
        return new ResultData();
    }
}