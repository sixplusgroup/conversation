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
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:38
 * @description ：
 */

@Service
public class TbOrderSyncServiceImpl implements TbOrderSyncService {
    private static final String IMPORT_FIELDS = "tid,num_iid,orders";

    @Autowired
    private TbOrderService tbOrderServiceImpl;
    @Autowired
    private TbAPIServiceImpl tbAPIServiceImpl;


    @Override
    public ResultData fullImport() {
        /*
        ResultData resultData = tbUserServiceImpl.getTbUser();
        //todo:判断响应结果
        TbUser tbUser = (TbUser) resultData.getData();
        //如果全量同步已执行，则返回
        if (tbUser.getStartSyncTime() != null) {
            return null;
        }
        Date startSyncTime = new Date();
        String sessionKey = tbUser.getSessionKey();
        Date now = new Date();
        //todo:分页
        TradesSoldGetRequest request = new TradesSoldGetRequest();
        request.setStartCreated(startSyncTime);
        request.setEndCreated(now);
        request.setFields(IMPORT_FIELDS);
        request.setPageSize(30L);
        request.setPageNo(1L);
        TradesSoldGetResponse response = tbAPIServiceImpl.tradesSoldGet(request, sessionKey);
        for (Trade trade : response.getTrades()) {
            tbOrderServiceImpl.handleTrade(trade);
        }
        //更新卖家用户信息
        tbUser.setStartSyncTime(startSyncTime);
        tbUser.setLastUpdateTime(now);
        tbUserServiceImpl.updateTbUser(tbUser);
         */
        return null;
    }

    @Override
    public ResultData incrementalImport() {
        Date lastUpdateTime = new Date();//获取user最后修改时间
        String sessionKey = "6100e02ceb111ceb4e6ff02506458185b2f7afa5fce9f232200642250842";//获取user的sessionKey
        Date now = new Date();
        //todo:分页
        TradesSoldIncrementGetRequest request = new TradesSoldIncrementGetRequest();
        request.setStartModified(lastUpdateTime);
        request.setEndModified(now);
        request.setFields(IMPORT_FIELDS);
        request.setPageSize(30L);
        request.setPageNo(1L);
        TradesSoldIncrementGetResponse response = tbAPIServiceImpl.tradesSoldIncrementGet(request, sessionKey);
        for (Trade trade : response.getTrades()) {
            tbOrderServiceImpl.handleTrade(trade);
        }
        //todo:保存用户最后修改时间
        return null;
    }
}
