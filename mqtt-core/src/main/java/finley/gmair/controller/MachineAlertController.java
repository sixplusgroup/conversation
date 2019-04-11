package finley.gmair.controller;

import finley.gmair.model.mqtt.MachineAlert;
import finley.gmair.service.MachineAlertService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mqtt/alert")
public class MachineAlertController {

    @Autowired
    private MachineAlertService machineAlertService;

    @PostMapping("/create")
    public ResultData createAlert(String machineId, int code, String msg) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineId) || StringUtils.isEmpty(code) || StringUtils.isEmpty(msg)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        MachineAlert alert = new MachineAlert(machineId, code, msg);
        ResultData response = machineAlertService.create(alert);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store machine alert");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @GetMapping("/query")
    public ResultData getAlert() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = machineAlertService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine alert found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query machine alert");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping("/update")
    public ResultData updateAlert(String machineId, int code) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineId) || StringUtils.isEmpty(code)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Can't update without enough parameters");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("machineId", machineId);
        condition.put("alertStatus", false);
        condition.put("alertCode", code);
        ResultData response = machineAlertService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get alert");
            return result;
        }
        List<MachineAlert> list = (List<MachineAlert>) response.getData();
        if (list.size() == 1) {
            MachineAlert alert = list.get(0);
            condition.clear();
            condition.put("alertStatus", true);
            condition.put("alertId", alert.getAlertId());
            response = machineAlertService.modifySingle(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to update single alert");
                return result;
            }
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Update single alert successfully");
            return result;
        } else {
            condition.clear();
            condition.put("list", list);
            condition.put("alertStatus", true);
            response = machineAlertService.modifyBatch(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to update batch alert");
                return result;
            }
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Update batch alert successfully");
            return result;
        }
    }
}
