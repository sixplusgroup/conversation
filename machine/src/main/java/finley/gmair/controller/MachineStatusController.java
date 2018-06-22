package finley.gmair.controller;

import finley.gmair.service.CoreService;
import finley.gmair.service.MachinePm25Service;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/status")
public class MachineStatusController {

    @Autowired
    private MachinePm25Service machinePm25Service;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private CoreService coreService;

    @PostMapping("/schedule/hourly")
    ResultData handleMachineStatusHourly() {
        return machinePm25Service.handleHourly();
    }

    @PostMapping("/schedule/daily")
    ResultData handleMachineStatusDaily() {
        return machinePm25Service.handleDaily();
    }

    @PostMapping("/schedule/monthly")
    ResultData handleMachineStatusMonthly() {
        return machinePm25Service.handleMonthly();
    }

    @GetMapping("/isonline/{qrcode}")
    ResultData isOnline(@PathVariable String qrcode) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);

        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(String.format("cannot find the qrcode %s", qrcode));
            return result;
        } else {
            MachineQrcodeBindVo machineQrcodeBindVo = ((List<MachineQrcodeBindVo>) response.getData()).get(0);
            return coreService.isOnline(machineQrcodeBindVo.getMachineId());
        }
    }

}
