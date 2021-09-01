/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : gmair_install

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 14/07/2021 20:18:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for install_userassign
-- ----------------------------
DROP TABLE IF EXISTS `install_userassign`;
CREATE TABLE `install_userassign`  (
  `userassign_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_consignee` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userassign_detail` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userassign_status` tinyint(1) NOT NULL COMMENT '是否确认',
  `userassign_date` date NULL DEFAULT NULL,
  `userassign_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'install',
  `remarks` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`userassign_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
