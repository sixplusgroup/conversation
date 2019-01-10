package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("data-analysis-agent")
public interface DataAnalysisService {
    @PostMapping("/data/analysis/machine/status/schedule/statistical/hourly")
    ResultData statisticalDataHourly();

    @PostMapping("/data/analysis/machine/status/schedule/statistical/daily")
    ResultData statisticalDataDaily();
}
