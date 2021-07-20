package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("airquality-agent")
public interface AirqualityAgent {
    @GetMapping("/airquality/lastNday")
    ResultData fetchLastNDayData(@RequestParam("cityId") String cityId, @RequestParam("lastNday") int lastNday);

    @GetMapping("/airquality/lastNhour")
    ResultData fetchLastNHourData(@RequestParam("cityId")String cityId, @RequestParam("lastNhour")int lastNhour);
}
