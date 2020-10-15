package finley.gmair;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/12 14:50
 * @description ：
 */

public class TaobaoSDKTest {
    private static final String url = "http://gw.api.taobao.com/router/rest";
    private static final String appkey = "31404926";
    private static final String secret = "56539bd811035339bf55fe93383ef615";
    private static final String sessionKey = "6100e02ceb111ceb4e6ff02506458185b2f7afa5fce9f232200642250842";

    public static void main(String[] args) throws ApiException {
        getTrades();
    }

    private static final void getTrades() {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        //req.setFields("tid,type,status,payment,orders,rx_audit_status,created,modified");
        req.setFields("num_iid,delivery_time,collect_time,dispatch_time,sign_time,delivery_cps,orders");
        req.setStartCreated(StringUtils.parseDateTime("2020-09-01 00:00:00"));
        req.setEndCreated(StringUtils.parseDateTime("2020-10-11 23:59:59"));
        req.setPageNo(1L);
        req.setPageSize(400L);
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

    private static final void getIncrementTrades() {
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
}
