package finley.gmair.converter;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import finley.gmair.model.domain.UnifiedOrder;
import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.enums.OrderStatusEnum;
import finley.gmair.model.ordernew.CrmOrderStatus;
import finley.gmair.repo.UnifiedSkuItemRepo;
import finley.gmair.util.DateUtil;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 23:58
 * @description ：
 */

@Component
public class CrmOrderConverter {

    @Resource
    UnifiedSkuItemRepo unifiedSkuItemRepo;


    public List<UnifiedOrder> filterCrmOrder(UnifiedTrade trade) {
        return trade.getOrderList().stream().filter(
                order -> OrderStatusEnum.getEnumByValue(order.getStatus()).isPushCrm())
                .collect(Collectors.toList());
    }

    public List<CrmOrder> unifiedTrade2Crm(UnifiedTrade trade) {
        trade.setOrderList(filterCrmOrder(trade));
        List<CrmOrder> crmOrderList = Lists.newArrayList();
        // 判断是否是多子订单交易
        boolean isMultiOrders = trade.getOrderList().size() > 1;
        for (UnifiedOrder order : trade.getOrderList()) {
            UnifiedSkuItem skuItem = unifiedSkuItemRepo.findByShopAndSku(trade.getShopId(), order.getSkuId(), order.getNumId());
            Preconditions.checkArgument(skuItem != null,
                    "failed to find skuItem,shopId:{%s},skuId:{%s},numId:{%s}",
                    trade.getShopId(), order.getSkuId(), order.getNumId());

            CrmOrder crmOrder = new CrmOrder();
            // 渠道来源
            crmOrder.setQdly(skuItem.getChannel());
            // 机器型号
            crmOrder.setJqxh(skuItem.getMachineModel());
            String ddh = isMultiOrders ? trade.getTid() + "-" + order.getOid() : order.getOid();
            crmOrder.setDdh(ddh);
            // 数量
            crmOrder.setSl(String.valueOf(order.getNum()));
            // 实收金额
            Double ssje;
            if (!isMultiOrders) {
                ssje = trade.getPayment();
            } else {
                String property = skuItem.getPropertiesName() != null ? skuItem.getPropertiesName() : "";
                // 多子订单且有邮费，甲醛检测仪子订单包含邮费
                if (property.contains("检测仪") && property.contains("租赁") && trade.getPostFee() != null) {
                    ssje = order.getPayment() + trade.getPostFee();
                } else {
                    ssje = order.getPayment();
                }
            }
            crmOrder.setSsje(String.valueOf(ssje));
            // 付款日期
            crmOrder.setXdrq(DateUtil.date2Str(trade.getPayTime()));
            // 用户姓名
            crmOrder.setYhxm(trade.getConsigneeName());
            // 联系方式
            crmOrder.setLxfs(trade.getConsigneePhone());
            crmOrder.setDq(trade.getConsigneeCity());
            crmOrder.setDz(trade.getConsigneeAddress());
            String buyerMes = null == trade.getBuyerMessage() ? "" : trade.getBuyerMessage();
            crmOrder.setBuyermes(buyerMes);
            String sellerMemo = null == trade.getSellerMemo() ? "" : trade.getSellerMemo();
            crmOrder.setSellermes(sellerMemo);


            Preconditions.checkArgument(skuItem.getIsFictitious() != null);
            CrmOrderStatus billStatus = unifiedOrder2CrmStatus(order, skuItem.getIsFictitious());
            // 校验crm状态是否转换成功
            Preconditions.checkArgument(billStatus != null,
                    "unifiedOrder2CrmStatus,order:%s,isFictitious:%s",
                    JSON.toJSON(order), skuItem.getIsFictitious());
            crmOrder.setBillstat(String.valueOf(billStatus.getValue()));

            crmOrderList.add(crmOrder);
        }
        return crmOrderList;
    }

    private CrmOrderStatus unifiedOrder2CrmStatus(UnifiedOrder unifiedOrder, Boolean isFictitious) {
        // 校验crm状态是否转换成功
        OrderStatusEnum orderStatus = OrderStatusEnum.getEnumByValue(unifiedOrder.getStatus());
        boolean hasExpressInfo = StringUtils.isBlank(unifiedOrder.getLogisticsId());
        switch (orderStatus) {
            case WAIT_SELLER_SEND_GOODS:
                return CrmOrderStatus.UNTREATED;
            case WAIT_BUYER_CONFIRM_GOODS:
                if (!isFictitious && hasExpressInfo) {
                    // 实物订单的订单号为空的 -> 已勘测，客户要求延迟发货
                    return CrmOrderStatus.SURVEYED_REQUEST_DELIVERY_DELAY;
                } else {
                    // 部分发货、等待买家确认收货 -> 已发货运输中
                    return CrmOrderStatus.DELIVERED_IN_TRANSIT;
                }
            case TRADE_CLOSED:
                return CrmOrderStatus.GOODS_RETURNED;
            case TRADE_FINISHED:
                return CrmOrderStatus.ALL_INSTALLATION_COMPLETED;
            default:
                return null;
        }
    }

    @Data
    public static class CrmOrder {
        /**
         * 渠道来源
         */
        private String qdly;
        /**
         * 机器型号
         */
        private String jqxh;
        /**
         * 用户姓名
         */
        private String yhxm;
        /**
         * 订单号（订单号+联系方式，确认唯一性）
         */
        private String ddh;
        /**
         * 联系方式（订单号+联系方式，确认唯一性）
         */
        private String lxfs;
        /**
         * 数量
         */
        private String sl;
        /**
         * 下单日期（yyyy-MM-dd）
         */
        private String xdrq;
        /**
         * 实收金额（含邮费）
         */
        private String ssje;
        /**
         * 地区（如：杭州）
         */
        private String dq;
        /**
         * 地址（详细地址）
         */
        private String dz;
        /**
         * 订单状态
         */
        private String billstat;
        /**
         * 消息状态（默认是0）
         */
        private String messagestat;
        /**
         * 初始化状态（默认是1）
         */
        private String initflag;
        /**
         * 卖家留言（非空）
         */
        private String sellermes;
        /**
         * 买家留言（非空）
         */
        private String buyermes;

        /**
         * 账单
         */
        private String billentry;

        public CrmOrder() {
            this.messagestat = "0";
            this.initflag = "1";
            this.billentry = "";
        }
    }
}
