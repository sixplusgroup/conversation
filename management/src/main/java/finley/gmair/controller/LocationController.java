package finley.gmair.controller;

import finley.gmair.service.LocationService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/management/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @RequestMapping(method = RequestMethod.POST, value = "/address/resolve")
    public ResultData geocoder(String address) {
        return locationService.geocoder(address);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/province/list")
    ResultData province(){
        return locationService.province();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{provinceId}/cities")
    ResultData city(@PathVariable("provinceId") String provinceId){
        return locationService.city(provinceId);
    }
}
