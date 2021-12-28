package finley.gmair.api;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 17:18
 * @description ：
 */

@Service
public class TbApi {
    @Value("${taobao.url}")
    private String url;
    @Value("${taobao.appKey}")
    private String appKey;
    @Value("${taobao.appSecret}")
    private String appSecret;

    DefaultTaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);

    public TradesSoldGetResponse tradesSoldGet(TradesSoldGetRequest request, String sessionKey) {
        TradesSoldGetResponse resp = null;
        try {
            resp = client.execute(request, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public TradesSoldIncrementGetResponse tradesSoldIncrementGet(TradesSoldIncrementGetRequest request, String sessionKey) {
        TradesSoldIncrementGetResponse resp = null;
        try {
            resp = client.execute(request, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public TradeFullinfoGetResponse tradeFullInfoGet(TradeFullinfoGetRequest request, String sessionKey) {
        TradeFullinfoGetResponse resp = null;
        try {
            resp = client.execute(request, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public ItemsOnsaleGetResponse itemsOnSaleGet(ItemsOnsaleGetRequest request, String sessionKey) {
        ItemsOnsaleGetResponse resp = null;
        try {
            resp = client.execute(request, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public ItemSellerGetResponse itemSellerGet(ItemSellerGetRequest request, String sessionKey) {
        ItemSellerGetResponse resp = null;
        try {
            resp = client.execute(request, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return resp;
    }
}
