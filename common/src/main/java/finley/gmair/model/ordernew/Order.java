package finley.gmair.model.ordernew;

import java.util.Date;
import lombok.Data;

/**
    * 订单表
    */
@Data
public class Order {
    /**
    * 主键id
    */
    private String orderId;

    /**
    * trade_id from trade表
    */
    private String tradeId;

    /**
    * 商品数字ID
    */
    private Long numIid;

    /**
    * 交易商品对应的类目ID
    */
    private Long cid;

    /**
    * 子订单编号
    */
    private Long oid;

    /**
    * 字典项：订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。可选值:
TRADE_NO_CREATE_PAY(没有创建支付宝交易)
WAIT_BUYER_PAY(等待买家付款)
WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)
WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)
TRADE_BUYER_SIGNED(买家已签收,货到付款专用)
TRADE_FINISHED(交易成功)
TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)
TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)
PAY_PENDING(国际信用卡支付付款确认中)
    */
    private String status;

    /**
    * 购买数量（取值范围:大于零的整数）
    */
    private Integer num;

    /**
    * 商品标题
    */
    private String title;

    /**
    * 商品的最小库存单位Sku的id.可以通过taobao.item.sku.get获取详细的Sku信息
    */
    private Long skuId;

    /**
    * SKU的值。如：机身颜色:黑色;手机套餐:官方标配
    */
    private String skuPropertiesName;

    /**
    * 卖家类型，可选值为：B（商城商家），C（普通卖家）
    */
    private String sellerType;

    /**
    * 枚举类值：退款状态。

可选值：
WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意)
WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货)
WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)
SELLER_REFUSE_BUYER(卖家拒绝退款)
CLOSED(退款关闭)
SUCCESS(退款成功)
    */
    private String refundStatus;

    /**
    * 卖家是否已评价。可选值：true(已评价)，false(未评价)
    */
    private Boolean sellerRate;

    /**
    * 	买家是否已评价。可选值：true(已评价)，false(未评价)
    */
    private Boolean buyerRate;

    /**
    * 表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true；反之，该值为false。
    */
    private Boolean isDaixiao;

    /**
    * 商品图片的绝对路径
    */
    private String picPath;

    /**
    * 发货的仓库编码
    */
    private String storeCode;

    /**
    * 子订单的运送方式（卖家对订单进行多次发货之后，一个主订单下的子订单的运送方式可能不同，用order.shipping_type来区分子订单的运送方式）
    */
    private String shippingType;

    /**
    * 子订单发货的快递公司名称
    */
    private String logisticsCompany;

    /**
    * 子订单所在包裹的运单号
    */
    private String invoiceNo;

    /**
    * 子订单发货时间，当卖家对订单进行了多次发货，子订单的发货时间和主订单的发货时间可能不一样了，那么就需要以子订单的时间为准。（没有进行多次发货的订单，主订单的发货时间和子订单的发货时间都一样）
    */
    private Date consignTime;

    /**
    * 商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分
    */
    private Double price;

    /**
    * 应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分
    */
    private Double totalFee;

    /**
    * 子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。建议使用退款前的实付金额减去退款单中的实际退款金额计算。
    */
    private Double payment;

    /**
    * 手工调整金额.格式为:1.01;单位:元;精确到小数点后两位.
    */
    private Double adjustFee;

    /**
    * 子订单级订单优惠金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
    */
    private Double discountFee;

    /**
    * 优惠分摊
    */
    private Double partMjzDiscount;

    /**
    * 分摊之后的实付金额
    */
    private Double divideOrderFee;
}