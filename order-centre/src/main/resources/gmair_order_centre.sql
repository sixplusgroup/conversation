create database if not exists gmair_order_centre;
use gmair_order_centre;

drop table if exists `unified_shop`;
create table `unified_shop`
(
    shop_id         varchar(50)          not null comment '主键id'
        primary key,
    platform        int                  not null comment '枚举类值：平台',
    sid             varchar(50)          not null comment '业务店铺id',
    channel         varchar(30)          not null comment '渠道来源',
    shop_title      varchar(50)          not null comment '店铺标题',
    start_pull_time datetime             null comment '同步开始时间',
    last_pull_time  datetime             null comment '上次同步时间',
    app_key         varchar(100)         not null comment 'appKey',
    app_secret      varchar(100)         not null comment 'appSecret',
    session_key     varchar(100)         not null comment '店铺授权token',
    authorize_time  datetime             null comment '店铺授权时间',
    expire_time     datetime             null comment '授权过期时间',
    sys_create_time datetime   default current_timestamp comment '系统创建时间',
    sys_update_time datetime   default current_timestamp on update current_timestamp comment '系统更新时间',
    sys_block_flag  tinyint(1) default 0 not null comment '是否删除',
    unique key `uniqueIndex` (`sid`, `platform`)
)
    DEFAULT CHARSET = utf8
    comment '店铺表';

insert into unified_shop
values ("jd1", 2, "sid", "61", "title", "2021-12-25 00:00:00", "2021-12-25 00:00:00",
        "6861B1CF2C2FA7D763AA34F0254E6BAB", "f9f7f2f76f62497cb74875b930295ca9", "fd764b94465e45328315d1bf12029330riot",
        null, null, null, null, 0);

drop table if exists `unified_trade`;
create table `unified_trade`
(
    trade_id           varchar(50)          not null comment '主键id'
        primary key,
    tid                varchar(50)          not null comment '交易编号 (父订单的交易编号)',
    trade_platform     int                  not null comment '枚举类值：平台',
    shop_id            varchar(50)          not null comment '店铺表主键',
    status             int                  not null comment '主订单状态（多子订单要看子订单状态）',
    create_time        datetime             not null comment '交易创建时间。格式:yyyy-MM-dd HH:mm:ss',
    update_time        datetime             not null comment '交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-dd HH:mm:ss',
    pay_time           datetime             null comment '交易付款时间。格式:yyyy-MM-dd HH:mm:ss。订单的付款时间即为物流订单的创建时间。',
    end_time           datetime             null comment '交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间 。格式:yyyy-MM-dd HH:mm:ss',
    consignee_name     varchar(30)          null comment '收货人的姓名',
    consignee_phone    varchar(30)          null comment '收货人的电话号码',
    consignee_province varchar(30)          null comment '收货人的所在省份',
    consignee_city     varchar(30)          null comment '收货人的所在城市',
    consignee_district varchar(30)          null comment '收货人的所在地区',
    consignee_address  varchar(100)         null comment '收货人的详细地址',
    price              double               null comment '商品总价格。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    payment            double               null comment '实付金额(含邮费等)。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    post_fee           double               null comment '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    buyer_message      varchar(255)         null comment '买家留言',
    seller_memo        varchar(255)         null comment '卖家备注',
    is_fuzzy           tinyint(1)           not null comment '是否模糊化',
    crm_push_status    int        default 0 not null comment 'CRM推送状态',
    drift_push_status  int        default 0 not null comment 'CRM推送状态',
    sys_create_time    datetime   default current_timestamp comment '系统创建时间',
    sys_update_time    datetime   default current_timestamp on update current_timestamp comment '系统更新时间',
    sys_block_flag     tinyint(1) default 0 not null comment '是否删除',
    unique key `uniqueIndex` (`tid`, `trade_platform`)
)
    DEFAULT CHARSET = utf8
    comment '主订单表';

drop table if exists `unified_order`;
create table `unified_order`
(
    order_id          varchar(50)          not null comment '主键id'
        primary key,
    trade_id          varchar(50)          not null comment 'trade_id from trade表',
    tid               varchar(50)          not null comment '主订单编号',
    oid               varchar(50)          not null comment '子订单编号',
    status            int                  not null comment '字典项：订单状态',
    num_id            varchar(50)          null comment '商品数字ID',
    sku_id            varchar(50)          null comment '商品的最小库存单位Sku的id',
    num               int                  null comment '购买数量（取值范围:大于零的整数）',
    price             double               null comment '商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分',
    payment           double               null comment '子订单实付金额(不含邮费),多子订单为（主订单实付-邮费）分摊;精确到2位小数;单位:元。如:200.07，表示:200元7分',
    logistics_company varchar(30)          null comment '子订单发货的快递公司名称',
    logistics_id      varchar(50)          null comment '子订单所在包裹的运单号',
    sys_create_time   datetime   default current_timestamp comment '系统创建时间',
    sys_update_time   datetime   default current_timestamp on update current_timestamp comment '系统更新时间',
    sys_block_flag    tinyint(1) default 0 not null comment '是否删除'
)
    DEFAULT CHARSET = utf8
    comment '子订单表';

drop table if exists `unified_sku_item`;
create table `unified_sku_item`
(
    item_id         varchar(50)          not null comment '主键'
        primary key,
    platform        int                  not null comment '枚举类值：平台',
    shop_id         varchar(50)          not null comment '店铺表主键',
    num_id          varchar(50)          null comment '商品id',
    sku_id          varchar(50)          null comment '商品的最小库存单位Sku的id',
    channel         varchar(30)          null comment '渠道来源',
    machine_model   varchar(30)          null comment '机器型号 如: GM420',
    is_fictitious   tinyint(1) default 0 null comment '是否为虚拟产品(默认为false)',
    title           varchar(50)          null comment '商品标题,不能超过60字节',
    properties_name varchar(255)         null comment '属性名称',
    price           double               null comment '属于这个sku的商品的价格 取值范围:0-100000000;精确到2位小数;单位:元',
    sys_create_time datetime   default current_timestamp comment '系统创建时间',
    sys_update_time datetime   default current_timestamp on update current_timestamp comment '系统更新时间',
    sys_block_flag  tinyint(1) default 0 not null comment '是否删除',
    unique key `uniqueIndex` (`shop_id`, `num_id`, `sku_id`)
)
    DEFAULT CHARSET = utf8
    comment '商品项表';

drop table if exists `trade_record`;
create table trade_record
(
    record_id       varchar(45)          not null comment '主键'
        primary key,
    platform        int                  not null comment '枚举类值：平台',
    shop_id         varchar(50)          not null comment '店铺id',
    tid             varchar(50)          not null comment '主订单id',
    order_id        varchar(50)          null comment '子订单id',
    record_message  varchar(255)         null comment '记录信息',
    trade_data      json                 null comment '订单数据',
    user_name       varchar(30)          null comment '操作人',
    sys_create_time datetime   default current_timestamp comment '系统创建时间',
    sys_update_time datetime   default current_timestamp on update current_timestamp comment '系统更新时间',
    sys_block_flag  tinyint(1) default 0 not null comment '是否删除'
)
    DEFAULT CHARSET = utf8
    comment '订单操作记录表';

