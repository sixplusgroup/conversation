package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {
    /**
     * 获取设备的当前运行状态
     *
     * @param qrcdoe
     * @return
     */
    @GetMapping("/machine/status/byqrcode")
    ResultData indoor(@RequestParam(value = "qrcode") String qrcdoe);

    /**
     * 获取设备所处城市的cityId
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/machine/default/location/probe/cityid")
    ResultData outdoor(@RequestParam("qrcode") String qrcode);
}
