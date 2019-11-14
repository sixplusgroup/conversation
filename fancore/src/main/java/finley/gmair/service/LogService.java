package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("log-agent")
public interface LogService {
    @PostMapping("/log/mqtt/ack/create")
    ResultData createMqttAckLog(@RequestParam("ackId") String ackId,
                                @RequestParam("machineId") String machineId,
                                @RequestParam("code") int code,
                                @RequestParam("component") String component,
                                @RequestParam("ip") String ip,
                                @RequestParam("logDetail") String logDetail);
}
