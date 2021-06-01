package finley.gmair.scene.client;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Lyy
 * @create : 2021-01-13 20:35
 **/
@FeignClient("log-agent")
@RequestMapping("/log")
public interface LogClient {

    /**
     * 用户获取操作设备的日志
     *
     * @param userId 用户ID（即consumerId）
//     * @param qrcode 设备
     * @return 结果
     */
    @PostMapping(value = "/useraction/query")
    public ResultData getUserActionLog(@RequestParam(value = "userId") String userId,
//                                        @RequestParam(value = "machineValue") String qrcode,
                                       @RequestParam(value = "pageIndex") int pageIndex,
                                       @RequestParam(value = "pageSize") int pageSize);

}
