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

-- ----------------------------
-- Records of mqtt_logger_record
-- ----------------------------
INSERT INTO `mqtt_logger_record` VALUES ('202001094e5vra0', 'client/FA/34567263672/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:46:10', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('202001096e5haz80', 'client/FA/142453556362/rfid_enabled', 'close', '0', '2020-01-09 17:38:56', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('202001098xl9x524', 'client/FA/142453556362/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:42:09', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('2020010995ia8i80', 'client/FA/34567263672/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:45:33', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('202001099guwe876', 'client/FA/142453556362/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:44:34', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109ayr83a10', 'client/FA/142453556362/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:44:53', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109ewvru436', 'client/FA/142453556362/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:44:25', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109fxgz4759', 'client/FA/34567263672/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 17:36:55', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('20200109fya9wx12', 'client/FA/142453556362/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:43:53', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109g68gfu18', 'client/FA/142453556362/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:43:21', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109gn3ehn67', 'client/FA/142453556362/update', 'decrese the vol ++sh d', '0', '2020-01-09 17:36:55', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109gounzh49', 'client/FA/34567263672/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:45:27', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('20200109h8yw5724', 'client/FA/142453556362/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 17:36:55', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109n9xwxu21', 'client/FA/142453556362/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:41:35', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109nl68358', 'client/FA/34567263672/update', 'decrese the vol ++sh d', '0', '2020-01-09 17:36:55', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('20200109no4oly26', 'client/FA/142453556362/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:44:38', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109ny3vo449', 'client/FA/34567263672/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:45:39', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('20200109r4h95359', 'client/FA/34567263672/update', 'decrese the vol ++sh d', '0', '2020-01-09 15:45:36', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('20200109v8rfyw26', 'client/FA/34567263672/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:46:17', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('20200109vn2ihi11', 'client/FA/34567263672/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:46:12', '34567263672');
INSERT INTO `mqtt_logger_record` VALUES ('20200109wzyhoy73', 'client/FA/142453556362/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:42:14', '142453556362');
INSERT INTO `mqtt_logger_record` VALUES ('20200109yr73o560', 'client/FA/34567263672/rfid_enabled', 'decrese the vol ++sh d', '0', '2020-01-09 15:46:15', '34567263672');
