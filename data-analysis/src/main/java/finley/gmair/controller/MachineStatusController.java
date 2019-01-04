package finley.gmair.controller;

import finley.gmair.service.MachineStatusService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/analysis/machine/status")
public class MachineStatusController {

    @Autowired
    private MachineStatusService machineStatusService;

    @GetMapping("/probe/statistical/lasthour")
    public ResultData probeStatisticalData(){
        ResultData result = new ResultData();
        ResultData response = machineStatusService.getHourlyStatisticalData();
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setData(response.getData());
            return result;
        }
        return result;
    }
}
