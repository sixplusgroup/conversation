package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用machine服务
 *
 * @author lycheeshell
 * @date 2021/1/17 16:04
 */
@FeignClient("machine-agent")
public interface MachineService {

    /**
     * 查询设备各项数据最新状态
     *
     * @param qrcode 二维码
     * @return 设备最新数据
     */
    @GetMapping("/machine/{qrcode}/status")
    ResultData runningStatus(@PathVariable("qrcode") String qrcode);

    /**
     * 查询设备的定时配置
     *
     * @param qrcode 二维码
     * @return 设备定时数据
     */
    @GetMapping("/machine/power/onoff/get/record/by/code")
    ResultData getRecord(@RequestParam("qrcode") String qrcode);

    /**
     * 配置设备的定时
     *
     * @param qrcode 设备二维码
     * @param startHour 开始时间，小时
     * @param startMinute 开始时间，分钟
     * @param endHour 结束时间，小时
     * @param endMinute 结束时间，分钟
     * @param status 开启和关闭的状态
     * @return 配置结果
     */
    @PostMapping("/machine/power/onoff/confirm")
    ResultData configConfirm(@RequestParam("qrcode") String qrcode,
                             @RequestParam("startHour") int startHour, @RequestParam("startMinute") int startMinute,
                             @RequestParam("endHour") int endHour, @RequestParam("endMinute") int endMinute,
                             @RequestParam("status") boolean status);

    /**
     * 根据二维码查询machineId
     *
     * @param codeValue 二维码
     * @return machineId列表
     */
    @GetMapping("/machine/qrcode/findbyqrcode/consumer")
    ResultData findMachineIdByCodeValueFacetoConsumer(@RequestParam("codeValue") String codeValue);

    /**
     * 查看板子的版本
     *
     * @param qrcode 二维码
     * @return 板子的版本
     */
    @GetMapping("/machine/board/by/qrcode")
    ResultData findBoardVersionByQRcode(@RequestParam("qrcode") String qrcode);

}
