package finley.gmair.controller;


import finley.gmair.form.log.MachineComLogForm;
import finley.gmair.model.log.MachineComLog;
import finley.gmair.service.LogService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/machinecom/query/{uid}")
    public ResultData queryMachineComLog(@PathVariable String uid) {
        ResultData result = new ResultData();
        ResultData response = logService.fetchMachineComLog(uid);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        } else {
            return response;
        }

    }

    /**
     * This method is used to query all machineComLogs in the database
     *
     * @return
     */
    @GetMapping("/machinecom/query")
    public ResultData queryAllMachineComLog() {
        ResultData result = new ResultData();
        ResultData response = logService.fetchgAllMachineComLog();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        } else {
            return response;
        }

    }

    @GetMapping("/machinecom/query/{uid}")
    public ResultData machineComLog(@PathVariable("uid") String uid) {
        ResultData result = new ResultData();
        if (uid != null) {

        }

        return result;
    }
}
