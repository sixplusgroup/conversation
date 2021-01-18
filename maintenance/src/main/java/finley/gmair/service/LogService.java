package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用log服务
 *
 * @author lycheeshell
 * @date 2021/1/18 23:19
 */
@FeignClient("log-agent")
public interface LogService {

    /**
     * 查询用户操作设备的历史记录
     *
     * @param userId       用户id
     * @param machineValue 二维码
     * @return 用户操作设备的历史记录
     */
    @PostMapping("/log/useraction/query")
    ResultData getUserActionLog(@RequestParam("userId") String userId, @RequestParam("machineValue") String machineValue);

}
