package finley.gmair.service.rpc;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 查询设备运行时长接口
 */
@FeignClient("data-analysis-agent")
public interface DataAnalysisService {

    /**
     * 获取运行时长(单位小时)
     */
    @RequestMapping(value = "/data/analysis/machine/status/{statusType}/lastNhour", method = RequestMethod.GET)
    ResultData fetchHourly(@RequestParam(name = "qrcode") String qrcode, @RequestParam(name = "lastNhour") int lastNHour, @PathVariable("statusType") String statusType);

    /**
     * 获取运行时长（单位天）
     */
    @RequestMapping(value = "/data/analysis/machine/status/{statusType}/lastNday", method = RequestMethod.GET)
    ResultData fetchDaily(@RequestParam(name = "qrcode") String qrcode, @RequestParam(name = "lastNday") int lastNDay, @PathVariable("statusType") String statusType);

}
