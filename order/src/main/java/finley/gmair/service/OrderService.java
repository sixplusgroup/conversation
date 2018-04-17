package finley.gmair.service;

import finley.gmair.model.order.PlatformOrder;
import finley.gmair.util.ResultData;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface OrderService {
    ResultData fetchPlatformOrder(Map<String, Object> condition);

    ResultData createPlatformOrder(PlatformOrder order);

    ResultData updatePlatformOrder(PlatformOrder order);

    ResultData process(MultipartFile file);
}
