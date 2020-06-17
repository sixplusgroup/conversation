/*
Navicat MySQL Data Transfer

Source Server         : zjd
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : gmair_mqtt

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2020-01-10 13:48:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `mqtt_logger_record`
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_logger_record`;
CREATE TABLE `mqtt_logger_record` (
  `record_id` varchar(32) NOT NULL,
  `topic_context` varchar(255) DEFAULT NULL,
  `payload_context` longtext,
  `block_flag` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `machine_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;