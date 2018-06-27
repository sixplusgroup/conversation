package finley.gmair.controller;

import finley.gmair.service.LocationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
            locationPlace = new StringBuilder((String) ad_info.get("nation"))
                    .append((String) ad_info.get("province"))
                    .append((String) ad_info.get("city"))
                    .toString();
            result.setData(locationPlace);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to locate the ip address");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to locate the ip address");
        }
        return result;
    }
}
