package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author zm
 * @date 2020/10/26 0026 11:03
 * @description 调用Crm系统提供的接口
 **/
@FeignClient(url = "${crm.url}", name = "crm")
public interface CrmAPIService {

    @PostMapping(value = "/update")
    Map<String, Object> updateOrderStatus(
            @RequestParam("param") String param
    );

    @PostMapping(value = "/add")
    Map<String, Object> createNewOrder(
            @RequestParam("param") String param
    );
}
