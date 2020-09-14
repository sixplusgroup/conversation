package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: Bright Chan
 * @date: 2020/9/14 21:36
 * @description: DataAnalysisAgent
 */

@FeignClient("data-analysis-agent")
public interface DataAnalysisAgent {
    //获取machine过去N天的数据记录
    @GetMapping("/data/analysis/machine/status/{statusType}/lastNday")
    ResultData fetchLastNDayData(@RequestParam("qrcode") String qrcode,
                                 @RequestParam("lastNday") int lastNday,
                                 @PathVariable("statusType") String statusType);
}
