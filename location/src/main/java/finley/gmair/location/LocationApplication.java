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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * This method is to analyze the user input and map it to a address
     *
     * @param address
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/address/resolve")
    public ResultData geocoder(String address) {
        ResultData result = new ResultData();

        return result;
    }

    /**
     * This will tell the location of the ip
     * if no parameter ip is specified, will use the request source ip by default
     *
     * @param ip
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/ip/resolve")
    public ResultData ip2address(String ip) {
        ResultData result = new ResultData();

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/province/list")
    public ResultData province() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = locationService.fetchProvince(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No province information found");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{provinceId}/cities")
    public ResultData city(@PathVariable("provinceId") String province) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(province)) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Please make sure the request is illegal");
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("provinceId", province);
        condition.put("blockFlag", false);
        ResultData response = locationService.fetchCity(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No city information found");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{cityId}/districts")
    public ResultData district(@PathVariable("cityId") String city) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(city)) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Please make sure the request is illegal");
        }

        return result;
    }

    /**
     * This method is called to initialize the province, city, district information
     * Do not call this method if you do not know what you are doing
     *
     * @return
     */
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
