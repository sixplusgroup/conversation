package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.TradeMapper;
import finley.gmair.model.drift.*;
import finley.gmair.model.dto.TbOrderDTO;
import finley.gmair.model.dto.TbTradeDTO;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.model.ordernew.TradeFrom;
import finley.gmair.service.DriftOrderSyncService;
import finley.gmair.service.TbOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private DriftOrderSyncService driftOrderSyncService;

    private Logger logger = LoggerFactory.getLogger(TbOrderSyncServiceImpl.class);

    private static final Long DRIFT_NUM_IID = 618391118089L;

    @Override
    @Transient
    public ResultData handleTrade(Trade trade) {
        //save trade to db
        ResultData result = saveTrade(trade);
        if (result.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return result;
        }
        //synchronize trade to drift or crm
        result = syncTrade(trade);
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

        logger.info("handle trade, trade:{}", JSON.toJSONString(trade));

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
                logger.info("handle order of trade, tradeId:{}, order:{}", tradeId, JSON.toJSONString(tmpTbOrder));
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
        if (isSyncToDriftTrade(trade)) {
            DriftOrderExpress driftOrder = toDriftOrderExpress(trade);
            result = driftOrderSyncService.syncOrderToDrift(driftOrder);
            logger.info("sync trade to drift, driftOrder:{}, syncResult:{}", JSON.toJSONString(driftOrder), result);
            if (result.getResponseCode() != ResponseCode.RESPONSE_OK) {
                return result;
            }
        }
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
        driftOrder.setOrderId(trade.getTid().toString());
        driftOrder.setConsignee(trade.getReceiverName());
        driftOrder.setPhone(trade.getReceiverMobile());
        driftOrder.setProvince(trade.getReceiverState());
        driftOrder.setCity(trade.getReceiverCity());
        driftOrder.setDistrict(trade.getReceiverDistrict());
        driftOrder.setAddress(trade.getReceiverAddress());
        driftOrder.setStatus(TbTradeStatus.valueOf(trade.getStatus()).toDriftOrderStatus());
        driftOrder.setCreateTime(trade.getCreated());
        driftOrder.setCreateAt(new Timestamp(trade.getCreated().getTime()));
        driftOrder.setExpectedDate(trade.getCreated());
        driftOrder.setTradeFrom(TradeFrom.TMALL);

        double totalPrice = Double.parseDouble(trade.getPostFee());
        double realPrice = 0;
        //构造DriftOrderItem
        List<DriftOrderItem> itemList = new ArrayList<>();
        for (Order order : trade.getOrders()) {
            if (!DRIFT_NUM_IID.equals(order.getNumIid())) {
                continue;
            }
            DriftOrderItem item = new DriftOrderItem();
            item.setItemId(order.getOid().toString());
            item.setOrderId(trade.getTid().toString());
            String property = order.getSkuPropertiesName();
            if (property.contains("试纸")) {
                item.setItemName("甲醛检测试纸");
            } else if (property.contains("检测仪")) {
                item.setItemName("GM甲醛检测仪");
            } else {
                item.setItemName(order.getSkuPropertiesName());
            }
            item.setSingleNum(1);
            item.setQuantity(order.getNum().intValue());
            item.setExQuantity(order.getNum().intValue());
            item.setItemPrice(Double.parseDouble(order.getPrice()));
            item.setTotalPrice(Double.parseDouble(order.getTotalFee()));
            item.setRealPrice(Double.parseDouble(order.getPayment()));
            itemList.add(item);
            totalPrice += item.getTotalPrice();
            realPrice += item.getRealPrice();
        }
        driftOrder.setList(itemList);
        driftOrder.setTotalPrice(totalPrice);
        driftOrder.setRealPay(realPrice);

        //构造DriftExpress
        DriftExpress driftExpress = new DriftExpress();
        for (Order order : trade.getOrders()) {
            if (!DRIFT_NUM_IID.equals(order.getNumIid())) {
                continue;
            }
            driftExpress.setOrderId(trade.getTid().toString());
            driftExpress.setCompany(trade.getOrders().get(0).getLogisticsCompany());
            driftExpress.setExpressNum(trade.getOrders().get(0).getInvoiceNo());
            driftExpress.setStatus(DriftExpressStatus.DELIVERED);
            break;
        }

        //判断是否实际未发货
        boolean noDelivered = (driftExpress.getExpressNum() == null || driftExpress.getCompany() == null)
                && (DriftOrderStatus.FINISHED.equals(driftOrder.getStatus())
                || DriftOrderStatus.DELIVERED.equals(driftOrder.getStatus()));
        if (noDelivered) {
            driftOrder.setStatus(DriftOrderStatus.CONFIRMED);
        }

        return new DriftOrderExpress(driftOrder, driftExpress);
    }

    /**
     * 判断是否同步到drift,trade.num_iid=x or trade.orders[*].num_iid=x
     *
     * @param trade
     * @return
     */
    private boolean isSyncToDriftTrade(Trade trade) {
        if (TbTradeStatus.TRADE_CLOSED_BY_TAOBAO.equals(TbTradeStatus.valueOf(trade.getStatus()))) {
            return false;
        }
        if (DRIFT_NUM_IID.equals(trade.getNumIid())) {
            return true;
        } else if (null == trade.getNumIid()) {
            if (trade.getOrders() == null) {
                return false;
            }
            for (Order order : trade.getOrders()) {
                if (DRIFT_NUM_IID.equals(order.getNumIid())) {
                    return true;
                }
            }
        }
        return false;
    }
}
