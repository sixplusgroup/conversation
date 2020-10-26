package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.dto.CrmStatusDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zm
 * @date 2020/10/26 0026 11:03
 * @description 调用Crm系统提供的接口
 **/
@FeignClient(url = "${crm.url}", name = "crm")
public interface CrmAPIService {

    @PostMapping(value = "/update")
    JSONObject updateOrderStatus(
            @RequestBody CrmStatusDTO crmStatus
    );

    @PostMapping(value = "/add")
    JSONObject addNewOrder(
            @RequestBody CrmOrderDTO crmOrder
    );
}
