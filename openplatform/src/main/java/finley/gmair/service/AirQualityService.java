package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("airquality-agent")
public interface AirQualityService {

    /**
     * 获取城市对应的空气质量信息
     *
     * @param cityId
     * @return
     */
    @GetMapping("/airquality/latest/{cityId}")
    ResultData airquality(@PathVariable("cityId") String cityId);
}
