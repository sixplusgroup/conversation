package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
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
        if (statusType.equals("pm25")) {
            ResultData result = new ResultData();
            ResultData response = new ResultData();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("citypm25", null);
            jsonObject.put("outpm25", null);
            jsonObject.put("indoorpm25", null);
            try {
                response = machineService.getCitylastNdayData(qrcode, lastNday);
                jsonObject.put("citypm25", response.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                response = machineService.fetchLastNDayData(qrcode, lastNday);
                jsonObject.put("outpm25", response.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                response = dataAnalysisAgent.fetchLastNDayData(qrcode, lastNday, statusType);
                jsonObject.put("indoorpm25", response.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setData(jsonObject);
            return result;
        }
        return dataAnalysisAgent.fetchLastNDayData(qrcode, lastNday, statusType);
    }

    @GetMapping("/machine/status/{statusType}/lastNhour")
    public ResultData fetchLastNHourIndoorPm25(String qrcode, int lastNhour, @PathVariable("statusType") String statusType) {
        if (statusType.equals("pm25")) {
            ResultData result = new ResultData();
            ResultData response = new ResultData();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("citypm25", null);
            jsonObject.put("outpm25", null);
            jsonObject.put("indoorpm25", null);
            try {
                response = machineService.getCitylastNhourData(qrcode, lastNhour);
                jsonObject.put("citypm25", response.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                response = machineService.fetchLastNHourData(qrcode, lastNhour);
                jsonObject.put("outpm25", response.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                response = dataAnalysisAgent.fetchLastNHourData(qrcode, lastNhour, statusType);
                jsonObject.put("indoorpm25", response.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setData(jsonObject);
            return result;
        }
        return dataAnalysisAgent.fetchLastNHourData(qrcode, lastNhour, statusType);
    }


}
