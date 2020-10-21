package finley.gmair.service.impl;

import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.ItemSellerGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.ItemSellerGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;
import finley.gmair.OrderNewApplication;
import finley.gmair.dao.OrderMapper;
import finley.gmair.service.TbItemSyncService;
import finley.gmair.util.IDGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderNewApplication.class)
public class TbAPIServiceImplTest {
    @Autowired
    TbAPIServiceImpl tbAPIServiceImpl;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    TbItemSyncService tbItemSyncServiceImpl;
    @Autowired
    TbOrderSyncServiceImpl tbOrderSyncServiceImpl;


    private static final String SESSION_KEY = "6100e02ceb111ceb4e6ff02506458185b2f7afa5fce9f232200642250842";

    @Test
    public void tradesSoldGet() {
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setFields("tid,num_iid,orders,post_fee");
        req.setStartCreated(StringUtils.parseDateTime("2020-10-20 00:00:00"));
        req.setEndCreated(new Date());
        req.setPageNo(1L);
        req.setPageSize(40L);
        req.setUseHasNext(false);
        TradesSoldGetResponse response = tbAPIServiceImpl.tradesSoldGet(req, SESSION_KEY);
        System.out.println(response.getBody());
    }

    @Test
    public void getIncrementTrades() {
        TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
        req.setFields("tid,num_iid,orders");
        req.setStartModified(StringUtils.parseDateTime("2020-10-01 12:00:00"));
        req.setEndModified(StringUtils.parseDateTime("2020-10-01 23:59:59"));
        req.setPageNo(1L);
        req.setPageSize(40L);
        req.setUseHasNext(true);
        TradesSoldIncrementGetResponse response = tbAPIServiceImpl.tradesSoldIncrementGet(req, SESSION_KEY);
        System.out.println(response.getBody());
    }

    @Test
    public void itemsOnSaleGet() {
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
        req.setFields("num_iid,cid,props,title,price,num,list_time,delist_time");
        ItemsOnsaleGetResponse rsp = tbAPIServiceImpl.itemsOnSaleGet(req, SESSION_KEY);
        System.out.println(rsp.getBody());
    }

    @Test
    public void itemSellerGetResponse() {
        ItemSellerGetRequest req = new ItemSellerGetRequest();
        req.setNumIid(618391118089L);
        req.setFields("num_iid,title,sku.sku_id,sku.price,sku.properties_name,price");
        ItemSellerGetResponse rsp = tbAPIServiceImpl.itemSellerGet(req, SESSION_KEY);
        System.out.println(rsp.getBody());
    }

    @Test
    public void mapperTest() {
        System.out.println(orderMapper == null);
        System.out.println(orderMapper.selectByPrimaryKey(""));
        System.out.println(orderMapper.selectAll().size());
    }

    @Test
    public void itemImport() {
        tbItemSyncServiceImpl.fullImport();
    }

    @Test
    public void orderImport() {
        tbOrderSyncServiceImpl.fullImport();
    }

    @Test
    public void generateId() {
        System.out.println(IDGenerator.generate("SEL"));
    }

}