package finley.gmair;

import com.alibaba.fastjson.JSON;
import com.jd.open.api.sdk.request.alpha.FceAlphaGetVenderCarrierRequest;
import com.jd.open.api.sdk.request.order.PopOrderSearchRequest;
import com.jd.open.api.sdk.response.alpha.FceAlphaGetVenderCarrierResponse;
import com.jd.open.api.sdk.response.order.PopOrderSearchResponse;
import finley.gmair.api.ApiCallException;
import finley.gmair.api.JdApi;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-25 17:57
 * @description ：
 */

public class ApiTest {
    private static JdApi jdApi = JdApi.setUp("6861B1CF2C2FA7D763AA34F0254E6BAB",
            "f9f7f2f76f62497cb74875b930295ca9", "fd764b94465e45328315d1bf12029330riot");

    public static void main(String[] args) {
        getOrder();
    }

    private static void getLogistic() {
        FceAlphaGetVenderCarrierRequest request = new FceAlphaGetVenderCarrierRequest();
        try {
            FceAlphaGetVenderCarrierResponse response = jdApi.execute(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ApiCallException e) {
            e.printStackTrace();
        }
    }

    private static void getOrder() {
        PopOrderSearchRequest jdRequest = new PopOrderSearchRequest();
        jdRequest.setStartDate("2021-12-25 00:00:00");
        jdRequest.setEndDate("2021-12-26 00:00:00");
        jdRequest.setOptionalFields(JdApi.Constant.ORDER_SEARCH_FIELDS);
        jdRequest.setOrderState(JdApi.Constant.ORDER_STATUS);
        jdRequest.setPageSize("50");
        jdRequest.setPage("1");
        //排序方式，默认升序,1是降序,其它数字都是升序
        jdRequest.setSortType(1);
        //查询时间类型，0按修改时间查询，1为按订单创建时间查询
        jdRequest.setDateType(0);
        try {
            PopOrderSearchResponse response = jdApi.execute(jdRequest);
            System.out.println(JSON.toJSONString(response));
        } catch (ApiCallException e) {
            e.printStackTrace();
        }
    }
}
