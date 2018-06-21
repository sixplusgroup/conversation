package finley.gmair.controller;

import finley.gmair.service.CoreService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This method is responsible to fetch response from machine-agent
 */
@RestController
@RequestMapping("/management/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private CoreService coreService;

    @GetMapping("/model/list")
    public ResultData modelList() {
        return machineService.modelList();
    }

    @GetMapping("/batch/list")
    public ResultData batchList(String modelId) {
        return machineService.batchList(modelId);
    }

    @PostMapping("/qrcode/check")
    public ResultData checkQrcode(String candidate) {
        return machineService.check(candidate);
    }

    @GetMapping("/core/repo/{machineId}/online")
    public ResultData online(@PathVariable("machineId") String machineId) {
        return coreService.isOnline(machineId);
    }
}
