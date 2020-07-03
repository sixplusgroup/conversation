package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 查询设备运行时长接口
 */
@FeignClient("data-analysis-agent")
public interface DataAnalysisService {

    /**
     * 获取运行时长(单位小时)
     */
    @GetMapping("/power/lastNhour")
    ResultData fetchPowerHourly(String qrcode, int lastNHour);

    /**
     * 获取运行时长（单位天）
     */
    @GetMapping("/power/lastNday")
    ResultData fetchPowerDaily(String qrcode, int lastNDay);

    /**
     * 获取风量（单位小时）
     */
    @GetMapping("/volume/lastNhour")
    ResultData fetchVolumeHourly(String qrcode, int lastNHour);

    /**
     * 获取风量（单位天）
     */
    @GetMapping("/volume/lastNday")
    ResultData fetchVolumeDaily(String qrcode, int lastNDay);
}
