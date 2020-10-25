package finley.gmair.service.impl;

import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.TradeMapper;
import finley.gmair.model.dto.TbOrderDTO;
import finley.gmair.model.dto.TbTradeDTO;
import finley.gmair.service.TbOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:46
 * @description ：
 */
@Service
public class TbOrderServiceImpl implements TbOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TradeMapper tradeMapper;

    @Override
    @Transient
    public ResultData handleTrade(Trade trade) {
        ResultData resultData = new ResultData();

        if(trade == null){
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("待处理的交易为空");
        }

        List<Order> tbOrders = trade.getOrders();

        System.out.println("handleTrade");
        System.out.println(trade.getOrders());

        // 1. 插入Trade
        TbTradeDTO tradeDTO = new TbTradeDTO(trade);
        int tradeInsertNum = tradeMapper.insertSelectiveWithTradeDTO(tradeDTO);
        if(tradeInsertNum == 0){
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("订单插入出错");
        }

        // 2. 插入Order
        if (tbOrders != null){
            String tradeId = tradeDTO.getTradeId();
            for (Order tmpTbOrder:tbOrders) {
                TbOrderDTO orderDTO = new TbOrderDTO(tmpTbOrder);
                orderDTO.setTradeId(tradeId);
                int orderInsertNum = orderMapper.insertSelectiveWithTbOrder(orderDTO);
                if (orderInsertNum == 0){
                    resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    resultData.setDescription("订单插入出错");
                }
            }
        }

        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        resultData.setDescription("订单插入成功");
        return resultData;
    }
}
