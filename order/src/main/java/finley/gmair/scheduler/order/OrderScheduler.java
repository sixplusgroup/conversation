package finley.gmair.scheduler.order;

import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderScheduler {

    @Autowired
    private OrderService orderService;

    /**
     * This method is used to relocate the location information for platform order
     * once retrieved by the method, the counter will grow
     * the maximum try out times is 10
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void locate() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("incomplete", true);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchPlatformOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {

        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            //nothing to be handled this time
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            //suppose to be something wrong on the server side
        }
    }
}
