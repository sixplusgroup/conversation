package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/20
 */
@FeignClient("airquality-agent")
public interface AirQualityService {

    @GetMapping("/airquality/hourly/cityAqi")
    ResultData allHourlyCityAqi();
}
