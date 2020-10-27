package finley.gmair.service.impl;

import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.TradeMapper;
import finley.gmair.model.drift.*;
import finley.gmair.model.dto.TbOrderDTO;
import finley.gmair.model.dto.TbTradeDTO;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.service.TbOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        //save trade to db
        ResultData result = saveTrade(trade);
        if (result.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return result;
        }
        //synchronize trade to drift or crm
        result = handleTrade(trade);
        if (result.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return result;
        }
        return new ResultData();
    }

    /**
     * 淘宝订单入库
     *
     * @param trade
     * @return
     */
    private ResultData saveTrade(Trade trade) {
        ResultData resultData = new ResultData();

        if (trade == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("待处理的交易为空");
        }

        List<Order> tbOrders = trade.getOrders();

        System.out.println("handleTrade");
        System.out.println(trade.getOrders());

        // 1. 插入Trade
        TbTradeDTO tradeDTO = new TbTradeDTO(trade);
        int tradeInsertNum = tradeMapper.insertSelectiveWithTradeDTO(tradeDTO);
        if (tradeInsertNum == 0) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("订单插入出错");
        }

        // 2. 插入Order
        if (tbOrders != null) {
            String tradeId = tradeDTO.getTradeId();
            for (Order tmpTbOrder : tbOrders) {
                TbOrderDTO orderDTO = new TbOrderDTO(tmpTbOrder);
                orderDTO.setTradeId(tradeId);
                int orderInsertNum = orderMapper.insertSelectiveWithTbOrder(orderDTO);
                if (orderInsertNum == 0) {
                    resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    resultData.setDescription("订单插入出错");
                }
            }
        }

        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        resultData.setDescription("订单插入成功");
        return resultData;
    }

    /**
     * 淘宝订单同步到drift或crm
     *
     * @param trade
     * @return
     */
    private ResultData syncTrade(Trade trade) {
        ResultData result = new ResultData();

        return result;
    }

    /**
     * trade转DriftOrderExpress
     *
     * @param trade
     * @return
     */
    private DriftOrderExpress toDriftOrderExpress(Trade trade) {
        //构造DriftOrder
        DriftOrder driftOrder = new DriftOrder();
        driftOrder.setOrderId(trade.getTidStr());
        driftOrder.setConsignee(trade.getReceiverName());
        driftOrder.setPhone(trade.getReceiverMobile());
        driftOrder.setProvince(trade.getReceiverState());
        driftOrder.setCity(trade.getReceiverCity());
        driftOrder.setDistrict(trade.getReceiverDistrict());
        driftOrder.setTotalPrice(Double.parseDouble(trade.getTotalFee()));
        driftOrder.setRealPay(Double.parseDouble(trade.getPayment()));
        driftOrder.setStatus(TbTradeStatus.valueOf(trade.getStatus()).toDriftOrderStatus());
        driftOrder.setCreateTime(trade.getCreated());
        driftOrder.setCreateAt(new Timestamp(trade.getCreated().getTime()));
        driftOrder.setIntervalDate(2);
        //todo:设置tradeFrom

        //构造DriftOrderItem
        List<DriftOrderItem> itemList = new ArrayList<>();
        for (Order order : trade.getOrders()) {
            DriftOrderItem item = new DriftOrderItem();
            item.setOrderId(trade.getTidStr());
            item.setItemName(order.getSkuPropertiesName());
            item.setSingleNum(1);
            item.setQuantity(order.getNum().intValue());
            item.setExQuantity(order.getNum().intValue());
            item.setItemPrice(Double.parseDouble(order.getPrice()));
            item.setTotalPrice(Double.parseDouble(order.getTotalFee()));
            item.setRealPrice(Double.parseDouble(order.getPayment()));
            itemList.add(item);
        }
        driftOrder.setList(itemList);

        //构造DriftExpress
        DriftExpress driftExpress = new DriftExpress();
        driftExpress.setOrderId(trade.getTidStr());
        driftExpress.setCompany(trade.getOrders().get(0).getLogisticsCompany());
        driftExpress.setExpressNum(trade.getOrders().get(0).getInvoiceNo());
        driftExpress.setStatus(DriftExpressStatus.DELIVERED);

        return new DriftOrderExpress(driftOrder, driftExpress);
    }
}
