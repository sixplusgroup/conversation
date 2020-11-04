create database if not exists gmair_ordercentre;
use gmair_ordercentre;
drop table if exists `trade`;
create table trade
(
    trade_id          varchar(100)  not null comment '主键id',
    tid               bigint        null comment '交易编号 (父订单的交易编号)',
    num_iid           bigint        null comment '商品数字编号',
    num               bigint        null comment '商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。',
    status            varchar(50)   null comment '枚举类值：交易状态。',
    type              varchar(50)   null comment '枚举类值：交易类型列表。',
    shipping_type     varchar(50)   null comment '枚举类型：创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。',
    trade_from        varchar(50)   null comment '枚举类值：交易内部来源。',
    step_trade_status varchar(50)   null comment '枚举类值：分阶段付款的订单状态（例如万人团订单等），目前有三返回状态：
FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)
FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)
FRONT_PAID_FINAL_PAID(定金和尾款都付)',
    buyer_rate        tinyint(1)    null comment '买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false',
    created           datetime      null comment '交易创建时间。格式:yyyy-MM-dd HH:mm:ss',
    modified          datetime      null comment '交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-dd HH:mm:ss',
    pay_time          datetime      null comment '付款时间。格式:yyyy-MM-dd HH:mm:ss。订单的付款时间即为物流订单的创建时间。',
    consign_time      datetime      null comment '卖家发货时间。格式:yyyy-MM-dd HH:mm:ss',
    end_time          datetime      null comment '交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间 。格式:yyyy-MM-dd HH:mm:ss',
    receiver_name     varchar(30)   null comment '收货人的姓名',
    receiver_state    varchar(30)   null comment '收货人的所在省份',
    receiver_address  varchar(100)  null comment '收货人的详细地址',
    receiver_zip      varchar(30)   null comment '收货人的邮编',
    receiver_mobile   varchar(30)   null comment '收货人的手机号码',
    receiver_phone    varchar(30)   null comment '收货人的电话号码',
    receiver_country  varchar(30)   null comment '收货人国籍',
    receiver_city     varchar(30)   null comment '收货人的所在城市
注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面
建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
    receiver_district varchar(30)   null comment '收货人的所在地区
注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面
建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
    receiver_town     varchar(30)   null comment '收货人街道地址',
    tmall_delivery    tinyint(1)    null comment '是否为tmallDelivery',
    cn_service        varchar(10)   null comment '天猫直送服务',
    delivery_cps      varchar(10)   null comment '派送CP（运送公司？）',
    cutoff_minutes    varchar(20)   null comment '截单时间',
    delivery_time     datetime      null comment '发货时间',
    collect_time      datetime      null comment '揽收时间',
    dispatch_time     datetime      null comment '派送时间',
    sign_time         datetime      null comment '签收时间',
    es_time           int           null comment '时效：天',
    price             double        null comment '商品价格。精确到2位小数；单位：元。如：200.07，表示：200元7分',
    total_fee         double        null comment '商品金额（商品价格乘以数量的总金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    payment           double        null comment '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    adjust_fee        double        null comment '卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，单笔的话则跟[order].adjust_fee一样',
    received_payment  double        null comment '卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    discount_fee      double        null comment '优惠金额：可以使用trade.promotion_details查询系统优惠系统优惠金额（如打折，VIP，满就送等），精确到2位小数，单位：元。如：200.07，表示：200元7分',
    post_fee          double        null comment '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    credit_card_fee   double        null comment '使用信用卡支付金额数',
    step_paid_fee     double        null comment '分阶段付款的已付金额（万人团订单已付金额）',
    mode              int default 0 null comment '用作数据的数据状态判断，初始值为0，去模糊化后为1，推送到CRM后为2',
    constraint trade_trade_id_uindex
        unique (trade_id)
)
    comment '主订单表';

alter table trade
    add primary key (trade_id);



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
    machine_model   varchar(45)          null comment '机器型号 如: GM420',
    title           varchar(45)          null comment '商品标题,不能超过60字节',
    properties_name varchar(45)          null comment '属性名称',
    price           double               null comment '属于这个sku的商品的价格 取值范围:0-100000000;精确到2位小数;单位:元',
    block_flag      tinyint(1) default 0 null,
    create_at       datetime             null
)
    comment '商品项表';
-- ----------------------------
-- Records of sku_item
-- ----------------------------
INSERT INTO `sku_item` VALUES ('SKU20201103277z9h69', '626318647774', '4605307634707', 'GM280', '果麦家用壁挂式静音除甲醛新风机系统GM280不含基础安装费', '颜色分类:GM280', 4799, 0, '2020-11-03 04:39:50');
INSERT INTO `sku_item` VALUES ('SKU202011032hogui37', '586351602396', '4586264371325', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮活性炭滤网', '颜色分类:天蓝色', 529, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU202011033fzhyo94', '613251447053', '4311665104167', 'KN95', '果麦KN95口罩 双国标认证一次性独立包装10只 成人防护透气抑菌', '颜色分类:白色', 389, 0, '2020-11-03 04:39:50');
INSERT INTO `sku_item` VALUES ('SKU202011033ia67x30', '629686000800', '4656495615040', 'GM420', '果麦新风 新风系统 新风机HEPA滤网 适用果麦GM420 滤网套餐3片装', '颜色分类:军绿色', 786, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU2020110346u22i84', '629253642224', '4467733428778', 'KN95', '果麦KN95口罩 双国标认证一次性独立包装3只 成人防护透气抑菌', '颜色分类:白色', 159, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU202011034i28wo33', '620647911924', NULL, 'GM-XD001', '果麦消菌除毒家用电解次氯酸钠制造机安全无毒自制 GM-XD001', NULL, 599, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU202011034oru3997', '619798523093', '4372205960431', 'GM-WY101U', '果麦空气净化器家用客厅卧室净化紫外线除菌除霾无叶风扇WY101U', '颜色分类:白色', 1999, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU202011034vrz9n11', '630216410808', NULL, 'GM280服务费', 'GM280基础安装服务费', NULL, 500, 0, '2020-11-03 04:39:50');
INSERT INTO `sku_item` VALUES ('SKU202011036he2nu47', '618815141952', '4371859521771', 'GM-WY101', '果麦空气净化器家用客厅卧室除菌除霾消毒净化无叶风扇WY101', '颜色分类:白色', 1799, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103767l5w46', '618391118089', '4541481439139', '检测试纸', '果麦甲醛检测仪家用专业新房测甲醛室内空气质量测试甲醛盒仪器', '颜色分类:检测试纸（需与机器一同拍下）', 20, 0, '2020-11-03 04:39:50');
INSERT INTO `sku_item` VALUES ('SKU2020110379gxf475', '586351602396', '3981119665761', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮活性炭滤网', '颜色分类:军绿色', 268, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU202011037izaur46', '623449418582', '4405853492351', 'HEPA', '果麦新风 抗菌专效HEPA除醛除霾无叶风扇滤网防霉防潮活性炭滤网', '颜色分类:高效除醛滤芯', 218, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103a66l6415', '586351602396', '4376897817466', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮活性炭滤网', '颜色分类:深紫色', 298, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103ahoria6', '615588445417', NULL, '超区安装服务费', '超区安装服务费链接-详情咨询客服', NULL, 50, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103axg25x28', '628618924856', NULL, '换购', '【双十一】420机型1元预约换购专享链接', NULL, 1, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103e385h43', '586351602396', '4376897817462', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮活性炭滤网', '颜色分类:浅灰色', 248, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103f6ehue63', '626318647774', '4618300587636', 'GM280', '果麦家用壁挂式静音除甲醛新风机系统GM280不含基础安装费', '颜色分类:GM280+280HEPA滤网套餐', 5298, 0, '2020-11-03 04:39:50');
INSERT INTO `sku_item` VALUES ('SKU20201103hy87ny76', '626730544278', '4447408692787', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮GM280滤网', '颜色分类:浅灰色', 198, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103ifrfef54', '586351602396', '4376897817461', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮活性炭滤网', '颜色分类:白色', 248, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103l4yg9221', '586351602396', '4376897817465', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮活性炭滤网', '颜色分类:深灰色', 298, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103levn2w33', '618391118089', '4541481439140', '甲醛检测仪租赁', '果麦甲醛检测仪家用专业新房测甲醛室内空气质量测试甲醛盒仪器', '颜色分类:甲醛检测仪租赁', 199, 0, '2020-11-03 04:39:50');
INSERT INTO `sku_item` VALUES ('SKU20201103lg427l2', '623449418582', '4405853492350', 'HEPA', '果麦新风 抗菌专效HEPA除醛除霾无叶风扇滤网防霉防潮活性炭滤网', '颜色分类:高效除霾滤芯', 148, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103lowh5o59', '617446176546', NULL, 'GM-XD002', '果麦消菌除毒便携式家用电解次氯酸钠制造机安全无毒GM-XD002', NULL, 299, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103lye5ru3', '586310838669', '4149173443299', 'GM420', '果麦新风机家用壁挂式窗式空气净化全屋换气静音除甲醛系统GM420', '颜色分类:GM420（送价值500元基础安装服务）', 5999, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103nhzaf824', '624068379607', '4415325353564', 'GM500', '果麦新风系统 家用壁挂式新风机 全屋换气通风静音除醛抗菌 GM500', '颜色分类:GM500(送价值500元基础安装服务)', 13999, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103oni5x943', '606109518995', '4417870427123', '320Pro', '果麦新风机系统家用壁挂式空气净化器换气通风静音除醛抗菌320Pro', '颜色分类:GM320Pro（送价值500元基础安装服务）', 5998, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103uro44240', '586351602396', '4376897817467', 'HEPA', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮活性炭滤网', '颜色分类:深卡其布色', 268, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103v62fan8', '611174950440', '4478473111519', 'GM-WY100', '果麦空气净化器家用除菌除霾除甲醛客厅卧室净化冷热无叶风扇Y100', '颜色分类:优雅黑', 2899, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103vag7ne52', '622369656114', NULL, '邮费', '邮费链接', NULL, 1, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103wn63va9', '619969535360', NULL, '中奖', '直播间中奖请联系此链接客服！！', NULL, 999999, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103yexuvx98', '586220069067', '4147478638789', 'PM2.5检测仪', '果麦/GMAIR 便携式专业激光雾霾PM2.5空气质量检测仪 5秒响应', '颜色分类:军绿色', 368.01, 0, '2020-11-03 04:39:49');
INSERT INTO `sku_item` VALUES ('SKU20201103znzz6w62', '626730544278', '4447408692788', 'GM280', '果麦新风 抗菌专效HEPA新风系统新风机防霉防潮GM280滤网', '颜色分类:白色', 588, 0, '2020-11-03 04:39:48');
INSERT INTO `sku_item` VALUES ('SKU20201103zu962o97', '620343128529', '4388985557089', 'M600', '果麦新风机家用静音新风系统空气净化器除甲醛雾霾壁挂式抑菌M600', '颜色分类:M600（送价值500元基础安装服务）', 5998, 0, '2020-11-03 04:39:48');

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

