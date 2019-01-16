package finley.gmair.controller;

import finley.gmair.service.DataAnalysisAgent;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/management/data/analysis")
public class DataAnalysisController {

    @Autowired
    private DataAnalysisAgent dataAnalysisAgent;

    @Autowired
    private MachineService machineService;

    @GetMapping("/machine/status/{statusType}/lastNday")
    public ResultData fetchLastNDayIndoorPm25(String qrcode, int lastNday, @PathVariable("statusType") String statusType) {
        if (statusType.equals("outpm25"))
            return machineService.fetchLastNDayData(qrcode, lastNday);
        return dataAnalysisAgent.fetchLastNDayData(qrcode, lastNday, statusType);
    }

    @GetMapping("/machine/status/{statusType}/lastNhour")
    public ResultData fetchLastNHourIndoorPm25(String qrcode, int lastNhour, @PathVariable("statusType") String statusType) {
        if (statusType.equals("outpm25"))
            return machineService.fetchLastNHourData(qrcode, lastNhour);
        return dataAnalysisAgent.fetchLastNHourData(qrcode, lastNhour, statusType);
    }


}
