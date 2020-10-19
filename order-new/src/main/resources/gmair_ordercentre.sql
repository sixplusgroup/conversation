/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : gmair_ordercentre

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 18/10/2020 17:39:04
*/
CREATE DATABASE `gmair_ordercentre`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
                          `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
                          `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'trade_id from trade表',
                          `num_iid` bigint NULL DEFAULT NULL COMMENT '商品数字ID',
                          `cid` bigint NULL DEFAULT NULL COMMENT '交易商品对应的类目ID',
                          `oid` bigint NULL DEFAULT NULL COMMENT '子订单编号',
                          `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项：订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。可选值:\nTRADE_NO_CREATE_PAY(没有创建支付宝交易)\nWAIT_BUYER_PAY(等待买家付款)\nWAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)\nWAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)\nTRADE_BUYER_SIGNED(买家已签收,货到付款专用)\nTRADE_FINISHED(交易成功)\nTRADE_CLOSED(付款以后用户退款成功，交易自动关闭)\nTRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)\nPAY_PENDING(国际信用卡支付付款确认中)',
                          `num` int NULL DEFAULT NULL COMMENT '购买数量（取值范围:大于零的整数）',
                          `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品标题',
                          `sku_id` bigint NULL DEFAULT NULL COMMENT '商品的最小库存单位Sku的id.可以通过taobao.item.sku.get获取详细的Sku信息',
                          `sku_properties_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU的值。如：机身颜色:黑色;手机套餐:官方标配',
                          `seller_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '卖家类型，可选值为：B（商城商家），C（普通卖家）',
                          `refund_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '枚举类值：退款状态。\r\n\r\n可选值：\r\nWAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意)\r\nWAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货)\r\nWAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)\r\nSELLER_REFUSE_BUYER(卖家拒绝退款)\r\nCLOSED(退款关闭)\r\nSUCCESS(退款成功)',
                          `seller_rate` tinyint(1) NULL DEFAULT NULL COMMENT '卖家是否已评价。可选值：true(已评价)，false(未评价)',
                          `buyer_rate` tinyint(1) NULL DEFAULT NULL COMMENT '	买家是否已评价。可选值：true(已评价)，false(未评价)',
                          `is_daixiao` tinyint(1) NULL DEFAULT NULL COMMENT '表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true；反之，该值为false。',
                          `pic_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片的绝对路径',
                          `store_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发货的仓库编码',
                          `shipping_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子订单的运送方式（卖家对订单进行多次发货之后，一个主订单下的子订单的运送方式可能不同，用order.shipping_type来区分子订单的运送方式）',
                          `logistics_company` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子订单发货的快递公司名称',
                          `invoice_no` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子订单所在包裹的运单号',
                          `consign_time` datetime(0) NULL DEFAULT NULL COMMENT '子订单发货时间，当卖家对订单进行了多次发货，子订单的发货时间和主订单的发货时间可能不一样了，那么就需要以子订单的时间为准。（没有进行多次发货的订单，主订单的发货时间和子订单的发货时间都一样）',
                          `price` double NULL DEFAULT NULL COMMENT '商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分',
                          `total_fee` double NULL DEFAULT NULL COMMENT '应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
                          `payment` double NULL DEFAULT NULL COMMENT '子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。建议使用退款前的实付金额减去退款单中的实际退款金额计算。',
                          `adjust_fee` double NULL DEFAULT NULL COMMENT '手工调整金额.格式为:1.01;单位:元;精确到小数点后两位.',
                          `discount_fee` double NULL DEFAULT NULL COMMENT '子订单级订单优惠金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
                          `part_mjz_discount` double NULL DEFAULT NULL COMMENT '优惠分摊',
                          `divide_order_fee` double NULL DEFAULT NULL COMMENT '分摊之后的实付金额',
                          PRIMARY KEY (`order_id`) USING BTREE,
                          UNIQUE INDEX `order_order_id_uindex`(`order_id`) USING BTREE,
                          INDEX `trade_id`(`trade_id`) USING BTREE,
                          CONSTRAINT `order_ibfk_1` FOREIGN KEY (`trade_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for trade
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade`  (
                          `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
                          `tid` bigint NULL DEFAULT NULL COMMENT '交易编号 (父订单的交易编号)',
                          `num_iid` bigint NULL DEFAULT NULL COMMENT '商品数字编号',
                          `num` int NULL DEFAULT NULL COMMENT '商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。',
                          `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '枚举类值：交易状态。\r\n可选值: \r\nTRADE_NO_CREATE_PAY(没有创建支付宝交易) \r\n\r\nWAIT_BUYER_PAY(等待买家付款) \r\n\r\nSELLER_CONSIGNED_PART(卖家部分发货) \r\n\r\nWAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)\r\n\r\nWAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)\r\n\r\nTRADE_BUYER_SIGNED(买家已签收,货到付款专用)\r\n\r\nTRADE_FINISHED(交易成功)\r\n\r\nTRADE_CLOSED(付款以后用户退款成功，交易自动关闭)\r\n\r\nTRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)\r\n\r\nPAY_PENDING(国际信用卡支付付款确认中)\r\n\r\nWAIT_PRE_AUTH_CONFIRM(0元购合约中)\r\n\r\nPAID_FORBID_CONSIGN(拼团中订单或者发货强管控的订单，已付款但禁止发货)',
                          `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '枚举类值：交易类型列表。\r\n\r\n同时查询多种交易类型可用逗号分隔。\r\n默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据\r\n\r\n可选值：\r\nfixed(一口价)\r\nauction(拍卖)\r\nguarantee_trade(一口价、拍卖)\r\nauto_delivery(自动发货)\r\nindependent_simple_trade(旺店入门版交易)\r\nindependent_shop_trade(旺店标准版交易)\r\nec(直冲)\r\ncod(货到付款)\r\nfenxiao(分销)\r\ngame_equipment(游戏装备)\r\nshopex_trade(ShopEX交易)\r\nnetcn_trade(万网交易)\r\nexternal_trade(统一外部交易)\r\no2o_offlinetrade（O2O交易\r\nstep (万人团)\r\nnopaid(无付款订单)\r\npre_auth_type(预授权0元购机交易)',
                          `shipping_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '枚举类型：创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。\r\n\r\n可选值：\r\nfree(卖家包邮)\r\npost(平邮)\r\nexpress(快递)\r\nems(EMS)\r\nvirtual(虚拟发货)\r\n25(次日必达)\r\n26(预约配送)。',
                          `trade_from` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '枚举类值：交易内部来源。\r\n\r\nWAP(手机)\r\nHITAO(嗨淘)\r\nTOP(TOP平台)\r\nTAOBAO(普通淘宝)\r\nJHS(聚划算)\r\n\r\n一笔订单可能同时有以上多个标记，则以逗号分隔',
                          `step_trade_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '枚举类值：分阶段付款的订单状态（例如万人团订单等），目前有三返回状态：\r\n\r\nFRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)\r\nFRONT_PAID_FINAL_NOPAID(定金已付尾款未付)\r\nFRONT_PAID_FINAL_PAID(定金和尾款都付)',
                          `buyer_rate` tinyint(1) NULL DEFAULT NULL COMMENT '买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false',
                          `created` datetime(0) NULL DEFAULT NULL COMMENT '交易创建时间。格式:yyyy-MM-dd HH:mm:ss',
                          `modified` datetime(0) NULL DEFAULT NULL COMMENT '交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-dd HH:mm:ss',
                          `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '付款时间。格式:yyyy-MM-dd HH:mm:ss。订单的付款时间即为物流订单的创建时间。',
                          `consign_time` datetime(0) NULL DEFAULT NULL COMMENT '卖家发货时间。格式:yyyy-MM-dd HH:mm:ss',
                          `end_time` datetime(0) NULL DEFAULT NULL COMMENT '交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间 。格式:yyyy-MM-dd HH:mm:ss',
                          `receiver_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的姓名',
                          `receiver_state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的所在省份',
                          `receiver_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的详细地址',
                          `receiver_zip` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的邮编',
                          `receiver_mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的手机号码',
                          `receiver_phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的电话号码',
                          `receiver_country` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人国籍',
                          `receiver_city` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的所在城市\n注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面\n建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
                          `receiver_district` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人的所在地区\n注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面\n建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
                          `receiver_town` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人街道地址',
                          `tmall_delivery` tinyint(1) NULL DEFAULT NULL COMMENT '是否为tmallDelivery',
                          `cn_service` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '天猫直送服务',
                          `delivery_cps` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '派送CP（运送公司？）',
                          `cutoff_minutes` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '截单时间',
                          `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
                          `collect_time` datetime(0) NULL DEFAULT NULL COMMENT '揽收时间',
                          `dispatch_time` datetime(0) NULL DEFAULT NULL COMMENT '派送时间',
                          `sign_time` datetime(0) NULL DEFAULT NULL COMMENT '签收时间',
                          `es_time` int NULL DEFAULT NULL COMMENT '时效：天',
                          `price` double NULL DEFAULT NULL COMMENT '商品价格。精确到2位小数；单位：元。如：200.07，表示：200元7分',
                          `total_fee` double NULL DEFAULT NULL COMMENT '商品金额（商品价格乘以数量的总金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
                          `payment` double NULL DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
                          `adjust_fee` double NULL DEFAULT NULL COMMENT '卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，单笔的话则跟[order].adjust_fee一样',
                          `received_payment` double NULL DEFAULT NULL COMMENT '卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
                          `discount_fee` double NULL DEFAULT NULL COMMENT '优惠金额：可以使用trade.promotion_details查询系统优惠系统优惠金额（如打折，VIP，满就送等），精确到2位小数，单位：元。如：200.07，表示：200元7分',
                          `post_fee` double NULL DEFAULT NULL COMMENT '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
                          `credit_card_fee` double NULL DEFAULT NULL COMMENT '使用信用卡支付金额数',
                          `step_paid_fee` double NULL DEFAULT NULL COMMENT '分阶段付款的已付金额（万人团订单已付金额）',
                          PRIMARY KEY (`trade_id`) USING BTREE,
                          UNIQUE INDEX `trade_trade_id_uindex`(`trade_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '主订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trade
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
