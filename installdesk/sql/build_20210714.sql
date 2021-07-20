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

 Date: 14/07/2021 20:15:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assign_action
-- ----------------------------
DROP TABLE IF EXISTS `assign_action`;
CREATE TABLE `assign_action`  (
  `action_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `action_message` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assign_code
-- ----------------------------
DROP TABLE IF EXISTS `assign_code`;
CREATE TABLE `assign_code`  (
  `assign_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code_serial` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`assign_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assign_goods
-- ----------------------------
DROP TABLE IF EXISTS `assign_goods`;
CREATE TABLE `assign_goods`  (
  `goods_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assign_member_view
-- ----------------------------
DROP TABLE IF EXISTS `assign_member_view`;
CREATE TABLE `assign_member_view`  (
  `assign_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code_value` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_detail` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_status` tinyint(1) NOT NULL,
  `assign_date` date NULL DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  `consumer_consignee` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_source` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_description` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_region` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'install'
) COMMENT = 'VIEW';

-- ----------------------------
-- Table structure for assign_report_view
-- ----------------------------
DROP TABLE IF EXISTS `assign_report_view`;
CREATE TABLE `assign_report_view`  (
  `assign_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_detail` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_status` tinyint(1) NOT NULL,
  `assign_date` date NULL DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `consumer_consignee` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_source` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'install',
  `team_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code_value` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NOT NULL
) COMMENT = 'VIEW';

-- ----------------------------
-- Table structure for assign_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `assign_snapshot`;
CREATE TABLE `assign_snapshot`  (
  `snapshot_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code_value` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `picture_path` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `wifi_configured` tinyint(1) NOT NULL DEFAULT 0,
  `install_method` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NOT NULL,
  `hole` tinyint(1) NOT NULL,
  PRIMARY KEY (`snapshot_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assin_feedback
-- ----------------------------
DROP TABLE IF EXISTS `assin_feedback`;
CREATE TABLE `assin_feedback`  (
  `feedback_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_rank` int(11) NOT NULL,
  `feedback_detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `company_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `company_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `message_title` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `company_detail` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`company_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dispatch_record
-- ----------------------------
DROP TABLE IF EXISTS `dispatch_record`;
CREATE TABLE `dispatch_record`  (
  `record_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `assign_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `team_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gmair_config
-- ----------------------------
DROP TABLE IF EXISTS `gmair_config`;
CREATE TABLE `gmair_config`  (
  `config_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `component_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `component_status` tinyint(1) NOT NULL DEFAULT 0,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for install_assign
-- ----------------------------
DROP TABLE IF EXISTS `install_assign`;
CREATE TABLE `install_assign`  (
  `assign_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_consignee` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_detail` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_region` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code_value` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_status` tinyint(1) NOT NULL,
  `assign_date` date NULL DEFAULT NULL,
  `assign_source` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_description` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assign_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'install',
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`assign_id`) USING BTREE,
  INDEX `fk_install_assign_install_team1_idx`(`team_id`) USING BTREE,
  CONSTRAINT `fk_install_assign_install_team1` FOREIGN KEY (`team_id`) REFERENCES `install_team` (`team_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for install_assign_type_info
-- ----------------------------
DROP TABLE IF EXISTS `install_assign_type_info`;
CREATE TABLE `install_assign_type_info`  (
  `assign_type_info_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `assign_type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `picture_num_limit` int(11) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`assign_type_info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for install_team
-- ----------------------------
DROP TABLE IF EXISTS `install_team`;
CREATE TABLE `install_team`  (
  `team_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_area` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`team_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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

-- ----------------------------
-- Table structure for order_express
-- ----------------------------
DROP TABLE IF EXISTS `order_express`;
CREATE TABLE `order_express`  (
  `express_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `express_status` tinyint(1) NULL DEFAULT NULL,
  `company` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `block_flag` tinyint(1) NULL DEFAULT 0,
  `express_num` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`express_id`) USING BTREE,
  UNIQUE INDEX `order_express_express_id_uindex`(`express_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for picture_md5
-- ----------------------------
DROP TABLE IF EXISTS `picture_md5`;
CREATE TABLE `picture_md5`  (
  `image_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `picture_path` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`image_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_member
-- ----------------------------
DROP TABLE IF EXISTS `team_member`;
CREATE TABLE `team_member`  (
  `member_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `wechat_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_role` tinyint(1) NOT NULL,
  `member_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`member_id`) USING BTREE,
  INDEX `fk_team_member_install_team_idx`(`team_id`) USING BTREE,
  CONSTRAINT `fk_team_member_install_team` FOREIGN KEY (`team_id`) REFERENCES `install_team` (`team_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_member_view
-- ----------------------------
DROP TABLE IF EXISTS `team_member_view`;
CREATE TABLE `team_member_view`  (
  `member_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `wechat_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_role` tinyint(1) NOT NULL,
  `team_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) COMMENT = 'VIEW';

-- ----------------------------
-- Table structure for team_view
-- ----------------------------
DROP TABLE IF EXISTS `team_view`;
CREATE TABLE `team_view`  (
  `team_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_area` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_member_count` bigint(21) NOT NULL DEFAULT 0
) COMMENT = 'VIEW';

-- ----------------------------
-- Table structure for team_watch
-- ----------------------------
DROP TABLE IF EXISTS `team_watch`;
CREATE TABLE `team_watch`  (
  `watch_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `member_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `team_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`watch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_watch_view
-- ----------------------------
DROP TABLE IF EXISTS `team_watch_view`;
CREATE TABLE `team_watch_view`  (
  `watch_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `member_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `team_id` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `member_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `team_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) COMMENT = 'VIEW';

-- ----------------------------
-- View structure for assign_member_view
-- ----------------------------
DROP VIEW IF EXISTS `assign_member_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `assign_member_view` AS select `install_assign`.`assign_id` AS `assign_id`,`install_assign`.`code_value` AS `code_value`,`install_assign`.`assign_detail` AS `assign_detail`,`install_team`.`team_id` AS `team_id`,`install_team`.`team_name` AS `team_name`,`team_member`.`member_id` AS `member_id`,`team_member`.`member_name` AS `member_name`,`install_assign`.`assign_status` AS `assign_status`,`install_assign`.`assign_date` AS `assign_date`,`install_assign`.`block_flag` AS `block_flag`,`install_assign`.`create_time` AS `create_time`,`install_assign`.`consumer_consignee` AS `consumer_consignee`,`install_assign`.`consumer_phone` AS `consumer_phone`,`install_assign`.`consumer_address` AS `consumer_address`,`install_assign`.`assign_source` AS `assign_source`,`install_assign`.`assign_description` AS `assign_description`,`install_assign`.`assign_region` AS `assign_region`,`install_assign`.`assign_type` AS `assign_type` from ((`install_assign` left join `install_team` on((`install_assign`.`team_id` = `install_team`.`team_id`))) left join `team_member` on((`install_assign`.`member_id` = `team_member`.`member_id`))) where (`install_assign`.`block_flag` = 0);

-- ----------------------------
-- View structure for assign_report_view
-- ----------------------------
DROP VIEW IF EXISTS `assign_report_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `assign_report_view` AS select `install_assign`.`assign_id` AS `assign_id`,`install_assign`.`assign_detail` AS `assign_detail`,`install_assign`.`assign_status` AS `assign_status`,`install_assign`.`assign_date` AS `assign_date`,`install_assign`.`block_flag` AS `block_flag`,`install_assign`.`consumer_consignee` AS `consumer_consignee`,`install_assign`.`consumer_phone` AS `consumer_phone`,`install_assign`.`consumer_address` AS `consumer_address`,`install_assign`.`assign_source` AS `assign_source`,`install_assign`.`assign_type` AS `assign_type`,`install_team`.`team_id` AS `team_id`,`install_team`.`team_name` AS `team_name`,`team_member`.`member_id` AS `member_id`,`team_member`.`member_name` AS `member_name`,`assign_snapshot`.`code_value` AS `code_value`,`install_assign`.`create_time` AS `create_time` from (((`install_assign` join `assign_snapshot`) join `install_team`) join `team_member`) where ((`install_assign`.`block_flag` = 0) and (`assign_snapshot`.`block_flag` = 0) and (`install_assign`.`assign_status` = 3) and (`install_assign`.`team_id` = `install_team`.`team_id`) and (`assign_snapshot`.`assign_id` = `install_assign`.`assign_id`) and (`install_assign`.`member_id` = `team_member`.`member_id`)) order by `install_assign`.`team_id`,`install_assign`.`member_id`,`install_assign`.`create_time`;

-- ----------------------------
-- View structure for team_member_view
-- ----------------------------
DROP VIEW IF EXISTS `team_member_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `team_member_view` AS select `tm`.`member_id` AS `member_id`,`tm`.`team_id` AS `team_id`,`tm`.`member_name` AS `member_name`,`tm`.`member_phone` AS `member_phone`,`tm`.`wechat_id` AS `wechat_id`,`tm`.`member_role` AS `member_role`,`it`.`team_name` AS `team_name` from (`team_member` `tm` left join `install_team` `it` on(((`tm`.`block_flag` = 0) and (`it`.`team_id` = `tm`.`team_id`))));

-- ----------------------------
-- View structure for team_view
-- ----------------------------
DROP VIEW IF EXISTS `team_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `team_view` AS select `it`.`team_id` AS `team_id`,`it`.`team_name` AS `team_name`,`it`.`team_area` AS `team_area`,`it`.`team_description` AS `team_description`,ifnull(count(`tm`.`member_id`),0) AS `team_member_count` from (`install_team` `it` left join `team_member` `tm` on(((`it`.`team_id` = `tm`.`team_id`) and (`tm`.`block_flag` = 0)))) group by `it`.`team_id`;

-- ----------------------------
-- View structure for team_watch_view
-- ----------------------------
DROP VIEW IF EXISTS `team_watch_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `team_watch_view` AS select `tw`.`watch_id` AS `watch_id`,`tw`.`member_id` AS `member_id`,`tw`.`team_id` AS `team_id`,`tm`.`member_name` AS `member_name`,`it`.`team_name` AS `team_name` from ((`team_watch` `tw` join `team_member` `tm`) join `install_team` `it`) where ((convert(`tw`.`member_id` using utf8) = `tm`.`member_id`) and (convert(`tw`.`team_id` using utf8) = `it`.`team_id`) and (`tw`.`block_flag` = 0));

SET FOREIGN_KEY_CHECKS = 1;
