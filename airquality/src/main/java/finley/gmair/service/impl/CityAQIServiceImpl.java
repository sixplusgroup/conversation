package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import finley.gmair.service.CityAQIService;
import finley.gmair.util.ResultData;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @Override
    public ResultData obtain() {
        ResultData result = new ResultData();
        StringBuffer url = new StringBuffer("http://datacenter.mep.gov.cn/websjzx/api/api/air/getAirHours.vm?");
        while (count > 0) {
            try {
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
                break;
            } catch (Exception e) {
                logger.error(e.getMessage());
                if (count <= 0) {
                    logger.info("Fail to load the air quality information.");
                }
                count--;
            }
        }
        logger.info(JSON.toJSONString(result.getData()));
        return result;
    }
}
