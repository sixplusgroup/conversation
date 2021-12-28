package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 23:52
 * @description ：
 */

@FeignClient(url = "${crm.url}", name = "crm")
public interface CrmClient {
    /**
     * @param param: {
     *               "ddh":"123456789",
     *               "lxfs":"138139123456",
     *               "billstat":"4"
     *               }
     * @return JSONObject:{
     * "ResponseCode": "RESPONSE_OK",
     * "Description": "订单状态更新成功！"
     * }
     */
    @PostMapping(value = "/update")
    JSONObject syncUpdate(@RequestParam("param") String param);

    /**
     * @param param: {
     *               "qdly": "58",
     *               "jqxh": "GM420",
     *               "ddh": "123456789",
     *               "sl": "1",
     *               "xdrq": "2020-10-15",
     *               "ssje": "3499",
     *               "yhxm": "张三",
     *               "lxfs": "138139123456",
     *               "dq": "杭州",
     *               "dz": "浙江省 杭州市 滨江区 长河街道月明路倾城之恋20-1-2501",
     *               "billstat": "1",
     *               "messagestat": "0",
     *               "initflag": "1",
     *               "sellermes": "卖家留言",
     *               "buyermes": "买家留言",
     *               "billentry":""
     *               }
     * @return 返回数据: {
     * "ResponseCode": "RESPONSE_ERROR",
     * "Description": "错误，存在相同的订单信息！"
     * }
     */
    @PostMapping(value = "/add")
    JSONObject syncCreate(@RequestParam("param") String param);
}
