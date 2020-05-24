package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("machine-agent")
public interface MachineService {

    /**
     * 用户控制设备
     * 查看finley.gmair.controller.ControlOptionControl#chooseComponent[machine]
     *
     * @param qrcode 设备二维码
     * @param component 组件
     * @param operation 操作
     * @return 结果
     */
    @PostMapping("/machine/control/option/operate")
    ResultData chooseComponent(@RequestParam("qrcode") String qrcode,
                               @RequestParam("component") String component,
                               @RequestParam("operation") String operation);

    /**
     * 调节设备风量
     * 查看finley.gmair.controller.ControlOptionControl#configSpeed[machine]
     *
     * @param qrcode 设备二维码
     * @param speed 将要设定的风速
     * @return 结果
     */
    @PostMapping(value = "/machine/control/config/speed")
    ResultData configSpeed(@RequestParam("qrcode")String qrcode, @RequestParam("speed")int speed);

    /**
     * 根据顾客Id获取其账号绑定的设备列表
     * 查看finley.gmair.controller.ConsumerQRcodeController#list[machine]
     *
     * @param consumerId 顾客Id
     * @return 绑定的设备列表
     */
    @GetMapping("/machine/consumer/machine/list")
    ResultData obtainMachineList(@RequestParam("consumerId") String consumerId);
}
