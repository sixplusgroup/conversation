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
    public ResultData uploadAndSync(@RequestParam MultipartFile file, @RequestParam String password){
        return orderNewService.uploadAndSync(file, password);
    }

}
