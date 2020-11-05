package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.StringUtils;
import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.TradeMapper;
import finley.gmair.model.drift.*;
import finley.gmair.model.dto.TbOrderDTO;
import finley.gmair.model.dto.TbOrderPartInfo;
import finley.gmair.model.dto.TbTradeDTO;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.model.ordernew.TradeFrom;
import finley.gmair.model.ordernew.TradeMode;
import finley.gmair.service.CrmSyncService;
import finley.gmair.service.DriftOrderSyncService;
import finley.gmair.service.TbOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zm
 * @date 2020/11/01 2:31 下午
 * @description TbOrderServiceImpl
 */
@Service
public class TbOrderServiceImpl implements TbOrderService {
    private static final Long DRIFT_NUM_IID = 618391118089L;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private CrmSyncService crmSyncService;
    @Autowired
    private DriftOrderSyncService driftOrderSyncService;
    private Logger logger = LoggerFactory.getLogger(TbOrderSyncServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData handleTrade(Trade trade) {
        //step1：save trade to db or update the existed trades（including the orders）
        ResultData rsp = new ResultData();
        SyncResult syncResult = new SyncResult();
        ResultData res = new ResultData();

        int tradeNum = tradeMapper.countByTid(trade.getTid());
        if (tradeNum == 0) {
            rsp = saveTrade(trade);
        } else if (tradeNum == 1) {
            rsp = updateTradeStatus(trade);
        }
        if (rsp.getResponseCode() != ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("trade入库失败");
            res.setData(trade);
            return res;
        }
        //step2：synchronize trade to drift or crm
        if (tradeNum == 1) {
            // 仅同步交易状态变更到CRM系统
            finley.gmair.model.ordernew.Trade crmTrade =
                    tradeMapper.selectByTid(trade.getTid()).get(0);
            // 只有去模糊化的交易mode==1才同步
            if (crmTrade.getMode() == TradeMode.PUSHED_TO_CRM.getValue()) {
                syncResult.setSyncToCRM(true);
                ResultData rsp1 = crmSyncService.updateOrderStatus(crmTrade);
                if (rsp1.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("syncOrderToCrm error, response:{}", rsp1);
                } else {
                    syncResult.setSyncToCRMSuccess(true);
                }
            }
        }

        //2.2 同步到drift
        if (isSyncToDriftTrade(trade)) {
            syncResult.setSyncToDrift(true);
            DriftOrderExpress driftOrder = toDriftOrderExpress(trade);
            ResultData rsp2 = driftOrderSyncService.syncOrderToDrift(driftOrder);
            if (rsp2.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("syncOrderToDrift error, response:{}", rsp2);
            } else {
                syncResult.setSyncToDriftSuccess(true);
            }
        }

        res.setResponseCode(ResponseCode.RESPONSE_OK);
        res.setDescription("处理交易成功");
        res.setData(syncResult);
        return res;
    }

    /**
     * 处理excel模糊字段
     *
     * @param list
     * @return
     */
    @Override
    public ResultData handlePartInfo(List<TbOrderPartInfo> list) {
        for (TbOrderPartInfo partInfo : list) {
            SyncResult syncResult = new SyncResult();
            //step1:update to db
            List<finley.gmair.model.ordernew.Trade> tradeList = tradeMapper.selectByTid(Long.parseLong(partInfo.getOrderId()));
            if (CollectionUtils.isEmpty(tradeList)) {
                logger.error("handlePartInfo error, failed to find by tid:{}", partInfo.getOrderId());
                //数据库中没有则不同步
                continue;
            }
            finley.gmair.model.ordernew.Trade trade = tradeList.get(0);
            trade.setReceiverName(partInfo.getReceiver());
            trade.setReceiverMobile(partInfo.getPhone());
            String[] strs = partInfo.getDeliveryAddress().split("    ");
            String address = strs.length > 1 ? strs[1] : partInfo.getDeliveryAddress();
            trade.setReceiverAddress(address);
            trade.setMode(TradeMode.DEBLUR.getValue());
            tradeMapper.updateByPrimaryKey(trade);


            //step2:sync to crm
            // 只有去模糊化的交易mode==1，并且状态不是TRADE_CLOSED_BY_TAOBAO和WAIT_BUYER_PAY
            if (trade.getMode() == TradeMode.DEBLUR.getValue() &&
                    TbTradeStatus.valueOf(trade.getStatus()).judgeCrmAdd()) {
                syncResult.setSyncToCRM(true);
                ResultData resultData1 = crmSyncService.createNewOrder(trade);
                if (resultData1.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    trade.setMode(TradeMode.PUSHED_TO_CRM.getValue());
                    tradeMapper.updateByPrimaryKey(trade);
                    syncResult.setSyncToCRMSuccess(true);
                } else {
                    logger.error("syncOrderPartToCrm error, response:{}, tid:{}", resultData1, trade.getTid());
                }
            }

            //step3:sync to drift
            List<finley.gmair.model.ordernew.Order> orderList2 = orderMapper.selectAllByTradeId(trade.getTradeId());
            boolean syncToDrift = false;
            if (DRIFT_NUM_IID.equals(trade.getNumIid())) {
                syncToDrift = true;
            }
            for (finley.gmair.model.ordernew.Order tempOrder : orderList2) {
                if (DRIFT_NUM_IID.equals(tempOrder.getNumIid())) {
                    syncToDrift = true;
                    break;
                }
            }
            if (TbTradeStatus.TRADE_CLOSED_BY_TAOBAO.equals(TbTradeStatus.valueOf(trade.getStatus()))
                    || TbTradeStatus.WAIT_BUYER_PAY.equals(TbTradeStatus.valueOf(trade.getStatus()))) {
                syncToDrift = false;
            }
            if (syncToDrift) {
                syncResult.setSyncToDrift(true);
                ResultData resultData2 = driftOrderSyncService.syncOrderPartInfoToDrift(trade.getTid().toString(), trade.getReceiverName(),
                        trade.getReceiverMobile(), trade.getReceiverAddress());
                if (resultData2.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("syncOrderPartToDrift error, response:{}, tid:{}", resultData2, trade.getTid());
                } else {
                    syncResult.setSyncToDriftSuccess(true);
                }
            }
            logger.info("handlePartInfo success, response:{}, tid:{}", syncResult, trade.getTid());
        }
        return new ResultData();
    }

    /**
     * tb单笔Trade新增到中台
     *
     * @param tbTrade 待添加的订单(com.taobao.api.domain.Trade)
     * @return ResultData
     * @author zm
     */
    private ResultData saveTrade(Trade tbTrade) {
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
    private ResultData updateTradeStatus(Trade tbTrade) {
        ResultData resultData = new ResultData();
        if (tbTrade == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("待处理的交易为空");
            return resultData;
        }
        TbTradeStatus updatedStatus =
                TbTradeStatus.valueOf(tbTrade.getStatus());
        // 1. 更新Trade的状态和相关时间信息
        finley.gmair.model.ordernew.Trade interTrade = new finley.gmair.model.ordernew.Trade();
        // 设置tid
        interTrade.setTid(tbTrade.getTid());
        // 更新状态
        interTrade.setStatus(updatedStatus.name());
        // 更新交易修改时间
        interTrade.setModified(tbTrade.getModified());
        // 更新付款时间
        interTrade.setPayTime(tbTrade.getPayTime());
        // 更新交易结束时间
        interTrade.setEndTime(tbTrade.getEndTime());
        // 更新派送公司
        interTrade.setDeliveryCps(tbTrade.getDeliveryCps());
        // 更新截单时间
        interTrade.setCutoffMinutes(tbTrade.getCutoffMinutes());
        // 更新发货时间
        if (tbTrade.getDeliveryTime() != null) {
            interTrade.setDeliveryTime(TimeUtil.formatTimeToDatetime(tbTrade.getDeliveryTime()));
        }
        // 更新揽收时间
        if (tbTrade.getCollectTime() != null) {
            interTrade.setCollectTime(TimeUtil.formatTimeToDatetime(tbTrade.getCollectTime()));
        }
        // 更新派送时间
        if (tbTrade.getDispatchTime() != null) {
            interTrade.setDispatchTime(TimeUtil.formatTimeToDatetime(tbTrade.getDispatchTime()));
        }
        // 更新签收时间
        if (tbTrade.getSignTime() != null) {
            interTrade.setSignTime(TimeUtil.formatTimeToDatetime(tbTrade.getSignTime()));
        }
        int tradeUpdateNum = tradeMapper.updateByTidSelective(interTrade);
        if (tradeUpdateNum == 0) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("交易状态更新出错");
            return resultData;
        }
        // 2. 更新Orders的状态以及相关信息
        List<Order> tbOrders = tbTrade.getOrders();
        if (tbOrders == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("子订单为空");
            return resultData;
        } else {
            for (Order tmpOrder : tbOrders) {
                finley.gmair.model.ordernew.Order interOrder = new finley.gmair.model.ordernew.Order();
                interOrder.setOid(tmpOrder.getOid());
                interOrder.setStatus(updatedStatus.name());
                interOrder.setStoreCode(tmpOrder.getStoreCode());
                interOrder.setShippingType(tmpOrder.getShippingType());
                interOrder.setLogisticsCompany(tmpOrder.getLogisticsCompany());
                interOrder.setInvoiceNo(tmpOrder.getInvoiceNo());
                if (tmpOrder.getConsignTime() != null) {
                    interOrder.setConsignTime(TimeUtil.formatTimeToDatetime(tmpOrder.getConsignTime()));
                }
                int orderUpdateNum = orderMapper.updateByOidSelective(interOrder);
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
        if (TbTradeStatus.TRADE_CLOSED_BY_TAOBAO.equals(TbTradeStatus.valueOf(trade.getStatus()))
                || TbTradeStatus.WAIT_BUYER_PAY.equals(TbTradeStatus.valueOf(trade.getStatus()))) {
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

    @Data
    private static class SyncResult {
        boolean syncToCRM;
        boolean syncToCRMSuccess;
        boolean syncToDrift;
        boolean syncToDriftSuccess;
    }
}
