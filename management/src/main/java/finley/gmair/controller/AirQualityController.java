package finley.gmair.controller;

import finley.gmair.service.AirQualityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/20
 */
@RestController
@RequestMapping("/management/airQuality")
public class AirQualityController {

    @Autowired
    private AirQualityService airQualityService;

    @GetMapping(value = "/hourly/cityAqi")
    public ResultData allHourlyCityAqi() {
        return airQualityService.allHourlyCityAqi();
    }
}