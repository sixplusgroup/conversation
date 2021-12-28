package finley.gmair.converter;

import com.google.common.collect.ImmutableMap;
import finley.gmair.model.domain.UnifiedOrder;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.drift.*;
import finley.gmair.model.enums.OrderStatusEnum;
import finley.gmair.model.enums.PlatformEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 20:17
 * @description ：
 */

@Component
public class DriftOrderConverter {
    private static final Map<String, String> ID_NAME_MAP =
            ImmutableMap.of("57146510696", "甲醛检测试纸", "70463602876", "GM甲醛检测仪");

    public List<UnifiedOrder> filterDriftOrder(UnifiedTrade trade) {
        return trade.getOrderList().stream().filter(
                order -> ID_NAME_MAP.containsKey(order.getSkuId()) &&
                        OrderStatusEnum.getEnumByValue(order.getStatus()).isPushDrift())
                .collect(Collectors.toList());
    }

    public DriftOrderExpress unifiedTrade2Drift(UnifiedTrade trade) {
        //过滤无效item
        trade.setOrderList(filterDriftOrder(trade));
        //构造DriftOrder
        DriftOrder driftOrder = new DriftOrder();
        driftOrder.setOrderId(trade.getTid());
        driftOrder.setConsignee(trade.getConsigneePhone());
        driftOrder.setPhone(trade.getConsigneePhone());
        driftOrder.setProvince(trade.getConsigneeProvince());
        driftOrder.setCity(trade.getConsigneeCity());
        driftOrder.setDistrict(trade.getConsigneeDistrict());
        driftOrder.setAddress(trade.getConsigneeAddress());
        //拼接买家备注和卖家备注
        driftOrder.setDescription(getDescription(trade.getBuyerMessage(), trade.getSellerMemo()));
        //转换状态
        driftOrder.setStatus(unifiedTradeStatus2DriftStatus(trade));
        driftOrder.setCreateTime(trade.getPayTime());
        driftOrder.setCreateAt(new Timestamp(trade.getPayTime().getTime()));
        driftOrder.setExpectedDate(trade.getPayTime());
        driftOrder.setTradeFrom(PlatformEnum.getEnumByValue(trade.getTradePlatform()).toTradeFrom());
        //totalPrice和realPay都要带上邮费(不能直接取trade因为子订单可能过滤非甲醛检测商品)
        driftOrder.setTotalPrice(trade.getPostFee() +
                trade.getOrderList().stream().mapToDouble(order -> order.getPrice() * order.getNum()).sum());
        driftOrder.setRealPay(trade.getPostFee() +
                trade.getOrderList().stream().mapToDouble(UnifiedOrder::getPayment).sum());

        //构造DriftOrderItem
        List<DriftOrderItem> itemList = new ArrayList<>();
        for (UnifiedOrder order : trade.getOrderList()) {
            DriftOrderItem item = new DriftOrderItem();
            item.setItemId(order.getOid());
            item.setOrderId(trade.getTid());
            item.setItemName(order.getSkuId());
            item.setSingleNum(1);
            item.setQuantity(order.getNum());
            item.setExQuantity(order.getNum());
            item.setItemPrice(order.getPrice());
            item.setTotalPrice(order.getPrice() * order.getNum());
            item.setRealPrice(order.getPayment());
            itemList.add(item);
        }
        driftOrder.setList(itemList);

        //构造DriftExpress
        UnifiedOrder order = trade.getOrderList().get(0);
        DriftExpress driftExpress = new DriftExpress();
        driftExpress.setOrderId(trade.getTid());
        driftExpress.setCompany(order.getLogisticsCompany());
        driftExpress.setExpressNum(order.getLogisticsId());
        driftExpress.setStatus(DriftExpressStatus.DELIVERED);

        return new DriftOrderExpress(driftOrder, driftExpress);
    }

    private DriftOrderStatus unifiedTradeStatus2DriftStatus(UnifiedTrade trade) {
        OrderStatusEnum uStatus = OrderStatusEnum.getEnumByValue(trade.getStatus());
        //已过滤甲醛检测子订单,默认物流信息相同,取一个看物流信息
        UnifiedOrder order = trade.getOrderList().get(0);
        boolean hasExpressInfo = StringUtils.isNotBlank(order.getLogisticsId());
        switch (uStatus) {
            case WAIT_BUYER_PAY:
                return DriftOrderStatus.APPLIED;
            case WAIT_SELLER_SEND_GOODS:
                return DriftOrderStatus.PAYED;
            case WAIT_BUYER_CONFIRM_GOODS:
                if (!hasExpressInfo) {
                    return DriftOrderStatus.CONFIRMED;
                } else {
                    return DriftOrderStatus.DELIVERED;
                }
            case TRADE_FINISHED:
                if (!hasExpressInfo) {
                    return DriftOrderStatus.CONFIRMED;
                } else {
                    return DriftOrderStatus.FINISHED;
                }
            case TRADE_CLOSED:
                return DriftOrderStatus.CANCELED;
            default:
                return null;
        }
    }

    private String getDescription(String buyerMessage, String sellerMessage) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(buyerMessage)) {
            sb.append("买家备注:").append(buyerMessage).append("    ");
        }
        if (StringUtils.isNotBlank(sellerMessage)) {
            sb.append("卖家备注:").append(sellerMessage);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
}
