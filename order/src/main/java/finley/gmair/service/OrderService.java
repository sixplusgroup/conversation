package finley.gmair.service;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.util.ResultData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface OrderService {
    ResultData fetchPlatformOrderChannel(Map<String, Object> condition);

    ResultData createPlatformOrderChannel(OrderChannel channel);

    ResultData updatePlatformOrderChannel(OrderChannel channel);

    ResultData process(MultipartFile file);
}
