package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.dataAnalysis.*;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data/analysis/machine/status")
public class MachineStatusController {

    @Autowired
    private MachineStatusService machineStatusService;

    @Autowired
    private IndoorPm25Service indoorPm25Service;

    @Autowired
    private VolumeService volumeService;

    @Autowired
    private HumidService humidService;

    @Autowired
    private TempService tempService;

    @Autowired
    private Co2Service co2Service;

    @Autowired
    private PowerService powerService;

    @Autowired
    private HeatService heatService;

    @Autowired
    private ModeService modeService;

    private Logger logger = LoggerFactory.getLogger(MachineStatusController.class);

    @GetMapping("/probe/statistical/lasthour")
    public ResultData probeStatisticalData() {
        ResultData result = new ResultData();
        ResultData response = machineStatusService.getHourlyStatisticalData();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            return result;
        }
        return result;
    }

    @PostMapping("/schedule/statistical/hourly")
    public ResultData statisticalDataHourly() {
        ResultData result = new ResultData();
        ResultData response = machineStatusService.getHourlyStatisticalData();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("统计时出错");
            return result;
        }
        List<Object> statisticalDataList = (List<Object>) response.getData();
        List<JSONObject> dataList = statisticalDataList.stream().map(e -> JSONObject.parseObject(e.toString())).collect(Collectors.toList());

        List<IndoorPm25Hourly> pm25HourlyList = dataList.stream().map(e -> new IndoorPm25Hourly(e.getString("machineId"),e.getDouble("averagePm25"),e.getIntValue("maxPm25"),e.getIntValue("minPm25"))).collect(Collectors.toList());
        List<VolumeHourly> volumeHourlyList = dataList.stream().map(e -> new VolumeHourly(e.getString("machineId"),e.getDouble("averageVolume"),e.getIntValue("maxVolume"),e.getIntValue("minVolume"))).collect(Collectors.toList());
        List<TempHourly> tempHourlyList =  dataList.stream().map(e -> new TempHourly(e.getString("machineId"),e.getDouble("averageTemp"),e.getIntValue("maxTemp"),e.getIntValue("minTemp"))).collect(Collectors.toList());
        List<Co2Hourly>  co2HourlyList =  dataList.stream().filter(e -> e.getString("averageCo2")!= null).map(e -> new Co2Hourly(e.getString("machineId"),e.getDouble("averageCo2"),e.getIntValue("maxCo2"),e.getIntValue("minCo2"))).collect(Collectors.toList());
        List<HumidHourly> humidHourlyList =  dataList.stream().map(e -> new HumidHourly(e.getString("machineId"),e.getDouble("averageHumid"),e.getIntValue("maxHumid"),e.getIntValue("minHumid"))).collect(Collectors.toList());
        List<PowerHourly> powerHourlyList =  dataList.stream().map(e -> new PowerHourly(e.getString("machineId"),e.getIntValue("powerOnMinute"),e.getIntValue("powerOffMinute"))).collect(Collectors.toList());
        List<HeatHourly> heatHourlyList = dataList.stream().map(e -> new HeatHourly(e.getString("machineId"),e.getIntValue("heatOnMinute"),e.getIntValue("heatOffMinute"))).collect(Collectors.toList());
        List<ModeHourly> modeHourlyList = dataList.stream().map(e -> new ModeHourly(e.getString("machineId"),e.getIntValue("manualMinute"),e.getIntValue("cosyMinute"),e.getIntValue("warmMinute"))).collect(Collectors.toList());

        ResultData response1 = indoorPm25Service.insertBatchHourly(pm25HourlyList);
        ResultData response2 = volumeService.insertBatchHourly(volumeHourlyList);
        ResultData response3 = tempService.insertBatchHourly(tempHourlyList);
        ResultData response4 = co2Service.insertBatchHourly(co2HourlyList);
        ResultData response5 = humidService.insertBatchHourly(humidHourlyList);
        ResultData response6 = powerService.insertBatchHourly(powerHourlyList);
        ResultData response7 = heatService.insertBatchHourly(heatHourlyList);
        ResultData response8 = modeService.insertBatchHourly(modeHourlyList);

        List<Object> resultList = new ArrayList<>();
        resultList.add(response1.getData());
        resultList.add(response2.getData());
        resultList.add(response3.getData());
        resultList.add(response4.getData());
        resultList.add(response5.getData());
        resultList.add(response6.getData());
        resultList.add(response6.getData());
        resultList.add(response7.getData());
        resultList.add(response8.getData());
        result.setData(resultList);
        result.setDescription("success to insert statistical machine data hourly into database ");
        return result;
    }

    @PostMapping("/schedule/statistical/daily")
    public ResultData statisticalDataDaily() {
        ResultData result = new ResultData();
        machineStatusService.handleDailyStatisticalData();
        return result;
    }
}
