package finley.gmair.scene.client;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : Lyy
 * @create : 2020-12-04 17:54
 **/
@FeignClient("machine-agent")
public interface MachineClient {

    // 设备执行指令
    @PostMapping("/machine/control/option/operate")
    ResultData operate(@RequestParam("qrcode") String qrcode,
                       @RequestParam("component") String component,
                       @RequestParam("operation") String operation);

    @PostMapping("/machine/control/option/config/speed")
    ResultData configSpeed(@RequestParam("qrcode") String qrcode, @RequestParam("speed") int speed);

    @PostMapping("/machine/control/option/config/light")
    ResultData configLight(@RequestParam("qrcode") String qrcode, @RequestParam("light") int light);

    @PostMapping("/machine/control/option/config/temp")
    ResultData configTemp(@RequestParam("qrcode") String qrcode, @RequestParam("temp") int temp);

    @PostMapping("/machine/control/option/config/timing")
    ResultData configTiming(@RequestParam("qrcode") String qrcode, @RequestParam("countdown") int countdown);

    // 获取当前设备数据
    @GetMapping("/machine/{qrcode}/status")
    ResultData runningStatus(@PathVariable("qrcode") String qrcode);

    // 设备定时开关
    @PostMapping("/machine/power/onoff/confirm")
    ResultData confirmPowerOnoff(@RequestParam("qrcode") String qrcode,
                                 @RequestParam("startHour") int startHour,
                                 @RequestParam("startMinute") int startMinute,
                                 @RequestParam("endHour") int endHour,
                                 @RequestParam("endMinute") int endMinute,
                                 @RequestParam("status") boolean status);


    // 根据用户ID获取设备列表
    @GetMapping("/machine/consumer/machine/list")
    ResultData obtainMachineList(@RequestParam("consumerId") String consumerId);
}
