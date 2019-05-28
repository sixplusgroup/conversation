package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {
    /**
     * 获取设备的当前运行状态
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/machine/status/byqrcode")
    ResultData indoor(@RequestParam(value = "qrcode") String qrcode);

    /**
     * 获取设备所处城市的cityId
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/machine/default/location/probe/cityid")
    ResultData outdoor(@RequestParam("qrcode") String qrcode);


    /**
     * 开关设备
     *
     * @param qrcode
     * @param component
     * @param operation
     * @return
     */
    @PostMapping("/machine/control/option/operate")
    ResultData power(@RequestParam("qrcode") String qrcode, @RequestParam("component") String component, @RequestParam("operation") String operation);

    /**
     * 调节风量
     *
     * @param qrcode
     * @param speed
     * @return
     */
    ResultData speed(@RequestParam("qrcode") String qrcode, @RequestParam("speed") Integer speed);
}
