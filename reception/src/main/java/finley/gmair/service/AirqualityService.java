package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("airquality-agent")
public interface AirqualityService {

    @GetMapping(value = "/airquality/latest/{cityId}")
    ResultData getLatestCityAirQuality(@RequestParam("cityId") String cityId);

    @GetMapping("/airquality/hourly/cityAqi/{cityId}")
    ResultData getHourlyCityAqi(@RequestParam("cityId") String cityId);

    @GetMapping("/airquality/daily/cityAqi/{cityId}")
    ResultData getDailyCityAqi(@RequestParam("cityId") String cityId);

    @GetMapping("/airquality/weekly/cityAqi/{cityId}")
    ResultData getWeeklyCityAqi(@RequestParam("cityId")String cityId);
}
