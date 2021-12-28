package finley.gmair.converter;

import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.response.get.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.response.get.OrderSearchInfo;
import finley.gmair.model.domain.UnifiedOrder;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.enums.OrderStatusEnum;
import finley.gmair.model.enums.PlatformEnum;
import finley.gmair.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-22 22:02
 * @description ：
 */

@Component
public class JdTradeConverter {
    private Logger logger = LoggerFactory.getLogger(JdTradeConverter.class);

    private static Map<String, String> LOGISTIC_MAP = getLogisticInfo();

    public UnifiedTrade jdTrade2UnifiedTrade(OrderSearchInfo originalTrade) {
        UnifiedTrade unifiedTrade = new UnifiedTrade();
        unifiedTrade.setTid(originalTrade.getOrderId());
        unifiedTrade.setTradePlatform(PlatformEnum.JD.getValue());
        unifiedTrade.setIsFuzzy(true);

        int status = jdStatus2UnifiedStatus(originalTrade.getOrderState()).getValue();
        unifiedTrade.setStatus(status);

        if (StringUtils.isNotBlank(originalTrade.getOrderStartTime())) {
            unifiedTrade.setCreateTime(DateUtil.str2Date(originalTrade.getOrderStartTime()));
        }
        if (StringUtils.isNotBlank(originalTrade.getModified())) {
            unifiedTrade.setUpdateTime(DateUtil.str2Date(originalTrade.getModified()));
        }
        if (StringUtils.isNotBlank(originalTrade.getOrderEndTime())) {
            unifiedTrade.setEndTime(DateUtil.str2Date(originalTrade.getOrderEndTime()));
        }
        if (StringUtils.isNotBlank(originalTrade.getPaymentConfirmTime())) {
            unifiedTrade.setPayTime(DateUtil.str2Date(originalTrade.getPaymentConfirmTime()));
        }
        if (originalTrade.getConsigneeInfo() != null) {
            if (StringUtils.isNotBlank(originalTrade.getConsigneeInfo().getProvince())) {
                unifiedTrade.setConsigneeProvince(originalTrade.getConsigneeInfo().getProvince());
            }
            if (StringUtils.isNotBlank(originalTrade.getConsigneeInfo().getCity())) {
                unifiedTrade.setConsigneeCity(originalTrade.getConsigneeInfo().getCity());
            }
            if (StringUtils.isNotBlank(originalTrade.getConsigneeInfo().getCounty())) {
                unifiedTrade.setConsigneeDistrict(originalTrade.getConsigneeInfo().getCounty());
            }
            if (StringUtils.isNotBlank(originalTrade.getConsigneeInfo().getFullAddress())) {
                unifiedTrade.setConsigneeAddress(originalTrade.getConsigneeInfo().getFullAddress());
            }
            if (StringUtils.isNotBlank(originalTrade.getConsigneeInfo().getFullname())) {
                unifiedTrade.setConsigneeName(originalTrade.getConsigneeInfo().getFullname());
            }
            if (StringUtils.isNotBlank(originalTrade.getConsigneeInfo().getTelephone())) {
                unifiedTrade.setConsigneePhone(originalTrade.getConsigneeInfo().getTelephone());
            }
        }
        if (StringUtils.isNotBlank(originalTrade.getOrderPayment())) {
            unifiedTrade.setPayment(Double.valueOf(originalTrade.getOrderPayment()));
        }
        if (StringUtils.isNotBlank(originalTrade.getFreightPrice())) {
            unifiedTrade.setPostFee(Double.parseDouble(originalTrade.getFreightPrice()));
        }
        if (StringUtils.isNotBlank(originalTrade.getOrderTotalPrice())) {
            unifiedTrade.setPrice(Double.parseDouble(originalTrade.getOrderTotalPrice()));
        }
        if (StringUtils.isNotBlank(originalTrade.getVenderRemark())) {
            unifiedTrade.setSellerMemo(originalTrade.getVenderRemark());
        }
        if (StringUtils.isNotBlank(originalTrade.getOrderRemark())) {
            unifiedTrade.setBuyerMessage(originalTrade.getOrderRemark());
        }
        List<UnifiedOrder> orderList = new ArrayList<>();
        for (ItemInfo itemInfo : originalTrade.getItemInfoList()) {
            UnifiedOrder unifiedOrder = new UnifiedOrder();
            unifiedOrder.setTid(originalTrade.getOrderId());
            unifiedOrder.setStatus(status);
            if (StringUtils.isNotEmpty(originalTrade.getWaybill())) {
                unifiedOrder.setLogisticsId(originalTrade.getWaybill());
            }
            if (StringUtils.isNotEmpty(originalTrade.getLogisticsId())) {
                unifiedOrder.setLogisticsCompany(getLogisticCompany(originalTrade.getLogisticsId()));
            }
            // 运单号-10000的物流公司为厂家直送/用户自提,写入卖家备注并将运单号和物流公司置空
            if ("-10000".equals(originalTrade.getWaybill())) {
                String sellerMemo = StringUtils.isNotBlank(unifiedTrade.getSellerMemo()) ? unifiedTrade.getSellerMemo() + " " : ""
                        + unifiedOrder.getLogisticsCompany();
                unifiedTrade.setSellerMemo(sellerMemo);
                unifiedOrder.setLogisticsId(null);
                unifiedOrder.setLogisticsCompany(null);
            }
            if (StringUtils.isNotEmpty(itemInfo.getItemTotal())) {
                unifiedOrder.setNum(Integer.valueOf(itemInfo.getItemTotal()));
            }
            if (StringUtils.isNotEmpty(itemInfo.getWareId())) {
                unifiedOrder.setNumId(itemInfo.getWareId());
            }
            if (StringUtils.isNotEmpty(itemInfo.getSkuId())) {
                unifiedOrder.setSkuId(itemInfo.getSkuId());
            }
            if (StringUtils.isNotEmpty(itemInfo.getJdPrice())) {
                unifiedOrder.setPrice(Double.valueOf(itemInfo.getJdPrice()));
            }
            // 单子订单oid=tid(大多数情况),多子订单oid=sku_id
            unifiedOrder.setOid(originalTrade.getItemInfoList().size() == 1
                    ? originalTrade.getOrderId() : itemInfo.getSkuId());
            // 子订单优惠分摊
            unifiedOrder.setPayment(getDividePayment(originalTrade, itemInfo));
            orderList.add(unifiedOrder);
        }
        unifiedTrade.setOrderList(orderList);
        return unifiedTrade;
    }

    OrderStatusEnum jdStatus2UnifiedStatus(String state) {
        if (StringUtils.isBlank(state)) {
            return null;
        }
        switch (state) {
            case "WAIT_SELLER_DELIVERY":
                return OrderStatusEnum.WAIT_SELLER_SEND_GOODS;
            case "WAIT_SELLER_STOCK_OUT":
                return OrderStatusEnum.WAIT_SELLER_SEND_GOODS;
            case "WAIT_GOODS_RECEIVE_CONFIRM":
                return OrderStatusEnum.WAIT_BUYER_CONFIRM_GOODS;
            case "FINISHED_L":
                return OrderStatusEnum.TRADE_FINISHED;
            case "TRADE_CANCELED":
                return OrderStatusEnum.TRADE_CLOSED;
            case "LOCKED":
                return OrderStatusEnum.TRADE_LOCKED;
            default: {
                logger.error("failed to parse jdStatus to UnifiedStatus,jdStatus:{}", state);
                return OrderStatusEnum.UNKNOWN;
            }
        }
    }

    private String getLogisticCompany(String id) {
        return LOGISTIC_MAP.get(id);
    }

    private Double getDividePayment(OrderSearchInfo order, ItemInfo item) {
        if (order == null || item == null
                || StringUtils.isBlank(item.getJdPrice())
                || StringUtils.isBlank(item.getItemTotal())
                || StringUtils.isBlank(order.getOrderSellerPrice())) {
            logger.error("failed to parse jdStatus to getDividePayment,trade:{},item:{}", order, item);
            return null;
        }
        BigDecimal orderTotalPrice = new BigDecimal(item.getJdPrice()).multiply(new BigDecimal(item.getItemTotal()));
        BigDecimal tradeTotalPrice = new BigDecimal(order.getOrderTotalPrice());
        if (tradeTotalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        //分摊比例=子订单总价/主订单总价
        BigDecimal ratio = orderTotalPrice.divide(tradeTotalPrice, ROUND_HALF_UP);
        //子订单分摊=主订单实付（除邮费）* 分摊
        BigDecimal tradePayment = new BigDecimal(order.getOrderPayment());
        BigDecimal postFee = new BigDecimal(order.getFreightPrice());
        BigDecimal paymentWithoutFreightPrice = tradePayment.subtract(postFee);

        BigDecimal dividePayment = paymentWithoutFreightPrice.multiply(ratio);
        return dividePayment.setScale(2, ROUND_HALF_UP).doubleValue();
    }

    private static Map<String, String> getLogisticInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("2087", "京东快递");
        map.put("336878", "京东大件物流");
        map.put("1274", "厂家自送");
        map.put("467", "顺丰快递");
        map.put("599866", "跨越速运");
        map.put("1499", "中通速递");
        map.put("332098", "用户自提");
        map.put("1327", "韵达快递");
        return map;
    }
}
