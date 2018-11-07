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

    @GetMapping("/airquality/province/list")
    ResultData provinceAirQualityList();

    @GetMapping("/airquality/latest")
    ResultData getLatestCityAirQuality();

    @GetMapping(value = "/airquality/latest/{cityId}")
    ResultData getLatestCityAirQuality(@PathVariable("cityId") String cityId);

    @GetMapping("/airquality/station/{stationId}")
    ResultData fetchLatestByStationId(@PathVariable("stationId") String stationId);

    @GetMapping("/airquality/station/city/{cityId}")
    ResultData fetchLatestByCityId(@PathVariable("cityId") String cityId);

    @GetMapping("/airquality/station/list")
    ResultData fetchLatestStationList();

    @GetMapping("/airquality/weekly/cityAqi/{cityId}")
    ResultData getWeeklyCityAqi(@PathVariable("cityId") String cityId);

    @GetMapping("/airquality/province/{provinceId}")
    ResultData provinceAirQuality(@PathVariable("provinceId") String provinceId);
}
