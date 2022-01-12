package finley.gmair;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import finley.gmair.model.ordernew.SkuItem;

import java.util.ArrayList;
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
        getSkuList();
    }

    private static void getTrades() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        //req.setFields("tid,type,status,payment,orders,rx_audit_status,created,modified");
        req.setFields(fields);
        //req.setStartCreated(StringUtils.parseDateTime("2020-11-04 19:20:00"));
        //req.setEndCreated(StringUtils.parseDateTime("2020-11-04 23:59:59"));
        req.setStatus("TRADE_NO_CREATE_PAY");
        req.setPageNo(1L);
        req.setPageSize(40L);
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
        req.setStartModified(StringUtils.parseDateTime("2020-11-03 12:00:00"));
        req.setEndModified(StringUtils.parseDateTime("2020-11-03 14:59:59"));
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
        System.out.println(rsp.getRequestUrl());
    }

    private static void getItemsOnSale() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
        req.setFields("num_iid,cid,props,title,price,num,list_time,delist_time");
        ItemsOnsaleGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    private static void getSkuList() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        ItemsOnsaleGetRequest request = new ItemsOnsaleGetRequest();
        request.setFields("num_iid");
        ItemsOnsaleGetResponse response = null;
        try {
            response = client.execute(request, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        assert response != null;
        List<Item> itemList = response.getItems();
        List<Item> completeItemList = Lists.newArrayList();
        //通过API获取skuItemList
        List<SkuItem> skuItemList = new ArrayList<>();
        for (Item item : itemList) {
            ItemSellerGetRequest req = new ItemSellerGetRequest();
            req.setNumIid(item.getNumIid());
            req.setFields("num_iid,title,sku.properties_name,property_alias,price");
            ItemSellerGetResponse itemSellerGetResponse = null;
            try {
                itemSellerGetResponse = client.execute(req, sessionKey);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            completeItemList.add(itemSellerGetResponse.getItem());
        }
        System.out.println(JSON.toJSON(completeItemList));
    }

    /**
     * 获取单笔交易的详细信息
     */
    private static void getTradeFullInfo() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
        req.setFields(fields);
        req.setTid(1730232757751787570L);
        TradeFullinfoGetResponse rsp = client.execute(req, sessionKey);
        System.out.println(rsp.getBody());
    }

    private static void getRefund() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        RefundGetRequest request = new RefundGetRequest();
        request.setRefundId(84854052037325798L);
        request.setFields("refund_id, alipay_no, tid, oid, buyer_nick, seller_nick, total_fee, status, created, refund_fee, good_status, has_good_return, payment, reason, desc, num_iid, title, price, num, good_return_time, company_name, sid, address, shipping_type, refund_remind_timeout, refund_phase, refund_version, operation_contraint, attribute, outer_id, sku");
        RefundGetResponse rsp = client.execute(request, sessionKey);
        System.out.println(rsp.getBody());
    }
}
