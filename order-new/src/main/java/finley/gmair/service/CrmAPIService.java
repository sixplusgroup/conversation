package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zm
 * @date 2020/10/26 0026 11:03
 * @description Crm系统提供的接口
 **/
@FeignClient(url = "${crm.url}", name = "crm")
public interface CrmAPIService {

    /**
     *
     * @param param: {
     *    "ddh":"123456789",
     *    "lxfs":"138139123456",
     *    "billstat":"4"
     * }
     * @return 返回数据:{
     *    "ResponseCode": "RESPONSE_OK",
     *    "Description": "订单状态更新成功！"
     * }
     */
    @PostMapping(value = "/update")
    JSONObject updateOrderStatus(
            @RequestParam("param") String param
    );

    /**
     *
     * @param param: {
     * 	  "qdly": "58",
     * 	  "jqxh": "GM420",
     * 	  "ddh": "123456789",
     * 	  "sl": "1",
     * 	  "xdrq": "2020-10-15",
     * 	  "ssje": "3499",
     * 	  "yhxm": "张三",
     * 	  "lxfs": "138139123456",
     * 	  "dq": "杭州",
     * 	  "dz": "浙江省 杭州市 滨江区 长河街道月明路倾城之恋20-1-2501",
     * 	  "billstat": "1",
     * 	  "messagestat": "0",
     * 	  "initflag": "1"
     * }
     * @return 返回数据: {
     *    "ResponseCode": "RESPONSE_ERROR",
     *    "Description": "错误，存在相同的订单信息！"
     * }
     */
    @PostMapping(value = "/add")
    JSONObject createNewOrder(
            @RequestParam("param") String param
    );
}
