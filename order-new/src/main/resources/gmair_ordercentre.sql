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

 Date: 15/10/2020 23:59:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for buyer
-- ----------------------------
DROP TABLE IF EXISTS `buyer`;
CREATE TABLE `buyer`  (
  `buyer_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `buyer_nick` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '买家昵称',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`buyer_id`) USING BTREE,
  UNIQUE INDEX `buyer_buyer_id_uindex`(`buyer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '买家信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of buyer
-- ----------------------------

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type`  (
  `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典项类型主键id',
  `type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项类型名称',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`type_id`) USING BTREE,
  UNIQUE INDEX `dict_type_type_id_uindex`(`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典项类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dict_type
-- ----------------------------
INSERT INTO `dict_type` VALUES ('test_type_id_01', '交易状态（订单状态）', '2020-10-15 19:59:20', '2020-10-15 19:59:20', 'admin', 'admin');
INSERT INTO `dict_type` VALUES ('test_type_id_02', '退款状态', '2020-10-15 21:07:18', '2020-10-15 21:07:18', 'admin', 'admin');
INSERT INTO `dict_type` VALUES ('test_type_id_03', '物流方式', '2020-10-15 22:55:20', '2020-10-15 22:55:20', 'admin', 'admin');
INSERT INTO `dict_type` VALUES ('test_type_id_04', '分阶段付款的订单状态', '2020-10-15 23:14:42', '2020-10-15 23:14:42', 'admin', 'admin');
INSERT INTO `dict_type` VALUES ('test_type_id_05', '交易内部来源', '2020-10-15 23:18:34', '2020-10-15 23:18:34', 'admin', 'admin');
INSERT INTO `dict_type` VALUES ('test_type_id_06', '交易类型', '2020-10-15 23:22:21', '2020-10-15 23:22:21', 'admin', 'admin');

-- ----------------------------
-- Table structure for map_trade_buyer
-- ----------------------------
DROP TABLE IF EXISTS `map_trade_buyer`;
CREATE TABLE `map_trade_buyer`  (
  `teade_buyer_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易-买家关联表主键id',
  `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'trade_id from trade表',
  `buyer_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'buyer_id from buyer表',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`teade_buyer_id`) USING BTREE,
  UNIQUE INDEX `map_trade_buyer_mtb_id_uindex`(`teade_buyer_id`) USING BTREE,
  INDEX `trade_id`(`trade_id`) USING BTREE,
  INDEX `buyer_id`(`buyer_id`) USING BTREE,
  CONSTRAINT `map_trade_buyer_ibfk_1` FOREIGN KEY (`trade_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `map_trade_buyer_ibfk_2` FOREIGN KEY (`buyer_id`) REFERENCES `buyer` (`buyer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易-买家关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of map_trade_buyer
-- ----------------------------

-- ----------------------------
-- Table structure for map_trade_receiver
-- ----------------------------
DROP TABLE IF EXISTS `map_trade_receiver`;
CREATE TABLE `map_trade_receiver`  (
  `trade_receiver_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'trade_id from trade表',
  `receiver_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'receiver_id from receiver表',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`trade_receiver_id`) USING BTREE,
  UNIQUE INDEX `map_trade_receiver_mtr_id_uindex`(`trade_receiver_id`) USING BTREE,
  INDEX `trade_id`(`trade_id`) USING BTREE,
  INDEX `receiver_id`(`receiver_id`) USING BTREE,
  CONSTRAINT `map_trade_receiver_ibfk_1` FOREIGN KEY (`trade_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `map_trade_receiver_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `receiver` (`receiver_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单-收货人关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of map_trade_receiver
-- ----------------------------

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
  `refund_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项：退款状态。退款状态。可选值 WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品标题',
  `num` int NULL DEFAULT NULL COMMENT '购买数量（取值范围:大于零的整数）',
  `seller_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '卖家类型，可选值为：B（商城商家），C（普通卖家）',
  `seller_rate` tinyint(1) NULL DEFAULT NULL COMMENT '卖家是否已评价。可选值：true(已评价)，false(未评价)',
  `buyer_rate` tinyint(1) NULL DEFAULT NULL COMMENT '	买家是否已评价。可选值：true(已评价)，false(未评价)',
  `is_daixiao` tinyint(1) NULL DEFAULT NULL COMMENT '表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true；反之，该值为false。',
  `sku_id` bigint NULL DEFAULT NULL COMMENT '商品的最小库存单位Sku的id.可以通过taobao.item.sku.get获取详细的Sku信息',
  `sku_properties_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU的值。如：机身颜色:黑色;手机套餐:官方标配',
  `pic_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片的绝对路径',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `order_order_id_uindex`(`order_id`) USING BTREE,
  INDEX `trade_id`(`trade_id`) USING BTREE,
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`trade_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for order_delivery
-- ----------------------------
DROP TABLE IF EXISTS `order_delivery`;
CREATE TABLE `order_delivery`  (
  `order_delivery_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'order_id from order表',
  `store_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发货的仓库编码',
  `shipping_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子订单的运送方式（卖家对订单进行多次发货之后，一个主订单下的子订单的运送方式可能不同，用order.shipping_type来区分子订单的运送方式）',
  `logistics_company` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子订单发货的快递公司名称',
  `invoice_no` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子订单所在包裹的运单号',
  `consign_time` datetime(0) NULL DEFAULT NULL COMMENT '子订单发货时间，当卖家对订单进行了多次发货，子订单的发货时间和主订单的发货时间可能不一样了，那么就需要以子订单的时间为准。（没有进行多次发货的订单，主订单的发货时间和子订单的发货时间都一样）',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`order_delivery_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  CONSTRAINT `order_delivery_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '子订单配送信息补充表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_delivery
-- ----------------------------

-- ----------------------------
-- Table structure for order_price
-- ----------------------------
DROP TABLE IF EXISTS `order_price`;
CREATE TABLE `order_price`  (
  `order_price_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'order_id from order表',
  `price` double NULL DEFAULT NULL COMMENT '商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `total_fee` double NULL DEFAULT NULL COMMENT '应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `payment` double NULL DEFAULT NULL COMMENT '子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。建议使用退款前的实付金额减去退款单中的实际退款金额计算。',
  `adjust_fee` double NULL DEFAULT NULL COMMENT '手工调整金额.格式为:1.01;单位:元;精确到小数点后两位.',
  `discount_fee` double NULL DEFAULT NULL COMMENT '子订单级订单优惠金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `part_mjz_discount` double NULL DEFAULT NULL COMMENT '优惠分摊',
  `divide_order_fee` double NULL DEFAULT NULL COMMENT '分摊之后的实付金额',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`order_price_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  CONSTRAINT `order_price_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '子订单价格补充表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_price
-- ----------------------------

-- ----------------------------
-- Table structure for receiver
-- ----------------------------
DROP TABLE IF EXISTS `receiver`;
CREATE TABLE `receiver`  (
  `receiver_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人主键id',
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
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`receiver_id`) USING BTREE,
  UNIQUE INDEX `receiver_receiver_id_uindex`(`receiver_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收货人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of receiver
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `dict_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典表主键id',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项标识符',
  `dict_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项名称',
  `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键(sys_dict_type的主键)',
  `parent_dict_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父字典项id',
  `priority` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '优先级，用作排序（默认是0）',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `sys_dict_dict_id_uindex`(`dict_id`) USING BTREE,
  INDEX `sys_dict_ibfk_1`(`type_id`) USING BTREE,
  CONSTRAINT `sys_dict_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `dict_type` (`type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('test_dict_id_01', 'WAIT_BUYER_PAY', '等待买家付款', 'test_type_id_01', 'test_dict_id_12', NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_02', '\r\nWAIT_SELLER_SEND_GOODS', '等待卖家发货', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_03', 'SELLER_CONSIGNED_PART', '卖家部分发货', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_04', 'WAIT_BUYER_CONFIRM_GOODS', '等待买家确认收货', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_05', 'TRADE_BUYER_SIGNED', '买家已签收（货到付款专用）', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_06', 'TRADE_FINISHED', '交易成功', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_07', 'TRADE_CLOSED', '付款以后用户退款成功，交易自动关闭', 'test_type_id_01', 'test_dict_id_13', NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_08', 'TRADE_CLOSED_BY_TAOBAO', '付款以前，卖家或买家主动关闭交易', 'test_type_id_01', 'test_dict_id_13', NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_09', 'TRADE_NO_CREATE_PAY', '没有创建外部交易（支付宝交易）', 'test_type_id_01', 'test_dict_id_12', NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_10', 'WAIT_PRE_AUTH_CONFIRM', '余额宝0元购合约中', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_11', 'PAY_PENDING', '国际信用卡支付付款确认中', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_12', 'ALL_WAIT_PAY', '所有买家未付款的交易（包含：WAIT_BUYER_PAY、TRADE_NO_CREATE_PAY）\r', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_13', 'ALL_CLOSED', '所有关闭的交易（包含：TRADE_CLOSED、TRADE_CLOSED_BY_TAOBAO）', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_14', 'PAID_FORBID_CONSIGN', '拼团中订单或者发货强管控的订单，已付款但处于禁止发货发货状态', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_15', 'WAIT_SELLER_SEND_GOODS', '等待卖家发货,即:买家已付款', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_16', 'SELLER_CONSIGNED_PART', '卖家部分发货', 'test_type_id_01', NULL, NULL, '2020-10-15 20:15:28', '2020-10-15 20:15:28', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_17', 'WAIT_SELLER_AGREE', '买家已经申请退款，等待卖家同意', 'test_type_id_02', NULL, NULL, '2020-10-15 21:12:36', '2020-10-15 21:12:36', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_18', 'WAIT_BUYER_RETURN_GOODS', '卖家已经同意退款，等待买家退货', 'test_type_id_02', NULL, NULL, '2020-10-15 21:12:36', '2020-10-15 21:12:36', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_19', 'WAIT_SELLER_CONFIRM_GOODS', '买家已经退货，等待卖家确认收货', 'test_type_id_02', NULL, NULL, '2020-10-15 21:12:36', '2020-10-15 21:12:36', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_20', 'ELLER_REFUSE_BUYER', '卖家拒绝退款', 'test_type_id_02', NULL, NULL, '2020-10-15 21:12:36', '2020-10-15 21:12:36', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_21', 'CLOSED', '退款关闭', 'test_type_id_02', NULL, NULL, '2020-10-15 21:12:36', '2020-10-15 21:12:36', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_22', 'SUCCESS', '退款成功', 'test_type_id_02', NULL, NULL, '2020-10-15 21:12:36', '2020-10-15 21:12:36', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_23', 'free', '卖家包邮', 'test_type_id_03', NULL, NULL, '2020-10-15 23:00:56', '2020-10-15 23:00:56', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_24', 'post', '平邮', 'test_type_id_03', NULL, NULL, '2020-10-15 23:00:56', '2020-10-15 23:00:56', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_25', 'express', '快递', 'test_type_id_03', NULL, NULL, '2020-10-15 23:00:56', '2020-10-15 23:00:56', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_26', 'ems', 'EMS', 'test_type_id_03', NULL, NULL, '2020-10-15 23:00:56', '2020-10-15 23:00:56', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_27', 'virtual', '虚拟发货', 'test_type_id_03', NULL, NULL, '2020-10-15 23:00:56', '2020-10-15 23:00:56', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_28', '25', '次日必达', 'test_type_id_03', NULL, NULL, '2020-10-15 23:00:56', '2020-10-15 23:00:56', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_29', '26', '预约配送', 'test_type_id_03', NULL, NULL, '2020-10-15 23:00:56', '2020-10-15 23:00:56', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_30', 'FRONT_NOPAID_FINAL_NOPAID', '定金未付尾款未付', 'test_type_id_04', NULL, NULL, '2020-10-15 23:15:57', '2020-10-15 23:15:57', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_31', 'FRONT_PAID_FINAL_NOPAID', '定金已付尾款未付', 'test_type_id_04', NULL, NULL, '2020-10-15 23:15:57', '2020-10-15 23:15:57', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_32', 'FRONT_PAID_FINAL_PAID', '定金和尾款都付', 'test_type_id_04', NULL, NULL, '2020-10-15 23:15:57', '2020-10-15 23:15:57', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_33', 'WAP', '手机', 'test_type_id_05', NULL, NULL, '2020-10-15 23:20:25', '2020-10-15 23:20:25', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_34', 'HITAO', '嗨淘', 'test_type_id_05', NULL, NULL, '2020-10-15 23:20:25', '2020-10-15 23:20:25', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_35', 'TOP', 'TOP平台', 'test_type_id_05', NULL, NULL, '2020-10-15 23:20:25', '2020-10-15 23:20:25', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_36', 'TAOBAO', '普通淘宝', 'test_type_id_05', NULL, NULL, '2020-10-15 23:20:25', '2020-10-15 23:20:25', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_37', 'JHS', '聚划算', 'test_type_id_05', NULL, NULL, '2020-10-15 23:20:25', '2020-10-15 23:20:25', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_38', 'fixed', '一口价', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_39', 'auction', '拍卖', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_40', 'guarantee_trade', '一口价、拍卖', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_41', 'auto_delivery', '自动发货', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_42', 'independent_simple_trade', '旺店入门版交易', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_43', 'independent_shop_trade', '旺店标准版交易', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_44', 'ec', '直冲', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_45', 'cod', '货到付款', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_46', 'fenxiao', '分销', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_47', 'game_equipment', '游戏装备', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_48', 'shopex_trade', 'ShopEX交易', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_49', 'netcn_trade', '万网交易', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_50', 'external_trade', '统一外部交易', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_51', 'o2o_offlinetrade', 'O2O交易', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_52', 'step ', '万人团', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_53', 'nopaid', '无付款订单', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');
INSERT INTO `sys_dict` VALUES ('test_dict_id_54', 'pre_auth_type', '预授权0元购机交易', 'test_type_id_06', NULL, NULL, '2020-10-15 23:27:15', '2020-10-15 23:27:15', 'admin', 'admin');

-- ----------------------------
-- Table structure for trade
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade`  (
  `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `tid` bigint NULL DEFAULT NULL COMMENT '交易编号 (父订单的交易编号)',
  `num_iid` bigint NULL DEFAULT NULL COMMENT '商品数字编号',
  `num` int NULL DEFAULT NULL COMMENT '商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项，交易状态。可选值: * TRADE_NO_CREATE_PAY(没有创建支付宝交易) * WAIT_BUYER_PAY(等待买家付款) * SELLER_CONSIGNED_PART(卖家部分发货) * WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款) * WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) * TRADE_BUYER_SIGNED(买家已签收,货到付款专用) * TRADE_FINISHED(交易成功) * TRADE_CLOSED(付款以后用户退款成功，交易自动关闭) * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易) * PAY_PENDING(国际信用卡支付付款确认中) * WAIT_PRE_AUTH_CONFIRM(0元购合约中) * PAID_FORBID_CONSIGN(拼团中订单或者发货强管控的订单，已付款但禁止发货)',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项，交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed(一口价) auction(拍卖) guarantee_trade(一口价、拍卖) auto_delivery(自动发货) independent_simple_trade(旺店入门版交易) independent_shop_trade(旺店标准版交易) ec(直冲) cod(货到付款) fenxiao(分销) game_equipment(游戏装备) shopex_trade(ShopEX交易) netcn_trade(万网交易) external_trade(统一外部交易)o2o_offlinetrade（O2O交易）step (万人团)nopaid(无付款订单)pre_auth_type(预授权0元购机交易)',
  `shipping_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项，创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：free(卖家包邮),post(平邮),express(快递),ems(EMS),virtual(虚拟发货)，25(次日必达)，26(预约配送)。',
  `trade_from` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项，交易内部来源。WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)一笔订单可能同时有以上多个标记，则以逗号分隔',
  `step_trade_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项，分阶段付款的订单状态（例如万人团订单等），目前有三返回状态：FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)，FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)，FRONT_PAID_FINAL_PAID(定金和尾款都付)',
  `buyer_rate` tinyint(1) NULL DEFAULT NULL COMMENT '买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false',
  `created` datetime(0) NULL DEFAULT NULL COMMENT '交易创建时间。格式:yyyy-MM-dd HH:mm:ss',
  `modified` datetime(0) NULL DEFAULT NULL COMMENT '交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-dd HH:mm:ss',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '付款时间。格式:yyyy-MM-dd HH:mm:ss。订单的付款时间即为物流订单的创建时间。',
  `consign_time` datetime(0) NULL DEFAULT NULL COMMENT '卖家发货时间。格式:yyyy-MM-dd HH:mm:ss',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间 。格式:yyyy-MM-dd HH:mm:ss',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`trade_id`) USING BTREE,
  UNIQUE INDEX `trade_trade_id_uindex`(`trade_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '主订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trade
-- ----------------------------

-- ----------------------------
-- Table structure for trade_delivery
-- ----------------------------
DROP TABLE IF EXISTS `trade_delivery`;
CREATE TABLE `trade_delivery`  (
  `trade_delivery_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'trade_id from trade表',
  `tmall_delivery` tinyint(1) NULL DEFAULT NULL COMMENT '是否为tmallDelivery',
  `cn_service` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '天猫直送服务',
  `delivery_cps` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '派送CP（运送公司？）',
  `cutoff_minutes` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '截单时间',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `collect_time` datetime(0) NULL DEFAULT NULL COMMENT '揽收时间',
  `dispatch_time` datetime(0) NULL DEFAULT NULL COMMENT '派送时间',
  `sign_time` datetime(0) NULL DEFAULT NULL COMMENT '签收时间',
  `es_time` int NULL DEFAULT NULL COMMENT '时效：天',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`trade_delivery_id`) USING BTREE,
  UNIQUE INDEX `trade_delivery_tt_id_uindex`(`trade_delivery_id`) USING BTREE,
  INDEX `trade_id`(`trade_id`) USING BTREE,
  CONSTRAINT `trade_delivery_ibfk_1` FOREIGN KEY (`trade_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易运送补充表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trade_delivery
-- ----------------------------

-- ----------------------------
-- Table structure for trade_price
-- ----------------------------
DROP TABLE IF EXISTS `trade_price`;
CREATE TABLE `trade_price`  (
  `trade_price_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主键id',
  `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'trade_id from trade表',
  `price` double NULL DEFAULT NULL COMMENT '商品价格。精确到2位小数；单位：元。如：200.07，表示：200元7分',
  `total_fee` double NULL DEFAULT NULL COMMENT '商品金额（商品价格乘以数量的总金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `adjust_fee` double NULL DEFAULT NULL COMMENT '卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，单笔的话则跟[order].adjust_fee一样',
  `payment` double NULL DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `received_payment` double NULL DEFAULT NULL COMMENT '卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `discount_fee` double NULL DEFAULT NULL COMMENT '优惠金额：可以使用trade.promotion_details查询系统优惠系统优惠金额（如打折，VIP，满就送等），精确到2位小数，单位：元。如：200.07，表示：200元7分',
  `post_fee` double NULL DEFAULT NULL COMMENT '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `credit_card_fee` double NULL DEFAULT NULL COMMENT '使用信用卡支付金额数',
  `step_paid_fee` double NULL DEFAULT NULL COMMENT '分阶段付款的已付金额（万人团订单已付金额）',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  INDEX `trade_id`(`trade_id`) USING BTREE,
  CONSTRAINT `trade_price_ibfk_1` FOREIGN KEY (`trade_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易价格补充表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trade_price
-- ----------------------------

-- ----------------------------
-- Table structure for trade_supplement
-- ----------------------------
DROP TABLE IF EXISTS `trade_supplement`;
CREATE TABLE `trade_supplement`  (
  `trade_supplement_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `trade_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'trade_id from trade表',
  `post_gate_declare` tinyint(1) NULL DEFAULT NULL COMMENT '是否是邮关订单',
  `cross_bonded_declare` tinyint(1) NULL DEFAULT NULL COMMENT '是否是跨境订单',
  `order_tax_promotion_fee` double NULL DEFAULT NULL COMMENT '天猫国际计税优惠金额',
  `is_o2o_passport` tinyint(1) NULL DEFAULT NULL COMMENT '是否是智慧门店订单，只有true，或者 null 两种情况',
  `sys_create_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的创建时间',
  `sys_modified_time` timestamp(0) NULL DEFAULT NULL COMMENT '在系统中的最后修改时间',
  `sys_create_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的创建人id',
  `sys_modified_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '在系统中的修改人id',
  PRIMARY KEY (`trade_supplement_id`) USING BTREE,
  UNIQUE INDEX `trade_supplement_ts_id_uindex`(`trade_supplement_id`) USING BTREE,
  INDEX `trade_id`(`trade_id`) USING BTREE,
  CONSTRAINT `trade_supplement_ibfk_1` FOREIGN KEY (`trade_id`) REFERENCES `trade` (`trade_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'trade 表补充字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trade_supplement
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
