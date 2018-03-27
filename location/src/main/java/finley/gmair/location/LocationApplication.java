package finley.gmair.location;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.LocationService;
import finley.gmair.util.HttpDeal;
import finley.gmair.util.LocationProperties;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan({"finley.gmair.service", "finley.gmair.dao"})
@RequestMapping("/location")
public class LocationApplication {
    private final static String TENCENT_DISTRICT_URL = "http://apis.map.qq.com/ws/district/v1/list";

    @Autowired
    private LocationService locationService;

    public static void main(String[] args) {
        SpringApplication.run(LocationApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/address/resolve")
    public ResultData geocoder(String address) {
        ResultData result = new ResultData();

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/init")
    public ResultData init() {
        ResultData result = new ResultData();
        String url = new StringBuffer(TENCENT_DISTRICT_URL).append("?key=").append(LocationProperties.getValue("tencent_map_key")).toString();
        JSONObject response = JSON.parseObject(HttpDeal.getResponse(url));
        if (!StringUtils.isEmpty(response) && response.getInteger("status") == 0) {
            new Thread(() -> locationService.process(response)).start();
            result.setDescription("Succeed to fetch the data, processing");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve the response from tencent map");
        }
        return result;
    }
}
