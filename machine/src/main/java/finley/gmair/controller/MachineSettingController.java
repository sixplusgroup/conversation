package finley.gmair.controller;

import finley.gmair.service.MachineSettingService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/machine/setting")
public class MachineSettingController {

    @Autowired
    private MachineSettingService machineSettingService;

    @PostMapping("/powerAction/list")
    public ResultData powerActionList() {
        return machineSettingService.fetchPowerActionMachine();
    }
}
