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
    @PostMapping("/machine/control/option/config/speed")
    ResultData speed(@RequestParam("qrcode") String qrcode, @RequestParam("speed") Integer speed);

    /**
     * 根据qrcode查model_id
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/machine/qrcode/model")
    ResultData getModel(@RequestParam("codeValue") String qrcode);

    /**
     * 根据model_id查风量范围
     *
     * @param modelId
     * @return
     */
    @GetMapping("/machine/model/volume/probe/by/modelId")
    ResultData probeModelVolumeByModelId(@RequestParam("modelId") String modelId);

    /**
     * 根据设备二维码及设备组件查询
     *
     * @param modelId
     * @param component
     * @return
     */
    @GetMapping("/machine/control/option/search")
    ResultData search(@RequestParam("modelId") String modelId, @RequestParam("component") String component, @RequestParam("value") String value);

    /**
     * 模式转换
     *
     * @param qrcode
     * @param mode
     * @return
     */
    @PostMapping("/machine/control/option/operate")
    ResultData mode(@RequestParam("qrcode") String qrcode, @RequestParam("component") String component, @RequestParam("operation") String mode);


    /**
     * 开关童锁
     *
     * @param qrcode
     * @param component
     * @param operation
     * @return
     */
    @PostMapping("/machine/control/option/operate")
    ResultData lock(@RequestParam("qrcode") String qrcode, @RequestParam("component") String component, @RequestParam("operation") String operation);


    /**
     * 调节辅热
     *
     * @param qrcode
     * @param operation
     * @return
     */
    @PostMapping("/machine/control/option/operate")
    ResultData heat(@RequestParam("qrcode") String qrcode, @RequestParam("component") String component, @RequestParam("operation") String operation);

}
