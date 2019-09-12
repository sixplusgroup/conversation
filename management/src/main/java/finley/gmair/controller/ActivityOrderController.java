package finley.gmair.controller;

import finley.gmair.service.ActivityOrderService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 535188589@qq.com
 * @date 2019/8/24
 */

@CrossOrigin
@RestController
@RequestMapping("/management/drift")
public class ActivityOrderController {

    @Autowired
    private ActivityOrderService activityOrderService;

    @GetMapping("order/list")
    public ResultData fetchOrderList(String startTime, String endTime, String provinceName, String cityName, String status){
        return activityOrderService.fetchOrderList(startTime,endTime,provinceName,cityName,status);
    }

    @GetMapping("order/list_all")
    public ResultData fetchOrderList(){
        return activityOrderService.fetchOrderList();
    }
}
