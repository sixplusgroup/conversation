import finley.gmair.quality.AirQualityApplication;
import finley.gmair.service.AirQualityStatisticService;
import finley.gmair.service.MonitorStationCrawler;
import finley.gmair.util.ResultData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AirQualityApplication.class})
public class AirQualityTest {

    @Autowired
    private AirQualityStatisticService airQualityStatisticService;

    @Test
    public void test1() {
        airQualityStatisticService.handleAirQualityDailyStatistic();
    }

    @Test
    public void test2() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", "513200");
        LocalDate today = LocalDateTime.now().toLocalDate();
        LocalDate lastWeekDay = today.minusDays(7);
        condition.put("createTimeGTE", lastWeekDay);

        ResultData response = airQualityStatisticService.fetchAirQualityDailyStatistic(condition);

        System.out.println(response);
    }
}
