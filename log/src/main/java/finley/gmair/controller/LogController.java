package finley.gmair.controller;


import finley.gmair.form.log.*;
import finley.gmair.model.log.*;
import finley.gmair.service.LogService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * This method is used to add machineComLog in the system
     *
     * @return
     */
    @PostMapping("/machinecom/create")
    public ResultData addMachineComLog(MachineComLogForm form) {
        ResultData result = new ResultData();
        String uid = form.getUid().trim();
        String action = form.getAction().trim();
        String logDetail = form.getLogDetail().trim();
        String ip = form.getIp().trim();
        MachineComLog machineComLog = new MachineComLog(uid, action, logDetail, ip);
        ResultData response = logService.createMachineComLog(machineComLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to add machineComLog: ").append(uid).toString());
        }
        return result;
    }

    /**
     * This method is used to query machineComLog in the database
     *
     * @return
     */
    @GetMapping(value = {"/machinecom/query/{uid}", "/machinecom/query"})
    public ResultData queryMachineComLog(@PathVariable(required = false) String uid) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if(uid != null){
            condition.put("uid", uid);
        }
        ResultData response = logService.fetchMachineComLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }else{
            return response;
        }

    }

    /**
     * This method is used to add systemEventLog in the system
     *
     * @return
     */
    @PostMapping("/system/create")
    public ResultData createModuleLog(SystemEventLogForm logForm) {
        ResultData result = new ResultData();
        String module = logForm.getModule().trim();
        String eventStatus = logForm.getEventStatus().trim();
        long time = System.currentTimeMillis();
        String logDetail = logForm.getLogDetail().trim();
        String ip = logForm.getIp().trim();

        SystemEventLog systemEventLog = new SystemEventLog(module, eventStatus, time, logDetail, ip);
        ResultData response = logService.createModuleLog(systemEventLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to add machineComLog");
        }
        return result;
    }

    /**
     * This method is used to query module log in the database
     *
     * @return
     */
    @PostMapping(value = "/system/query")
    public ResultData getModuleLog(String module) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (module != null) {
            condition.put("module", module);
        }
        ResultData response = logService.fetchModuleLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query module log");
        }
        return result;
    }

    /**
     * This method is used to add user action log in the database
     *
     * @return
     */
    @PostMapping(value = "/useraction/create")
    public ResultData createUserActionLog(UserActionLogForm logForm) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(logForm.getUserId()) || StringUtils.isEmpty(logForm.getMachineValue()) ||
                StringUtils.isEmpty(logForm.getComponent()) || StringUtils.isEmpty(logForm.getLogDetail())
                || StringUtils.isEmpty(logForm.getIp()) || StringUtils.isEmpty(logForm.getActionValue())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        String userId = logForm.getUserId().trim();
        String machineValue = logForm.getMachineValue().trim();
        long time = System.currentTimeMillis();
        String component = logForm.getComponent().trim();
        String logDetail = logForm.getLogDetail().trim();
        String ip = logForm.getIp().trim();
        String actionValue = logForm.getActionValue();

        UserMachineOperationLog userActionLog = new UserMachineOperationLog(userId, machineValue, time, component, logDetail, ip, actionValue);
        ResultData response = logService.createUserActionLog(userActionLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to add user action log");
        }

        return result;
    }

    /**
     * This method is used to query user action log in the database
     *
     * @return
     */
    @PostMapping("/useraction/query")
    public ResultData getUserActionLog(String userId, String machineValue) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(userId)) {
            condition.put("userId", userId);
        }
        if (!StringUtils.isEmpty(machineValue)) {
            condition.put("machineValue", machineValue);
        }
        ResultData response = logService.fetchUserActionLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query user action log");
        }
        return result;
    }

    /**
     * This method is used to add server-machine in the database
     *
     * @return
     */
    @PostMapping(value = "/servermachine/create")
    public ResultData createServerMachineLog(Server2MachineLogForm logForm) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(logForm.getMachineValue()) || StringUtils.isEmpty(logForm.getComponent())
                || StringUtils.isEmpty(logForm.getLogDetail()) || StringUtils.isEmpty(logForm.getIp())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        String machineValue = logForm.getMachineValue().trim();
        String component = logForm.getComponent().trim();
        long time = System.currentTimeMillis();
        String logDetail = logForm.getLogDetail().trim();
        String ip = logForm.getIp().trim();

        Server2MachineLog server2MachineLog = new Server2MachineLog(machineValue, component, time, logDetail, ip);
        ResultData response = logService.createServer2MachineLog(server2MachineLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to add user server-machine log");
        }
        return result;
    }

    /**
     * This method is used to query server-machine log in the database
     *
     * @return
     */
    @PostMapping(value = "/servermachine/query")
    public ResultData getServerMachineLog(String machineValue) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(machineValue)) {
            condition.put("machineValue", machineValue);
        }
        ResultData response = logService.fetchServer2MachineLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query server-machine log");
        }
        return result;
    }

    @PostMapping(value = "/userlog/create")
    public ResultData createUserLog(UserLogForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getUserId()) || StringUtils.isEmpty(form.getComponent())
                || StringUtils.isEmpty(form.getLogDetail()) || StringUtils.isEmpty(form.getIp())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String userId = form.getUserId().trim();
        String component = form.getComponent().trim();
        String logDetail = form.getLogDetail().trim();
        String ip = form.getIp().trim();
        UserAccountOperationLog log = new UserAccountOperationLog(logDetail, ip, userId, component);
        ResultData response = logService.createUserLog(log);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store user log");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @PostMapping("/userlog/query")
    public ResultData getUserLog(String userId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(userId)) {
            condition.put("userId", userId);
        }
        ResultData response = logService.fetchUserLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query user log");
        }
        return result;
    }

    @PostMapping("/mqtt/ack/create")
    public ResultData createMqttAckLog(MqttAckLogForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getAckId()) || StringUtils.isEmpty(form.getCode())
                || StringUtils.isEmpty(form.getComponent()) || StringUtils.isEmpty(form.getMachineId())
                || StringUtils.isEmpty(form.getIp()) || StringUtils.isEmpty(form.getLogDetail())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String detail = form.getLogDetail().trim();
        String ip = form.getIp().trim();
        String ackId = form.getAckId().trim();
        String machineId = form.getMachineId().trim();
        int code = form.getCode();
        String component = form.getComponent().trim();
        MqttAckLog log = new MqttAckLog(detail, ip, ackId, machineId, code, component);
        ResultData response = logService.createMqttAckLog(log);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store mqtt ack log");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @PostMapping(value = "/mqtt/ack/query")
    public ResultData getMqttAckLog(String machineId, String ackId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(machineId)) {
            condition.put("machineId", machineId);
        }
        if (!StringUtils.isEmpty(ackId)) {
            condition.put("ackId", ackId);
        }
        ResultData response = logService.fetchMqttAckLog(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No mqtt ack log found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query mqtt ack log");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
        }
        return result;
    }

    @PostMapping(value = "/adminlog/create")
    public ResultData createAdminLog(UserLogForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getUserId()) || StringUtils.isEmpty(form.getComponent())
                || StringUtils.isEmpty(form.getLogDetail()) || StringUtils.isEmpty(form.getIp())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required");
            return result;
        }
        String userId = form.getUserId().trim();
        String component = form.getComponent().trim();
        String logDetail = form.getLogDetail().trim();
        String ip = form.getIp().trim();
        AdminAccountOperationLog log = new AdminAccountOperationLog(logDetail, ip, userId, component);
        ResultData response = logService.createAdminLog(log);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store admin log");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @PostMapping("/adminlog/query")
    public ResultData getAdminLog(String userId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(userId)) {
            condition.put("userId", userId);
        }
        ResultData response = logService.fetchAdminLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query admin log");
        }
        return result;
    }
}
