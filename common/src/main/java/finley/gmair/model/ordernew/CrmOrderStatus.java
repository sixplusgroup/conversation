package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author zm
 * @date 2020/10/21 0021 9:35
 * @description CRM系统中的订单状态
 **/
public enum CrmOrderStatus {
    /*未处理：已付款，未发货的订单初始状态 -> 未发货*/
    UNTREATED("1"),

    /*预勘测，联系失败：客服咨询是否发货 -> 未发货*/
    PRE_SURVEY_CONTACT_FAILED("2"),

    /*预约安装，联系失败：用户已收货，安装师联系用户失败 -> 已发货*/
    SCHEDULE_INSTALLATION_CONTACT_FAILED("3"),

    /*未支付远程费 -> 未发货*/
    UNPAID_REMOTEFEE("4"),

    /*考虑是否安装 -> 未发货*/
    CONSIDER_WHETHER_INSTALL("5"),

    /*待拓展城市，等待排期 -> 未发货*/
    WAITING_SCHEDULED("6"),

    /*已勘测，客户要求延迟发货 -> 未发货*/
    SURVEYED_REQUEST_DELIVERY_DELAY("7"),

    /*已勘测，未发货 -> 未发货*/
    SURVEYED_NO_DELIVERY("8"),

    /*已发货，运输中 -> 已发货*/
    DELIVERED_IN_TRANSIT("9"),

    /*已发货，未排期 -> 已发货*/
    DELIVERED_NO_SCHEDULED("10"),

    /*已联系，等待客户通知 -> 未发货*/
    CONTACTED_WAITING_NOTIFICATION("11"),

    /*已经勘测玻璃 -> 未发货*/
    GLASS_SURVEYED("12"),

    /*排期中 -> 未发货*/
    SCHEDULING("13"),

    /*部分安装完成 -> 已收货*/
    PARTIAL_INSTALLATION_COMPLETED("14"),

    /*全部安装完成 -> 已收货*/
    ALL_INSTALLATION_COMPLETED("15"),

    /*已退货*/
    GOODS_RETURNED("16"),

    /*准备退货*/
    READY_TO_RETURN("17"),

    /*不在安装区域 -> 未发货*/
    NOT_IN_INSTALLATION_AREA("18"),

    /*已发配件箱代发货 -> 已发货*/
    DELIVERED_OF_ACCESSORIES_BOX("19"),

    /*优先发配件箱 -> 已发货*/
    PRIORITY_DELIVERED_BY_ACCESSORIES_BOX("20");

    private String value;

    CrmOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
