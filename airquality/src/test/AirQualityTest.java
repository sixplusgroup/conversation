import finley.gmair.quality.AirQualityApplication;
import finley.gmair.service.MonitorStationCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AirQualityApplication.class})
public class AirQualityTest {

    @Autowired
    private MonitorStationCrawler monitorStationCrawler;

    @Test
    public void test1() {
        monitorStationCrawler.updateCityStation();
    }

    @Test
    public void test2() {
        monitorStationCrawler.craw();
    }
}
