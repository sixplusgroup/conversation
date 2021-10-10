package finley.gmair.service.feign;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Joby
 */
@FeignClient("gmair-shop-api")
public interface ShopOrderService {

    @PostMapping("/p/order/payed")
    ResponseEntity<Void> updateOrderPayed (@RequestParam("orderId") String orderId);

}
