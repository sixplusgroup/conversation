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
import finley.gmair.service.CrmSyncService;
import finley.gmair.service.DriftOrderSyncService;
import finley.gmair.service.TbOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 * @date 2020/11/01 2:31 下午
 * @description TbOrderServiceImpl
 */
public class TbOrderServiceImpl implements TbOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private CrmSyncService crmSyncService;

    @Autowired
    private DriftOrderSyncService driftOrderSyncService;

    private Logger logger = LoggerFactory.getLogger(TbOrderSyncServiceImpl.class);

    private static final Long DRIFT_NUM_IID = 618391118089L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData handleTrade(Trade trade) {
        //step1：save trade to db or update the existed trades（including the orders）
        ResultData rsp = new ResultData();
        ResultData res = new ResultData();
        // 判断是否是新增订单
        int tradeNum = tradeMapper.countByTid(trade.getTid());
        if (tradeNum == 0) {
            // 交易不存在 -> 创建新交易
            rsp = saveTrade(trade);
        } else if (tradeNum == 1) {
            // 交易存在 -> 更新交易状态
            rsp = updateTradeStatus(trade);
        }
        if (rsp.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("处理交易失败");
            return res;
        }
        //step2：synchronize trade to drift or crm
        // 2.1 同步到CRM
        ResultData rsp1 = new ResultData();
        finley.gmair.model.ordernew.Trade crmTrade =
                tradeMapper.selectByTid(trade.getTid()).get(0);
        if (tradeNum == 0) {
            // 添加交易到CRM系统
            rsp1 = crmSyncService.createNewOrder(crmTrade);
        } else if (tradeNum == 1) {
            // 更新交易状态到CRM系统
            rsp1 = crmSyncService.updateOrderStatus(crmTrade);
        }
        if (rsp1.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("处理交易失败");
            return res;
        }

        //2.2 同步到drift
        ResultData rsp2 = syncTrade(trade);
        if (rsp2.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return rsp2;
        }

        res.setResponseCode(ResponseCode.RESPONSE_OK);
        res.setDescription("处理交易成功");
        return res;
    }

    /**
     * tb单笔Trade新增到中台
     *
     * @param tbTrade 待添加的订单(com.taobao.api.domain.Trade)
     * @return ResultData
     * @author zm
     */
    private ResultData saveTrade (Trade tbTrade){
        ResultData resultData = new ResultData();

        if (tbTrade == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("待处理的交易为空");
            return resultData;
        }

        logger.info("handle trade, trade:{}", JSON.toJSONString(tbTrade));

        // 1. 插入Trade
        TbTradeDTO tradeDTO = new TbTradeDTO(tbTrade);
        int tradeInsertNum = tradeMapper.insertSelectiveWithTradeDTO(tradeDTO);
        if (tradeInsertNum == 0) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("订单插入出错");
        }
        List<Order> tbOrders = tbTrade.getOrders();
        // 2. 插入Order
        if (tbOrders == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("子订单为空");
            return resultData;
        } else {
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
        resultData.setDescription("交易新增成功");
        return resultData;
    }

    /**
     * 更新中台交易的订单状态
     *
     * @param tbTrade 待更新的订单(com.taobao.api.domain.Trade)
     * @return ResultData
     * @author zm
     */
    private ResultData updateTradeStatus (Trade tbTrade){
        ResultData resultData = new ResultData();
        if (tbTrade == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("待处理的交易为空");
            return resultData;
        }
        TbTradeStatus updatedStatus = TbTradeStatus.valueOf(tbTrade.getStatus());
        // 1. 更新Trade的状态
        int tradeUpdateNum = tradeMapper.updateStatusByTid(updatedStatus.name(), tbTrade.getTid());
        if (tradeUpdateNum == 0) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("交易状态更新出错");
            return resultData;
        }
        // 2. 更新Orders的状态
        List<Order> tbOrders = tbTrade.getOrders();
        if (tbOrders == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("子订单为空");
            return resultData;
        } else {
            for (Order tmpOrder : tbOrders) {
                int orderUpdateNum = orderMapper.updateStatusByOid(
                        updatedStatus.name(), tmpOrder.getOid());
                if (orderUpdateNum == 0) {
                    resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    resultData.setDescription("子订单状态更新出错");
                    return resultData;
                }
            }
        }
        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        resultData.setDescription("交易状态更新成功");
        return resultData;
    }

    /**
     * 淘宝订单同步到drift或crm
     *
     * @param trade
     * @return
     */
    private ResultData syncTrade (Trade trade){
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
    private DriftOrderExpress toDriftOrderExpress (Trade trade){
        //构造DriftOrder
        DriftOrder driftOrder = new DriftOrder();
        driftOrder.setOrderId(trade.getTid().toString());
        driftOrder.setConsignee(trade.getReceiverName());
        driftOrder.setPhone(trade.getReceiverMobile());
        driftOrder.setProvince(trade.getReceiverState());
        driftOrder.setCity(trade.getReceiverCity());
        driftOrder.setDistrict(trade.getReceiverDistrict());
        driftOrder.setAddress(trade.getReceiverAddress());
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
            if (!DRIFT_NUM_IID.equals(order.getNumIid())) {
                continue;
            }
            DriftOrderItem item = new DriftOrderItem();
            item.setItemId(order.getOid().toString());
            item.setOrderId(trade.getTid().toString());
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

        return new DriftOrderExpress(driftOrder, driftExpress);
    }

    /**
     * 判断是否同步到drift,trade.num_iid=x or trade.orders[*].num_iid=x
     *
     * @param trade
     * @return
     */
    private boolean isSyncToDriftTrade (Trade trade){
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
