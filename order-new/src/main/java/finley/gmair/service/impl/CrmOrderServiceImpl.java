package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.OrderMapper;
import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.ordernew.*;
import finley.gmair.service.CrmAPIService;
import finley.gmair.service.CrmOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zm
 * @date 2020/10/26 0026 17:49
 **/
public class CrmOrderServiceImpl implements CrmOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CrmAPIService crmAPIService;

    /**
     * 将中台系统中的trade（一个父订单）转存到crm系统中
     *
     * @param trade
     * @return finley.gmair.util.ResultData
     * @author zm
     * @date 2020/10/26 0026 10:42
     **/
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResultData createTrade(Trade trade) {
        ResultData result = new ResultData();
        // 订单日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tradeDate = sdf.format(trade.getCreated());
        // 收件人姓名
        String receiverName = trade.getReceiverName();
        // 收件人手机号
        String phoneNum = trade.getReceiverMobile();
        // 详细地址
        String dz = trade.getReceiverState() + " " + trade.getReceiverCity() + " " + trade.getReceiverDistrict() + ""
                + trade.getReceiverTown() + "" + trade.getReceiverAddress();
        // 地区
        String dq = trade.getReceiverCity();

        List<Order> orderList = orderMapper.selectAllByTradeId(trade.getTradeId());

        for (Order tmpOrder :
                orderList) {
            CrmOrderDTO newOrder = new CrmOrderDTO();
            // 设置渠道来源
            newOrder.setQdly(TradeSource.TMALL.getValue());
            newOrder.setJqxh("GM420");
            newOrder.setDdh(String.valueOf(tmpOrder.getOid()));
            newOrder.setSl(String.valueOf(tmpOrder.getNum()));
            newOrder.setXdrq(tradeDate);
            // 实收金额 == 用户的实付金额？
            newOrder.setSsje(String.valueOf(tmpOrder.getTotalFee()));
            newOrder.setYhxm(receiverName);
            newOrder.setLxfs(phoneNum);
            newOrder.setDq(dq);
            newOrder.setDz(dz);

            String billState = String.valueOf(
                    TbTradeStatus.valueOf(tmpOrder.getStatus()).toCrmOrderStatus());
            newOrder.setBillstat(billState);
            String strOrder = JSONObject.toJSON(newOrder).toString();
            JSONObject response = crmAPIService.createNewOrder(strOrder);
            if (response.get("ResponseCode").equals(ResponseCode.RESPONSE_ERROR.toString())) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(response.get("Description").toString());
                return result;
            }
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("向crm系统中新增交易成功");
        return result;
    }
}
