package finley.gmair.service;


import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("airquality-agent")
public interface AirQualityFeignService {
    @RequestMapping(value = "/airquality/hourly/cityAqi", method = RequestMethod.POST)
    ResultData scheduleHourly();

    @RequestMapping(value = "/airquality/daily/cityAqi", method = RequestMethod.POST)
    ResultData scheduleDaily();

    @RequestMapping(value = "/airquality/monthly/cityAqi", method = RequestMethod.POST)
    ResultData scheduleMonthly();
}
