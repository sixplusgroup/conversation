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
import org.springframework.jca.cci.CciOperationNotSupportedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
@ComponentScan({"finley.gmair.service", "finley.gmair.dao"})
@RequestMapping("/location")
public class LocationApplication {
    private final static String TENCENT_DISTRICT_URL = "http://apis.map.qq.com/ws/district/v1/list";

    private final static String TENCENT_GEO_URL = "http://apis.map.qq.com/ws/geocoder/v1/";

    private final static String TENCENT_IP_URL = "http://apis.map.qq.com/ws/location/v1/ip";

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
        if (StringUtils.isEmpty(address)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The address cannot be empty");
            return result;
        }
        String url = new StringBuffer(TENCENT_GEO_URL).append("?key=").append(LocationProperties.getValue("tencent_map_key")).append("&address=").append(address.trim()).toString();
        JSONObject response = JSON.parseObject(HttpDeal.getResponse(url));
        return process(response);
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
        if (StringUtils.isEmpty(ip)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the ip is correct");
            return result;
        }
        String url = new StringBuffer(TENCENT_IP_URL).append("?ip=").append(ip).append("&key=").append(LocationProperties.getValue("tencent_map_key")).toString();
        JSONObject response = JSON.parseObject(HttpDeal.getResponse(url));
        return process(response);
    }

    @CrossOrigin
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

    @CrossOrigin
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

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/{cityId}/districts")
    public ResultData district(@PathVariable("cityId") String city) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(city)) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Please make sure the request is illegal");
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", city);
        condition.put("blockFlag", false);
        ResultData response = locationService.fetchDistrict(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No district information found");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/ll/resolve")
    public ResultData ll2description(String longitude, String latitude) {
        ResultData result = new ResultData();
        try {
            double lng = Double.parseDouble(longitude);
            double lat = Double.parseDouble(latitude);
            String url = new StringBuffer(TENCENT_GEO_URL).append("?location=").append(lat).append(",").append(lng).append("&key=").append(LocationProperties.getValue("tencent_map_key")).toString();
            JSONObject response = JSON.parseObject(HttpDeal.getResponse(url));
            return process(response);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the longitude & latitude is correct");
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

    @GetMapping("/city/profile")
    public ResultData cityProfile(String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        ResultData response = locationService.fetchCity(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No city information found for city id: ").append(cityId).toString());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    private ResultData process(JSONObject response) {
        ResultData result = new ResultData();
        if (!StringUtils.isEmpty(response) && response.getInteger("status") == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getJSONObject("result"));
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Cannot process the result at the moment");
        }
        return result;
    }


    @GetMapping("/probe/provinceId")
    public ResultData probeProvinceIdByCityId(String cityId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(cityId)){
           result.setResponseCode(ResponseCode.RESPONSE_ERROR);
           result.setDescription("please provide cityId");
           return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId",cityId);
        condition.put("blockFlag",false);
        ResultData response = locationService.fetchProvinceIdByCityId(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No provinceId found by city id: ").append(cityId).toString());
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find provinceId by cityId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
