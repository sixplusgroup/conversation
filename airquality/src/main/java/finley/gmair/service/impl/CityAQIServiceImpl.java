package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
<<<<<<< HEAD
=======
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.CityAirQualityDao;
import finley.gmair.model.air.CityAirQuality;
import finley.gmair.service.AirQualityCacheService;
>>>>>>> 604af76d3ca7ddd34966c8430afdd4fcb881e77f
import finley.gmair.service.CityAQIService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:/airquality.properties")
public class CityAQIServiceImpl implements CityAQIService {

    private Logger logger = LoggerFactory.getLogger(CityAQIServiceImpl.class);

    @Value("${account}")
    private String account;

    @Value("${password}")
    private String password;

    @Value("${retry_count}")
    private int count;

    @Value("${driver_path}")
    private String path;

    private Robot robot;

    @Autowired
    private AirQualityCacheService airQualityCacheService;

    @Autowired
    private CityAirQualityDao cityAirQualityDao;

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", path);
        System.setProperty("java.awt.headless", "false");
        try {
            robot = new Robot();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public ResultData obtain() {
        ResultData result = new ResultData();
        StringBuffer url = new StringBuffer("http://datacenter.mep.gov.cn/websjzx/api/api/air/getAirHours.vm?");
//        ProfilesIni ini = new ProfilesIni();
//        FirefoxProfile profile = ini.getProfile("default");
        int cd = count;
        String content = "[]";
        while (cd > 0) {
            try {
<<<<<<< HEAD
                URL location = new URL(url.toString());
                HttpURLConnection connection = (HttpURLConnection) location.openConnection();
                String origin = new StringBuffer(account).append(":").append(password).toString();
                String auth = new String(Base64.encodeBase64(origin.getBytes()));
                connection.setRequestProperty("Authorization", new StringBuffer("Basic ").append(auth).toString());
                connection.setRequestProperty("User-Agent", "MSIE 7.0");
                connection.setConnectTimeout(5000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer temp = new StringBuffer();
                while (null != (line = reader.readLine())) {
                    temp.append(line);
                }
                reader.close();
                connection.disconnect();
                JSONArray data = JSONArray.parseArray(temp.toString());
                result.setData(data);
=======
                ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File(path)).usingAnyFreePort().build();
                service.start();
                WebDriver driver = new ChromeDriver();
                WebDriver.Navigation navigation = driver.navigate();
                new Thread(() -> login()).start();
                navigation.to(url.toString());
                WebElement element = driver.findElement(By.tagName("body"));
                content = element.getText();
                driver.close();
                driver.quit();
                service.stop();
>>>>>>> 604af76d3ca7ddd34966c8430afdd4fcb881e77f
                break;
            } catch (Exception e) {
                logger.error(e.getMessage());
                if (count <= 0) {
                    logger.info("Fail to load the air quality information.");
                }
                cd--;
            }
        }
        JSONArray data = new JSONArray();
        try {
            data = JSON.parseArray(content);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(content);
        }
        List<CityAirQuality> list = new ArrayList<>();
        CityAirQuality quality;
        for (int i = 0; i < data.size(); i++) {
            JSONObject item = data.getJSONObject(i);
            quality = new CityAirQuality();
            try {
                quality.setCityId(item.getString("citycode"));
                quality.setAqi(item.getDouble("aqi"));
            } catch (Exception e) {
                logger.info(JSON.toJSONString(item));
                logger.error(quality.getCityId() + " cannot be updated at this time, skipping...");
                continue;
            }
            if (quality.getAqi() > 300) {
                quality.setAqiLevel("严重污染");
            } else if (quality.getAqi() > 200) {
                quality.setAqiLevel("重度污染");
            } else if (quality.getAqi() > 150) {
                quality.setAqiLevel("中度污染");
            } else if (quality.getAqi() > 100) {
                quality.setAqiLevel("轻度污染");
            } else if (quality.getAqi() > 50) {
                quality.setAqiLevel("良");
            } else if (quality.getAqi() >= 0) {
                quality.setAqiLevel("优");
            }
            try {
                quality.setPrimePollution(item.getString("primarypollutant"));
            } catch (Exception e) {
                quality.setPrimePollution("——");
            }
            try {
                quality.setPm2_5(item.getDouble("pm2_5"));
            } catch (Exception e) {
                quality.setPm2_5(0);
            }
            try {
                quality.setPm10(item.getDouble("pm10"));
            } catch (Exception e) {
                quality.setPm10(0);
            }
            try {
                quality.setCo(item.getDouble("co"));
            } catch (Exception e) {
                quality.setCo(0);
            }
            try {
                quality.setNo2(item.getDouble("no2"));
            } catch (Exception e) {
                quality.setNo2(0);
            }
            try {
                quality.setO3(item.getDouble("o3"));
            } catch (Exception e) {
                quality.setO3(0);
            }
            try {
                quality.setSo2(item.getDouble("so2"));
            } catch (Exception e) {
                quality.setSo2(0);
            }
            quality.setRecordTime(new Timestamp(System.currentTimeMillis() / (3600000) * 3600000));
            list.add(quality);
        }
        insertCityAqiDetail(list);
        return result;
    }

    private void login() {
        try {
            Thread.sleep(8000);
            stimulate(KeyEvent.VK_G, KeyEvent.VK_U, KeyEvent.VK_O, KeyEvent.VK_M, KeyEvent.VK_A, KeyEvent.VK_I);
            stimulate(KeyEvent.VK_TAB);
            Thread.sleep(100);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.delay(100);
            stimulate(KeyEvent.VK_G);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.delay(100);
            stimulate(KeyEvent.VK_M, KeyEvent.VK_A, KeyEvent.VK_I, KeyEvent.VK_R);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.delay(100);
            stimulate(KeyEvent.VK_2);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.delay(100);
            stimulate(KeyEvent.VK_2, KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_8);
            stimulate(KeyEvent.VK_ENTER);
            Thread.sleep(10000);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void stimulate(int... code) {
        for (int item : code) {
            robot.keyPress(item);
            robot.delay(100);
            robot.keyRelease(item);
            robot.delay(100);
        }
    }

    private void insertCityAqiDetail(List<CityAirQuality> airQualityList) {
        // step 1: update cache
        try {
            for (CityAirQuality cityAirQuality : airQualityList) {
                airQualityCacheService.generate(cityAirQuality);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // step 2: update database
        if (airQualityList.isEmpty())
            return;
        Timestamp timestamp = airQualityList.get(0).getRecordTime();
        Map<String, Object> condition = new HashMap();
        condition.put("recordTime", timestamp);
        condition.put("blockFlag", false);
        ResultData response = cityAirQualityDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            cityAirQualityDao.insertBatch(airQualityList);
        }
    }
}
