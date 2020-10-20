package finley.gmair.service.impl;

import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;
import finley.gmair.dao.TbUserMapper;
import finley.gmair.model.ordernew.TbUser;
import finley.gmair.service.TbOrderService;
import finley.gmair.service.TbOrderSyncService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:38
 * @description ：
 */

@Service
public class TbOrderSyncServiceImpl implements TbOrderSyncService {
    private static final String IMPORT_FIELDS = "orders,tid,num_iid,num,status,type,shipping_type,trade_from,step_trade_status," +
            "buyer_rate,created,modified,pay_time,consign_time,end_time,receiver_name,receiver_state,receiver_address,receiver_zip," +
            "receiver_mobile,receiver_phone,receiver_country,receiver_city,receiver_district,receiver_town,tmall_delivery,cn_service," +
            "delivery_cps,cutoff_minutes,delivery_time,collect_time,dispatch_time,sign_time,es_time,price,total_fee,payment,adjust_fee," +
            "received_payment,discount_fee,post_fee,credit_card_fee,step_paid_fee";

    private static final Long PAGES_SIZE = 50L;

    @Autowired
    private TbOrderService tbOrderServiceImpl;
    @Autowired
    private TbAPIServiceImpl tbAPIServiceImpl;
    @Autowired
    private TbUserMapper tbUserMapper;


    @Override
    public ResultData fullImport() {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        for (TbUser tbUser : tbUserList) {
            //如果全量同步已执行，则返回
            if (tbUser.getStartSyncTime() != null) {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                resultData.setDescription("fullImport has been executed");
                return resultData;
            }
            String sessionKey = tbUser.getSessionKey();
            Date startSyncTime = new Date();
            Date now = new Date();
            TradesSoldGetRequest request = new TradesSoldGetRequest();
            request.setStartCreated(startSyncTime);
            request.setEndCreated(now);
            request.setFields(IMPORT_FIELDS);
            request.setPageSize(PAGES_SIZE);
            long pageNo = 1L;

            //分页同步
            while (true) {
                request.setPageNo(pageNo++);
                TradesSoldGetResponse response = tbAPIServiceImpl.tradesSoldGet(request, sessionKey);
                for (Trade trade : response.getTrades()) {
                    tbOrderServiceImpl.handleTrade(trade);
                }
                if (!response.getHasNext()) {
                    break;
                }
            }
            //更新卖家用户信息
            tbUser.setStartSyncTime(startSyncTime);
            tbUser.setLastUpdateTime(now);
            tbUserMapper.updateByPrimaryKey(tbUser);
        }
        return resultData;
    }

    @Override
    public ResultData incrementalImport() {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        for (TbUser tbUser : tbUserList) {
            //如果全量同步还未执行,则返回
            if (tbUser.getStartSyncTime() == null) {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                resultData.setDescription("fullImport is not executed");
                return resultData;
            }
            String sessionKey = tbUser.getSessionKey();
            Date lastUpdateTime = tbUser.getLastUpdateTime();
            Date now = new Date();
            TradesSoldIncrementGetRequest request = new TradesSoldIncrementGetRequest();
            request.setStartModified(lastUpdateTime);
            request.setEndModified(now);
            request.setFields(IMPORT_FIELDS);
            request.setPageSize(PAGES_SIZE);
            long pageNo = 1L;

            //分页同步
            while (true) {
                request.setPageNo(pageNo++);
                TradesSoldIncrementGetResponse response = tbAPIServiceImpl.tradesSoldIncrementGet(request, sessionKey);
                for (Trade trade : response.getTrades()) {
                    tbOrderServiceImpl.handleTrade(trade);
                }
                if (!response.getHasNext()) {
                    break;
                }
            }
            //更新卖家用户信息
            tbUser.setLastUpdateTime(now);
            tbUserMapper.updateByPrimaryKey(tbUser);
        }
        return resultData;
    }
}
