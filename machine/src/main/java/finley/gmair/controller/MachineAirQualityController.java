package finley.gmair.controller;


import finley.gmair.form.air.MachineAirQualityForm;
import finley.gmair.model.air.MachineAirQuality;
import finley.gmair.model.machine.v2.MachineStatus;
import finley.gmair.service.MachineAirQualityService;
import finley.gmair.service.MachineStatusCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/machine")
public class MachineAirQualityController {

    @Autowired
    private MachineAirQualityService machineAirQualityService;

    @Autowired
    private MachineStatusCacheService machineStatusCacheService;

    @RequestMapping(value = "/qirquality/create", method = RequestMethod.POST)
    private ResultData createMachineAirQuality(MachineAirQualityForm form) {
        ResultData result = new ResultData();

        if (form.getQrcode() == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("parameter qrcode is required");
            return result;
        }

        MachineAirQuality machineAirQuality = new MachineAirQuality();
        machineAirQuality.setQrcode(form.getQrcode());
        machineAirQuality.setPm25(form.getPm25());
        machineAirQuality.setHumidity(form.getHumidity());
        machineAirQuality.setTemperature(form.getTemperature());
        machineAirQuality.setCo2(form.getCo2());

        ResultData response = machineAirQualityService.add(machineAirQuality);

        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(response.getData());
        }

        return result;
    }

    @RequestMapping (value = "/machinestatus/create", method = RequestMethod.POST)
    public MachineStatus createMachineStatus(MachineStatus machineStatus) {
        return machineStatusCacheService.generate(machineStatus);
    }

    @RequestMapping (value = "/machinestatus/{uid}", method = RequestMethod.GET)
    public MachineStatus MachineStatus(@PathVariable("uid") String uid) {
        return machineStatusCacheService.fetch(uid);
    }
}
