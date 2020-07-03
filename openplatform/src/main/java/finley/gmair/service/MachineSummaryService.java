package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lyy
 * @Description
 * @create 2020-07-01 12:10 上午
 */
@FeignClient("machine-agent")
public interface MachineSummaryService {

    /**
     * 获取某个设备一星期内的pm25数据
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/machine/status/daily")
    ResultData getDailyPM25(String qrcode);
}
