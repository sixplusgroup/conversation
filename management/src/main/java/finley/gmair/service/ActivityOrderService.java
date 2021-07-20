//package finley.gmair.service;
//
//import finley.gmair.util.ResultData;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * @author 535188589@qq.com
// * @date 2019/8/24
// */
//@FeignClient("drift-agent")
//public interface ActivityOrderService {
//    @GetMapping("/drift/order/list")
//    ResultData fetchOrderList(@RequestParam("startTime")String startTime,@RequestParam("endTime")String endTime,@RequestParam("provinceName") String provinceName,@RequestParam("cityName") String cityName,@RequestParam("status") String status);
//
//    @GetMapping("/drift/order/list")
//    ResultData fetchOrderList();
//}
