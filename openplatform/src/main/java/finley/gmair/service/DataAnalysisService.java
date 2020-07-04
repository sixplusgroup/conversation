package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 查询设备运行时长接口
 */
@FeignClient("data-analysis-agent")
public interface DataAnalysisService {

    /**
     * 获取运行时长(单位小时)
     */
    @GetMapping("/data/analysis/machine/status/power/lastNhour")
    ResultData fetchPowerHourly(@RequestParam(name = "qrcode") String qrcode, @RequestParam(name = "lastNHour") int lastNHour);

    /**
     * 获取运行时长（单位天）
     */
    @GetMapping("/data/analysis/machine/status/power/lastNday")
    ResultData fetchPowerDaily(@RequestParam(name = "qrcode") String qrcode, @RequestParam(name = "lastNDay") int lastNDay);

    /**
     * 获取风量（单位小时）
     */
    @GetMapping("/data/analysis/machine/status/volume/lastNhour")
    ResultData fetchVolumeHourly(@RequestParam(name = "qrcode") String qrcode, @RequestParam(name = "lastNHour") int lastNHour);

    /**
     * 获取风量（单位天）
     */
    @GetMapping("/data/analysis/machine/status/volume/lastNday")
    ResultData fetchVolumeDaily(@RequestParam(name = "qrcode") String qrcode, @RequestParam(name = "lastNDay") int lastNDay);
}
