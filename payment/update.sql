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

 Date: 26/07/2019 17:08:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for configuration
-- ----------------------------
DROP TABLE IF EXISTS `configuration`;
CREATE TABLE `configuration`  (
  `environment` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'value of test or actual',
  `pay_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `state` tinyint(1) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of configuration
-- ----------------------------
INSERT INTO `configuration` VALUES ('test', 'https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder', 1);
INSERT INTO `configuration` VALUES ('actual', 'https://api.mch.weixin.qq.com/pay/unifiedorder', 0);

-- ----------------------------
-- Table structure for return_info
-- ----------------------------
DROP TABLE IF EXISTS `return_info`;
CREATE TABLE `return_info`  (
  `info_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `device_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nonce_str` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `prepay_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sign` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_type` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- 2021.11.2
ALTER TABLE `gmair_payment`.`trade` add `pay_client` varchar(50) NOT NULL DEFAULT 'OFFICIALACCOUNT';