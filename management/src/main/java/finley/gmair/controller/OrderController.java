package finley.gmair.controller;

import finley.gmair.service.OrderNewService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 22:28
 * @description: OrderController
 */

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderNewService orderNewService;

    @PostMapping("/uploadAndSync")
    public ResultData uploadAndSync(@RequestParam MultipartFile file, @RequestParam String password) {
        return orderNewService.uploadAndSync(file, password);
    }

    @GetMapping(value = "/skuItem/list")
    public ResultData list() {
        return orderNewService.list();
    }

    @PostMapping(value = "/skuItem/update")
    public ResultData update(@RequestParam String itemId,
                             @RequestParam String machineModel,
                             @RequestParam boolean fictitious) {
        return orderNewService.update(itemId, machineModel, fictitious);
    }

    @PostMapping(value = "/skuItem/import")
    public ResultData manualImport() {
        return orderNewService.manualImport();
    }
}
