package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("airquality-agent")
public interface AirqualityService {

    @GetMapping(value = "/airquality/latest/{cityId}")
    ResultData getLatestCityAirQuality(@PathVariable("cityId") String cityId);

    @GetMapping("/airquality/hourly/cityAqi/{cityId}")
    ResultData getHourlyCityAqi(@PathVariable("cityId") String cityId);

    @GetMapping("/airquality/daily/cityAqi/{cityId}")
    ResultData getDailyCityAqi(@PathVariable("cityId") String cityId);

    @GetMapping("/airquality/weekly/cityAqi/{cityId}")
    ResultData getWeeklyCityAqi(@PathVariable("cityId")String cityId);
}
