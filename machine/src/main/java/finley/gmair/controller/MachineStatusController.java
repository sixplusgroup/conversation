package finley.gmair.controller;

import finley.gmair.service.MachinePm25Service;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/machine/status")
public class MachineStatusController {

    @Autowired
    private MachinePm25Service machinePm25Service;

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

    }

}
