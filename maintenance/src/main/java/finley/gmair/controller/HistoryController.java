package finley.gmair.controller;

import finley.gmair.service.LogService;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户操作历史的交互接口
 *
 * @author lycheeshell
 * @date 2021/1/18 23:18
 */
@RestController
@RequestMapping("/maintenance/history")
public class HistoryController {

    @Resource
    private LogService logService;

    /**
     * 查询用户设备的操作历史
     *
     * @param consumerId 用户电话
     * @param qrcode     设备二维码
     * @return 用户设备操作历史
     */
    @GetMapping(value = "/getOperationHistory")
    public ResultData getOperationHistory(String consumerId, String qrcode) {
        if (StringUtils.isEmpty(consumerId)) {
            return ResultData.error("consumerId为空");
        }
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }

        return logService.getUserActionLog(consumerId, qrcode);
    }

}
