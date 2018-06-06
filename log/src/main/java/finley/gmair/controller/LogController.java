package finley.gmair.controller;


import finley.gmair.form.log.MachineComLogForm;
import finley.gmair.form.log.SystemEventLogForm;
import finley.gmair.model.log.MachineComLog;
import finley.gmair.model.log.SystemEventLog;
import finley.gmair.service.LogService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    @GetMapping(value = {"/system/query/{module}", "/system/query"})
    public ResultData getModuleLog(@PathVariable(required = false) String module) {
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
}
