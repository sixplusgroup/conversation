/*
 Navicat MySQL Data Transfer

 Source Server         : navicatToMysql
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : gmair_payment

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 24/07/2019 13:21:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for trade
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade`  (
  `trade_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `out_trade_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trade_description` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nonce_str` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `total_fee` int(10) NOT NULL,
  `spbill_create_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trade_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `openid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `start_time` datetime(0) NOT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `trade_status` tinyint(1) NOT NULL,
  PRIMARY KEY (`trade_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
