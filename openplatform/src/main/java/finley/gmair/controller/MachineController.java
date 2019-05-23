package finley.gmair.controller;

import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: MachineController
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/7 2:00 PM
 */
@RestController
@RequestMapping("/openplatform/machine")
public class MachineController {
    private Logger logger = LoggerFactory.getLogger(MachineController.class);

    @Autowired
    private MachineService machineService;

    /**
     * 获取设备的状态信息
     *
     * @param appid
     * @param qrcode
     * @return
     */
    @GetMapping("/indoor")
    public ResultData indoor(String appid, String qrcode) {
        ResultData result = new ResultData();
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅了该设备二维码");
            return result;
        }
        ResultData response = machineService.indoor(qrcode);
        return result;
    }

    /**
     * 获取设备所处城市的空气信息
     *
     * @param appid
     * @param qrcode
     * @return
     */
    @GetMapping("/outdoor")
    public ResultData outdoor(String appid, String qrcode) {
        ResultData result = new ResultData();
        if (!prerequisities(appid, qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保该appid有效，且已订阅了该设备二维码");
            return result;
        }
        ResultData response = machineService.outdoor(qrcode);
        return result;
    }

    private boolean prerequisities(String appid, String qrcode) {
        //判断appid是否合法

        //检查该appid是否可以查看该qrcode

        return true;
    }
}
