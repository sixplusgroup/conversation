drop table if exists `trade`;
create table trade
(
    trade_id          varchar(100) not null comment '主键id'
        primary key,
    tid               bigint       null comment '交易编号 (父订单的交易编号)',
    num_iid           bigint       null comment '商品数字编号',
    num               bigint       null comment '商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。',
    status            varchar(50)  null comment '枚举类值：交易状态。',
    type              varchar(50)  null comment '枚举类值：交易类型列表。',
    shipping_type     varchar(50)  null comment '枚举类型：创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。',
    trade_from        varchar(50)  null comment '枚举类值：交易内部来源。',
    step_trade_status varchar(50)  null comment '枚举类值：分阶段付款的订单状态（例如万人团订单等），目前有三返回状态：
FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)
FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)
FRONT_PAID_FINAL_PAID(定金和尾款都付)',
    buyer_rate        tinyint(1)   null comment '买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false',
    created           datetime     null comment '交易创建时间。格式:yyyy-MM-dd HH:mm:ss',
    modified          datetime     null comment '交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-dd HH:mm:ss',
    pay_time          datetime     null comment '付款时间。格式:yyyy-MM-dd HH:mm:ss。订单的付款时间即为物流订单的创建时间。',
    consign_time      datetime     null comment '卖家发货时间。格式:yyyy-MM-dd HH:mm:ss',
    end_time          datetime     null comment '交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间 。格式:yyyy-MM-dd HH:mm:ss',
    receiver_name     varchar(30)  null comment '收货人的姓名',
    receiver_state    varchar(30)  null comment '收货人的所在省份',
    receiver_address  varchar(100)  null comment '收货人的详细地址',
    receiver_zip      varchar(30)  null comment '收货人的邮编',
    receiver_mobile   varchar(30)  null comment '收货人的手机号码',
    receiver_phone    varchar(30)  null comment '收货人的电话号码',
    receiver_country  varchar(30)  null comment '收货人国籍',
    receiver_city     varchar(30)  null comment '收货人的所在城市
注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面
建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
    receiver_district varchar(30)  null comment '收货人的所在地区
注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面
建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
    receiver_town     varchar(30)  null comment '收货人街道地址',
    tmall_delivery    tinyint(1)   null comment '是否为tmallDelivery',
    cn_service        varchar(10)  null comment '天猫直送服务',
    delivery_cps      varchar(10)  null comment '派送CP（运送公司？）',
    cutoff_minutes    varchar(20)  null comment '截单时间',
    delivery_time     datetime     null comment '发货时间',
    collect_time      datetime     null comment '揽收时间',
    dispatch_time     datetime     null comment '派送时间',
    sign_time         datetime     null comment '签收时间',
    es_time           int          null comment '时效：天',
    price             double       null comment '商品价格。精确到2位小数；单位：元。如：200.07，表示：200元7分',
    total_fee         double       null comment '商品金额（商品价格乘以数量的总金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    payment           double       null comment '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    adjust_fee        double       null comment '卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，单笔的话则跟[order].adjust_fee一样',
    received_payment  double       null comment '卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    discount_fee      double       null comment '优惠金额：可以使用trade.promotion_details查询系统优惠系统优惠金额（如打折，VIP，满就送等），精确到2位小数，单位：元。如：200.07，表示：200元7分',
    post_fee          double       null comment '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    credit_card_fee   double       null comment '使用信用卡支付金额数',
    step_paid_fee     double       null comment '分阶段付款的已付金额（万人团订单已付金额）',
    constraint trade_trade_id_uindex
        unique (trade_id)
)
    comment '主订单表';



drop table if exists `order`;
create table `order`
(
    order_id            varchar(100) not null comment '主键id'
        primary key,
    trade_id            varchar(100) null comment 'trade_id from trade表',
    num_iid             bigint       null comment '商品数字ID',
    cid                 bigint       null comment '交易商品对应的类目ID',
    oid                 bigint       null comment '子订单编号',
    status              varchar(50)  null comment '字典项：订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。可选值:
TRADE_NO_CREATE_PAY(没有创建支付宝交易)
WAIT_BUYER_PAY(等待买家付款)
WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)
WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)
TRADE_BUYER_SIGNED(买家已签收,货到付款专用)
TRADE_FINISHED(交易成功)
TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)
TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)
PAY_PENDING(国际信用卡支付付款确认中)',
    num                 bigint       null comment '购买数量（取值范围:大于零的整数）',
    title               varchar(50)  null comment '商品标题',
    sku_id              bigint       null comment '商品的最小库存单位Sku的id.可以通过taobao.item.sku.get获取详细的Sku信息',
    sku_properties_name varchar(50)  null comment 'SKU的值。如：机身颜色:黑色;手机套餐:官方标配',
    seller_type         varchar(2)   null comment '卖家类型，可选值为：B（商城商家），C（普通卖家）',
    refund_status       varchar(50)  null comment '枚举类值：退款状态。
可选值：
WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意)
WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货)
WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)
SELLER_REFUSE_BUYER(卖家拒绝退款)
CLOSED(退款关闭)
SUCCESS(退款成功)',
    seller_rate         tinyint(1)   null comment '卖家是否已评价。可选值：true(已评价)，false(未评价)',
    buyer_rate          tinyint(1)   null comment '	买家是否已评价。可选值：true(已评价)，false(未评价)',
    is_daixiao          tinyint(1)   null comment '表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true；反之，该值为false。',
    pic_path            varchar(100) null comment '商品图片的绝对路径',
    store_code          varchar(50)  null comment '发货的仓库编码',
    shipping_type       varchar(30)  null comment '子订单的运送方式（卖家对订单进行多次发货之后，一个主订单下的子订单的运送方式可能不同，用order.shipping_type来区分子订单的运送方式）',
    logistics_company   varchar(30)  null comment '子订单发货的快递公司名称',
    invoice_no          varchar(30)  null comment '子订单所在包裹的运单号',
    consign_time        datetime     null comment '子订单发货时间，当卖家对订单进行了多次发货，子订单的发货时间和主订单的发货时间可能不一样了，那么就需要以子订单的时间为准。（没有进行多次发货的订单，主订单的发货时间和子订单的发货时间都一样）',
    price               double       null comment '商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    total_fee           double       null comment '应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    payment             double       null comment '子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。建议使用退款前的实付金额减去退款单中的实际退款金额计算。',
    adjust_fee          double       null comment '手工调整金额.格式为:1.01;单位:元;精确到小数点后两位.',
    discount_fee        double       null comment '子订单级订单优惠金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    part_mjz_discount   double       null comment '优惠分摊',
    divide_order_fee    double       null comment '分摊之后的实付金额',
    constraint order_order_id_uindex
        unique (order_id)
)
    comment '订单表';
create index trade_id
    on `order` (trade_id);



drop table if exists `sku_item`;
create table sku_item
(
    item_id         varchar(45)          not null comment '主键'
        primary key,
    num_iid         varchar(45)          null comment '商品id',
    sku_id          varchar(45)          null comment '商品的最小库存单位Sku的id',
    title           varchar(45)          null comment '商品标题,不能超过60字节',
    properties_name varchar(45)          null comment '属性名称',
    price           double               null comment '属于这个sku的商品的价格 取值范围:0-100000000;精确到2位小数;单位:元',
    block_flag      tinyint(1) default 0 null,
    create_at       datetime             null
)
    comment '商品项表';



drop table if exists `tb_user`;
create table tb_user
(
    user_id          varchar(45)  not null comment '主键'
        primary key,
    start_sync_time  datetime     null comment '同步开始时间',
    last_update_time datetime     null comment '上次同步时间',
    session_key      varchar(100) null comment '用户授权token',
    authorize_time   datetime     null comment '用户授权时间',
    block_flag       tinyint(1)   null,
    create_at        datetime     null
)
    comment '淘宝卖家用户表';



INSERT INTO `tb_user`
VALUES ('SEL2020102088rn7213', '2020-10-01 00:00:00', NULL,
        '6100e02ceb111ceb4e6ff02506458185b2f7afa5fce9f232200642250842', '2020-10-01 00:00:00', 0,
        '2020-10-21 17:40:23');

