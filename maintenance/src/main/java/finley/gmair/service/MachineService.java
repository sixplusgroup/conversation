package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
