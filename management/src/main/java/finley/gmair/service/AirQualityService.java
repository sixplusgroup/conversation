package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/20
 */
@FeignClient("airquality-agent")
public interface AirQualityService {

    @GetMapping("/airquality/hourly/cityAqi")
    ResultData allHourlyCityAqi();

    @GetMapping("/airquality/latest")
    ResultData getLatestCityAirQuality();

    @GetMapping(value = "/airquality/latest/{cityId}")
    ResultData getLatestCityAirQuality(@PathVariable String cityId);

    @GetMapping("/airquality/station/{stationId}")
    ResultData fetchLatestByStationId(@PathVariable String stationId);

    @GetMapping("/airquality/station/city/{cityId}")
    ResultData fetchLatestByCityId(@PathVariable String cityId);

    @GetMapping("/airquality/station/list")
    ResultData fetchLatestStationList();
}
