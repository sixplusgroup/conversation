package finley.gmair.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zm
 * @date 2020/10/26 0026 11:03
 * @description 调用Crm系统提供的接口
 **/
@FeignClient(url = "${crm.url}", name = "crm")
public interface CrmAPIService {

    @PostMapping(value = "/update")
    Object updateOrderStatus(
            @RequestParam("param") String param
    );

    @PostMapping(value = "/add")
    Object createNewOrder(
            @RequestParam("param") String param
    );
}
