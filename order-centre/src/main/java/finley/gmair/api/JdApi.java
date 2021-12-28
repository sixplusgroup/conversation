package finley.gmair.api;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.response.search.OrderSearchInfo;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.request.order.PopOrderGetRequest;
import com.jd.open.api.sdk.request.order.PopOrderSearchRequest;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.jd.open.api.sdk.response.order.PopOrderGetResponse;
import com.jd.open.api.sdk.response.order.PopOrderSearchResponse;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 21:48
 * @description ：
 */

public class JdApi {
    private static final String SERVER_URL = "http://api.jd.com/routerjson";

    private final JdClient jdClient;

    private JdApi(JdClient jdClient) {
        this.jdClient = jdClient;
    }

    /**
     * 获取网络调用必须先setUp构建一个关于特定accessToken的client
     */
    public static JdApi setUp(String accessToken, String appKey, String appSecret) {
        return new JdApi(new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret));
    }


    public com.jd.open.api.sdk.domain.order.OrderQueryJsfService.response.get.OrderSearchInfo getOrderDetail(PopOrderGetRequest request) throws ApiCallException {
        //todo:重试
        try {
            PopOrderGetResponse response = jdClient.execute(request);
            return response.getOrderDetailInfo().getOrderInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getOrderTotal(PopOrderSearchRequest request) throws ApiCallException {
        //todo:重试
        try {
            PopOrderSearchResponse response = jdClient.execute(request);
            return response.getSearchorderinfoResult().getOrderTotal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<OrderSearchInfo> getOrderList(PopOrderSearchRequest request) throws ApiCallException {
        //todo:重试
        try {
            PopOrderSearchResponse response = jdClient.execute(request);
            return response.getSearchorderinfoResult().getOrderInfoList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends AbstractResponse> T execute(JdRequest<T> request) throws ApiCallException {
        try {
            return jdClient.execute(request);
        } catch (Exception e) {
            throw new ApiCallException(e.getMessage(), e.getCause());
        }
    }

    public <T extends AbstractResponse> T executeWithRetry(JdRequest<T> request, int retryTimes) throws ApiCallException {
        try {
            return jdClient.execute(request);
        } catch (Exception e) {
            throw new ApiCallException(e.getMessage(), e.getCause());
        }
    }

    public static class Constant {

        public static final String ORDER_GET_FIELDS = "orderId,venderId,orderType,payType," +
                "orderTotalPrice,orderSellerPrice,orderPayment,freightPrice,sellerDiscount," +
                "orderState,orderStateRemark,deliveryType,consigneeInfo,itemInfoList," +
                "invoiceEasyInfo,invoiceInfo,invoiceCode,orderRemark,orderStartTime,orderEndTime" +
                "couponDetailList,venderRemark,balancedUser,pin,returnOrder,paymentConfirmTime," +
                "waybill,logisticsId,vatInfo,modified,directParentOrderId,parentOrderId,customs," +
                "customsModel,orderSource,storeOrder,idSopShipmenttype,scDT,serviceFee,pauseBizInfo," +
                "taxFee,tuiHuoWuYou,storeId,realPin,orderMarkDesc,open_id,open_id_buyer";

        public static final long MAX_TIME_INTERVAL = 24 * 60 * 60 * 1000L;

        public static final String ORDER_SEARCH_FIELDS = "orderId";

        public static final int RETRY_TIMES = 3;

        public static final String ORDER_STATUS = "WAIT_SELLER_STOCK_OUT,WAIT_GOODS_RECEIVE_CONFIRM," +
                "WAIT_SELLER_DELIVERY,PAUSE,FINISHED_L,TRADE_CANCELED,LOCKED";
    }
}
