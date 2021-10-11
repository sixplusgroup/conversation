/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : gmair_membership

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 11/10/2021 14:18:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tz_integral_add
-- ----------------------------
DROP TABLE IF EXISTS `tz_integral_add`;
CREATE TABLE `tz_integral_add`  (
  `id` bigint(20) NOT NULL,
  `membership_user_id` bigint(20) NOT NULL,
  `integral_value` int(11) NOT NULL DEFAULT 0,
  `is_confirmed` tinyint(1) NOT NULL DEFAULT 0,
  `confirmed_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `membership_user_id`(`membership_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tz_integral_record
-- ----------------------------
DROP TABLE IF EXISTS `tz_integral_record`;
CREATE TABLE `tz_integral_record`  (
  `id` bigint(20) NOT NULL,
  `membership_user_id` bigint(20) NOT NULL,
  `integral_value` int(11) NOT NULL DEFAULT 0,
  `is_add` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `membership_user_id`(`membership_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tz_membership_user
-- ----------------------------
DROP TABLE IF EXISTS `tz_membership_user`;
CREATE TABLE `tz_membership_user`  (
  `id` bigint(20) NOT NULL,
  `integral` int(11) NOT NULL DEFAULT 0,
  `membership_type` int(11) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NOT NULL,
  `consumer_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `integral`(`integral`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Event structure for gmair_membership_integral_maintain
-- ----------------------------
DROP EVENT IF EXISTS `gmair_membership_integral_maintain`;
delimiter ;;
CREATE EVENT `gmair_membership_integral_maintain`
ON SCHEDULE
EVERY '1' YEAR STARTS '2023-01-03 00:00:00'
ON COMPLETION PRESERVE
COMMENT 'integralmaintain'
DO update gmair_membership.membership_consumer set first_integral = second_integral, second_integral = 0
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
