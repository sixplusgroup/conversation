package finley.gmair.controller;

import finley.gmair.model.mqttManagement.MachineAlert;
import finley.gmair.service.MachineAlertService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备警报处理
 *
 * @author lycheeshell
 */
@RestController
@RequestMapping("/mqtt")
public class MachineController {

    @Resource
    private MachineAlertService machineAlertService;

    @PostMapping(value = "/alert/create")
    public ResultData createAlert(String machineId, int code, String msg) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineId) || StringUtils.isEmpty(code) || StringUtils.isEmpty(msg)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }
        return ResultData.ok(machineAlertService.createMachineAlert(machineId, code, msg));
    }

    @GetMapping(value = "/alert/query")
    public ResultData getAlert() {
        List<MachineAlert> machineAlertList = machineAlertService.queryMachineAlert(null, null, null);
        if (machineAlertList == null || machineAlertList.size() == 0) {
            return ResultData.empty("No machine alert found");
        } else {
            return ResultData.ok(machineAlertList);
        }
    }

    @GetMapping(value = "/alert/getExistingAlert")
    public ResultData getExistingAlert(String machineId, Integer code) {
        List<MachineAlert> machineAlertList = machineAlertService.queryMachineAlert(machineId, code, false);
        if (machineAlertList == null || machineAlertList.size() == 0) {
            return ResultData.empty("No machine alert found");
        } else {
            return ResultData.ok(machineAlertList);
        }
    }

    @PostMapping(value = "/alert/update")
    public ResultData updateAlert(String machineId, int code) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineId) || StringUtils.isEmpty(code)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Can't update without enough parameters");
            return result;
        }
        int line = machineAlertService.updateMachineAlert(machineId, code);
        if (line == 0) {
            return ResultData.error("Fail to get alert");
        } else if (line == 1) {
            return ResultData.ok(null, "Update single alert successfully");
        } else {
            return ResultData.ok(null, "Update batch alert successfully");
        }
    }
}
