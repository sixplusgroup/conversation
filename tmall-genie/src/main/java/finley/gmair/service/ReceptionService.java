package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reception-agent")
public interface ReceptionService {

    // 根据token获取设备列表
    @GetMapping("/reception/machine/list")
    ResultData getDeviceListByToken(@RequestParam("access_token") String accessToken);

}
