package finley.gmair.controller;

import finley.gmair.service.RedisService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fan
 * @date 2019/6/13 11:30 AM
 */
@RestController
@RequestMapping("/core/repo")
public class RepositoryController {

    @Resource
    private RedisService redisService;

    @GetMapping("/{machineId}/online")
    public ResultData isOnline(@PathVariable("machineId") String machineId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供设备的MAC地址");
            return result;
        }
        if (!redisService.exists(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前尚未发现该设备");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("该设备目前已联网");
        return result;
    }
}
