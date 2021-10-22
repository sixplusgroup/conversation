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

 Date: 22/10/2021 14:56:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for integral_add_membership_view
-- ----------------------------
DROP TABLE IF EXISTS `integral_add_membership_view`;
CREATE TABLE `integral_add_membership_view`  (
  `id` bigint(20) NOT NULL,
  `membership_user_id` bigint(20) NOT NULL,
  `integral_value` int(11) NOT NULL DEFAULT 0,
  `is_confirmed` tinyint(1) NOT NULL DEFAULT 0,
  `confirmed_time` datetime(0) NULL DEFAULT NULL,
  `integral_add_create_time` datetime(0) NOT NULL,
  `description` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `device_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pictures` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `integral_add_block_flag` tinyint(1) NOT NULL DEFAULT 0,
  `membership_integral` int(11) NULL DEFAULT 0,
  `membership_type` int(11) NULL DEFAULT 0,
  `membership_user_create_time` datetime(0) NULL DEFAULT NULL,
  `consumer_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `membership_user_block_flag` tinyint(1) NULL DEFAULT 0,
  `user_mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consumer_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) COMMENT = 'VIEW';

-- ----------------------------
-- Table structure for integral_record_membership_view
-- ----------------------------
DROP TABLE IF EXISTS `integral_record_membership_view`;
CREATE TABLE `integral_record_membership_view`  (
  `id` bigint(20) NOT NULL,
  `membership_user_id` bigint(20) NOT NULL,
  `integral_value` int(11) NOT NULL DEFAULT 0,
  `is_add` tinyint(1) NOT NULL DEFAULT 0,
  `integral_record_create_time` datetime(0) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `integral_record_block_flag` tinyint(1) NOT NULL DEFAULT 0,
  `consumer_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consumer_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `membership_integral` int(11) NULL DEFAULT 0,
  `membership_type` int(11) NULL DEFAULT 0,
  `membership_user_create_time` datetime(0) NULL DEFAULT NULL,
  `membership_user_block_flag` tinyint(1) NULL DEFAULT 0,
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) COMMENT = 'VIEW';

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
  `description` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `device_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pictures` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT 0,
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
  `block_flag` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `membership_user_id`(`membership_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tz_membership_config
-- ----------------------------
DROP TABLE IF EXISTS `tz_membership_config`;
CREATE TABLE `tz_membership_config`  (
  `config_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `config_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
insert into tz_membership_config (config_name,config_value) values("sign_in_integral",100);
-- ----------------------------
-- Table structure for tz_membership_user
-- ----------------------------
DROP TABLE IF EXISTS `tz_membership_user`;
CREATE TABLE `tz_membership_user`  (
  `id` bigint(20) NOT NULL,
  `integral` int(11) NOT NULL DEFAULT 0,
  `membership_type` int(11) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NOT NULL,
  `consumer_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT 0,
  `user_mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consumer_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `integral`(`integral`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for integral_add_membership_view
-- ----------------------------
DROP VIEW IF EXISTS `integral_add_membership_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `integral_add_membership_view` AS select `ia`.`id` AS `id`,`ia`.`membership_user_id` AS `membership_user_id`,`ia`.`integral_value` AS `integral_value`,`ia`.`is_confirmed` AS `is_confirmed`,`ia`.`confirmed_time` AS `confirmed_time`,`ia`.`create_time` AS `integral_add_create_time`,`ia`.`description` AS `description`,`ia`.`device_model` AS `device_model`,`ia`.`pictures` AS `pictures`,`ia`.`block_flag` AS `integral_add_block_flag`,`mu`.`integral` AS `membership_integral`,`mu`.`membership_type` AS `membership_type`,`mu`.`create_time` AS `membership_user_create_time`,`mu`.`consumer_id` AS `consumer_id`,`mu`.`block_flag` AS `membership_user_block_flag`,`mu`.`user_mobile` AS `user_mobile`,`mu`.`pic` AS `pic`,`mu`.`nick_name` AS `nick_name`,`mu`.`consumer_name` AS `consumer_name` from (`tz_integral_add` `ia` left join `tz_membership_user` `mu` on((`ia`.`membership_user_id` = `mu`.`id`)));

-- ----------------------------
-- View structure for integral_record_membership_view
-- ----------------------------
DROP VIEW IF EXISTS `integral_record_membership_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `integral_record_membership_view` AS select `ir`.`id` AS `id`,`ir`.`membership_user_id` AS `membership_user_id`,`ir`.`integral_value` AS `integral_value`,`ir`.`is_add` AS `is_add`,`ir`.`create_time` AS `integral_record_create_time`,`ir`.`description` AS `description`,`ir`.`block_flag` AS `integral_record_block_flag`,`mu`.`consumer_id` AS `consumer_id`,`mu`.`user_mobile` AS `user_mobile`,`mu`.`consumer_name` AS `consumer_name`,`mu`.`integral` AS `membership_integral`,`mu`.`membership_type` AS `membership_type`,`mu`.`create_time` AS `membership_user_create_time`,`mu`.`block_flag` AS `membership_user_block_flag`,`mu`.`pic` AS `pic`,`mu`.`nick_name` AS `nick_name` from (`tz_integral_record` `ir` left join `tz_membership_user` `mu` on((`ir`.`membership_user_id` = `mu`.`id`)));

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
