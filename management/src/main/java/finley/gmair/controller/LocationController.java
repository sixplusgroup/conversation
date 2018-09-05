package finley.gmair.controller;

import finley.gmair.service.LocationService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
