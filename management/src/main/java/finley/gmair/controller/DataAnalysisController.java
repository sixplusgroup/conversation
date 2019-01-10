package finley.gmair.controller;

import finley.gmair.service.DataAnalysisAgent;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/management/data/analysis")
public class DataAnalysisController {

    @Autowired
    private DataAnalysisAgent dataAnalysisAgent;

    @GetMapping("/machine/status/{statusType}/lastNday")
    public ResultData fetchLastNDayIndoorPm25(String qrcode, int lastNday, @PathVariable("statusType")String statusType) {
        return dataAnalysisAgent.fetchLastNDayData(qrcode, lastNday, statusType);
    }

    @GetMapping("/machine/status/{statusType}/lastNhour")
    public ResultData fetchLastNHourIndoorPm25(String qrcode, int lastNhour, @PathVariable("statusType")String statusType) {
        return dataAnalysisAgent.fetchLastNHourData(qrcode, lastNhour, statusType);
    }


}
