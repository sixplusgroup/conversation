package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @param machineId 设备mac
     * @param code   告警码
     * @return 设备当前存在的警报列表
     */
    @GetMapping("/mqtt/alert/getExistingAlert")
    ResultData getExistingAlert(@RequestParam("machineId") String machineId, @RequestParam("code") Integer code);

    /**
     * 消除设备告警
     *
     * @param machineId 设备mac
     * @param code 告警码
     * @return 消除操作的结果
     */
    @PostMapping("/mqtt/alert/update")
    ResultData updateAlert(@RequestParam("machineId") String machineId, @RequestParam("code") int code);

    /**
     * 服务端要求即刻上报数据命令
     *
     * @param uid 设备mac
     * @return 操作结果
     */
    @PostMapping("/core/com/demand/report")
    ResultData demandReport(@RequestParam("uid") String uid);

}
