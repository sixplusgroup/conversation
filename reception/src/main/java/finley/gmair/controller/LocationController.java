package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.LocationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import finley.gmair.util.IPUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/reception/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "/ip/address", method = RequestMethod.GET)
    public ResultData ipAddress(HttpServletRequest request) {
        ResultData result = new ResultData();
        String ip = IPUtil.getIP(request);
        String locationPlace = "";
        ResultData response = locationService.ip2address(ip);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            LinkedHashMap<String, Object> locInformation = (LinkedHashMap<String, Object>) response.getData();
            LinkedHashMap<String, Object> ad_info = (LinkedHashMap<String, Object>) locInformation.get("ad_info");
            JSONObject json = new JSONObject();
            json.put("province", ad_info.get("province"));
            json.put("city", ad_info.get("city"));
            json.put("code", ad_info.get("adcode"));
            result.setData(json);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to locate the ip address");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to locate the ip address");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/province/list")
    public ResultData province() {
        return locationService.province();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{provinceId}/cities")
    public ResultData city(@PathVariable("provinceId") String province) {
        return locationService.city(province);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{cityId}/districts")
    public ResultData district(@PathVariable("cityId") String city) {
        return locationService.district(city);
    }

    @GetMapping("/probe/provinceId")
    public ResultData probeProvinceIdByCityId(String cityId) {
        return locationService.probeProvinceIdByCityId(cityId);
    }

    @GetMapping("/probe/cityId")
    public ResultData probeCityId(String code) {
        return locationService.probeCityId(code);
    }
}
