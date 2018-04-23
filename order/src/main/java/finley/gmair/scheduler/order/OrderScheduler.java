package finley.gmair.scheduler.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.location.OrderLocationRetryCount;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.LocationService;
import finley.gmair.service.OrderLocationRetryCountService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.location.OrderLocationRetryCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderScheduler {

    private static final int MAX_RETRY_COUNT = 10;

    @Autowired
    private OrderLocationRetryCountService orderLocationRetryCountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationService locationService;

    /**
     * This method is used to relocate the location information for platform order
     * once retrieved by the method, the counter will grow
     * the maximum try out times is 10
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void locate() {
        System.out.println("start schedule....");
        Map<String, Object> condition = new HashMap<>();
        condition.put("retryCountLT", MAX_RETRY_COUNT);
        condition.put("blockFlag", false);
        ResultData response = orderLocationRetryCountService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            // if remain incomplete orders
            List<OrderLocationRetryCountVo> list = (List<OrderLocationRetryCountVo>) response.getData();
            for (OrderLocationRetryCountVo retryCount : list) {
                String orderId = retryCount.getOrderId();
                condition.clear();
                condition.put("blockFlag", false);
                condition.put("orderId", orderId);
                response = orderService.fetchPlatformOrder(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    // 1. get the order and address
                    PlatformOrder order = ((List<PlatformOrder>) response.getData()).get(0);
                    String address = order.getAddress();
                    // 2. resolve address
                    ResultData locationResult = locationService.geocoder(address);
                    try {
                        if (locationResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            JSONObject location = (JSON.parseObject(JSON.toJSONString(locationResult.getData()))).getJSONObject("address_components");
                            String province = location.getString("province");
                            String city = location.getString("city");
                            String district = location.getString("district");
                            PlatformOrder orderUpdate = new PlatformOrder();
                            orderUpdate.setLocation(province, city, district);
                            orderUpdate.setTotalPrice(order.getTotalPrice());
                            orderUpdate.setStatus(order.getStatus());
                            orderUpdate.setOrderId(order.getOrderId());
                            response = orderService.updatePlatformOrder(orderUpdate);
                            // 3. if resolve address successfully, delete retryCount record
                            response = updateRetryCount(retryCount.getOrderId(), retryCount.getRetryCount() + 1);
                            response = orderLocationRetryCountService.delete(condition);
                        } else {
                            // if resolve failed, update the retry count
                            response = updateRetryCount(retryCount.getOrderId(), retryCount.getRetryCount() + 1);
                        }
                    } catch (Exception e) {
                        // encounter exception
                        response = updateRetryCount(retryCount.getOrderId(), retryCount.getRetryCount() + 1);
                    }
                }
            }
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            //nothing to be handled this time
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            //suppose to be something wrong on the server side
        }
    }

    private ResultData updateRetryCount(String orderId, int retryCount) {
        OrderLocationRetryCount orderLocationRetryCount = new OrderLocationRetryCount();
        orderLocationRetryCount.setRetryCount(retryCount);
        orderLocationRetryCount.setOrderId(orderId);
        orderLocationRetryCount.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return orderLocationRetryCountService.update(orderLocationRetryCount);
    }
}
