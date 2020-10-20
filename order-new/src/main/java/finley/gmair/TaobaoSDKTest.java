package finley.gmair;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.TopAuthTokenRefreshRequest;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.TopAuthTokenRefreshResponse;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/12 14:50
 * @description ：
 */

public class TaobaoSDKTest {
    private static final String url = "https://eco.taobao.com/router/rest";
    private static final String appkey = "31404926";
    private static final String secret = "56539bd811035339bf55fe93383ef615";
    private static final String sessionKey = "6100e02ceb111ceb4e6ff02506458185b2f7afa5fce9f232200642250842";
    private static final String fields = "orders,tid,num_iid,num,status,type,shipping_type,trade_from,step_trade_status,buyer_rate,created,modified," +
            "pay_time,consign_time,end_time,receiver_name,receiver_state,receiver_address,receiver_zip,receiver_mobile,receiver_phone," +
            "receiver_country,receiver_city,receiver_district,receiver_town,tmall_delivery,cn_service,delivery_cps,cutoff_minutes," +
            "delivery_time,collect_time,dispatch_time,sign_time,es_time,price,total_fee,payment,adjust_fee,received_payment," +
            "discount_fee,post_fee,credit_card_fee,step_paid_fee";

    public static void main(String[] args) throws ApiException {
        getTrades();
    }

    private static void getTrades() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        //req.setFields("tid,type,status,payment,orders,rx_audit_status,created,modified");
        req.setFields(fields);
        req.setStartCreated(StringUtils.parseDateTime("2020-09-01 00:00:00"));
        req.setEndCreated(StringUtils.parseDateTime("2020-10-11 23:59:59"));
        req.setPageNo(1L);
        req.setPageSize(10L);
        req.setUseHasNext(true);
        TradesSoldGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
            List<Trade> tradeList = rsp.getTrades();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    private static void getIncrementTrades() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
        req.setFields("tid,type,status,payment,orders,rx_audit_status,created,modified");
        req.setStartModified(StringUtils.parseDateTime("2020-09-01 12:00:00"));
        req.setEndModified(StringUtils.parseDateTime("2020-09-01 13:59:59"));
        req.setPageNo(1L);
        req.setPageSize(40L);
        req.setUseHasNext(true);
        TradesSoldIncrementGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    private static void getItemsOnSale() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
        req.setHasShowcase(true);
        req.setFields("num_iid,cid,props,title,price,num,list_time,delist_time");
        ItemsOnsaleGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }
}
