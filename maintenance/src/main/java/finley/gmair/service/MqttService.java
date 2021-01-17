package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用mqtt-management服务
 *
 * @author lycheeshell
 * @date 2021/1/17 17:03
 */
@FeignClient("corev3-agent")
public interface MqttService {

    /**
     * 查询v3版本的设备当前存在的警报列表
     *
     * @param machineId 设备id
     * @return 设备当前存在的警报列表
     */
    @GetMapping("/mqtt/alert/getExistingAlert")
    ResultData getExistingAlert(@RequestParam("machineId") String machineId);

}
