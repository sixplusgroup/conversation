package finley.gmair.controller;


import finley.gmair.form.air.MachineAirQualityForm;
import finley.gmair.service.MachineAirQualityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airquality")
public class MachineAirQualityController {

    @Autowired
    private MachineAirQualityService machineAirQualityService;

    @RequestMapping(value = "/finley/gmair/machine/create", method = RequestMethod.POST)
    private ResultData createMachineAirQuality(MachineAirQualityForm form) {
        ResultData result = new ResultData();

        return result;
    }

}
