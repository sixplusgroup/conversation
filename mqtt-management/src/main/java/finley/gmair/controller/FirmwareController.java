package finley.gmair.controller;

import finley.gmair.model.mqttManagement.Firmware;
import finley.gmair.service.FirmwareService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 固件版本交互接口处理
 *
 * @author lycheeshell
 * @date 2021/02/08 14:50
 */
@RestController
@RequestMapping("/core/firmware")
public class FirmwareController {

    @Resource
    private FirmwareService firmwareService;

    /**
     * 新增固件版本信息
     *
     * @param version 版本号
     * @param link 下载链接
     * @param model 设备型号
     * @return 新增的固件
     */
    @PostMapping(value = "/create")
    public ResultData createFirmware(String version, String link, String model) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(version) || StringUtils.isEmpty(link)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Firmware firmware = new Firmware(version, link, model);
        ResultData response = firmwareService.create(firmware);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create firmware");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * 查询固件
     *
     * @param version 版本号
     * @param model 设备型号
     * @return 固件列表
     */
    @GetMapping(value = "/query")
    public ResultData getFirmware(String version, String model) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("firmwareVersion", version);
        condition.put("firmwareModel", model);
        ResultData response = firmwareService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No firmware get");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get firmware");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}
