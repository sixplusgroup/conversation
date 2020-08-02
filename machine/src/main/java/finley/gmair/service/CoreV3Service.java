package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("corev3-agent")
public interface CoreV3Service {
    /*查询设备的在线状态*/
    @GetMapping("/core/repo/{machineId}/online")
    ResultData isOnline(@PathVariable("machineId") String machineId);

    /*控制设备的电源状态*/
    @PostMapping("/core/com/control")
    ResultData configPower(@RequestParam("uid") String uid, @RequestParam("power") Integer power);

    /*控制设备的运行风量*/
    @PostMapping("/core/com/control")
    ResultData configSpeed(@RequestParam("uid") String uid, @RequestParam("speed") Integer speed);

    /*控制设备的辅热状态*/
    @PostMapping("/core/com/control")
    ResultData configHeat(@RequestParam("uid") String uid, @RequestParam("heat") Integer heat);

    /*控制设备的运行模式*/
    @PostMapping("/core/com/control")
    ResultData configMode(@RequestParam("uid") String uid, @RequestParam("mode") Integer mode);

    /*控制设备的童锁*/
    @PostMapping("/core/com/control")
    ResultData configLock(@RequestParam("uid") String uid, @RequestParam("childlock") Integer childlock);

    /*控制设备的亮度*/
    @PostMapping("/core/com/control")
    ResultData configLight(@RequestParam("uid") String uid, @RequestParam("light") Integer light);

    /*控制设备的滤网灯*/
    @PostMapping("/core/com/set/screen")
    ResultData configScreen(@RequestParam("uid") String uid, @RequestParam("valid") Integer valid);

    /*重置设备滤芯剩余寿命*/
    @PostMapping("/core/com/set/surplus")
    ResultData resetSurplus(@RequestParam("uid") String uid, @RequestParam("remain") Integer remain);
}
