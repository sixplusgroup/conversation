package finley.gmair.controller;

import finley.gmair.service.MachineService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/map/machine")
public class MachineController {
    @Autowired
    private MachineService machineService;

    @GetMapping("/fetch")
    public ResultData fetchMachineLatLngList(){
        return machineService.fetchMachineLatLngList();
    }
}
