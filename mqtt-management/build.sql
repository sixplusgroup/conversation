-- Server version	8.0.22

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

CREATE DATABASE  IF NOT EXISTS `gmair_mqtt_management` DEFAULT CHARACTER SET utf8 ;
USE `gmair_mqtt_management`;

--
-- Table structure for table `action`
--

DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `action_id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(50) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `action_attribute_relate`
--

DROP TABLE IF EXISTS `action_attribute_relate`;
CREATE TABLE `action_attribute_relate` (
  `action_attribute_id` int NOT NULL AUTO_INCREMENT,
  `action_id` varchar(20) NOT NULL,
  `attribute_id` varchar(20) NOT NULL,
  PRIMARY KEY (`action_attribute_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Table structure for table `attribute`
--

DROP TABLE IF EXISTS `attribute`;
CREATE TABLE `attribute` (
  `attribute_id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(50) NOT NULL,
  `required` tinyint(1) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `machine_alert`
--

DROP TABLE IF EXISTS `machine_alert`;
CREATE TABLE `machine_alert` (
  `alert_id` varchar(20) NOT NULL,
  `machine_id` varchar(20) NOT NULL,
  `alert_code` int NOT NULL,
  `alert_msg` varchar(50) NOT NULL,
  `alert_status` tinyint(1) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`alert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `model`
--

DROP TABLE IF EXISTS `model`;
CREATE TABLE `model` (
  `model_id` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(50) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `model_action_relate`
--

DROP TABLE IF EXISTS `model_action_relate`;
CREATE TABLE `model_action_relate` (
  `model_action_id` int NOT NULL AUTO_INCREMENT,
  `model_id` varchar(20) NOT NULL,
  `action_id` varchar(20) NOT NULL,
  PRIMARY KEY (`model_action_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mqtt_firmware`
--

DROP TABLE IF EXISTS `mqtt_firmware`;
CREATE TABLE `mqtt_firmware` (
  `firmware_id` varchar(20) NOT NULL,
  `firmware_version` varchar(20) NOT NULL,
  `firmware_link` varchar(50) NOT NULL,
  `firmware_model` varchar(20) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`firmware_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `mqtt_topic`
--

DROP TABLE IF EXISTS `mqtt_topic`;
CREATE TABLE `mqtt_topic` (
  `topic_id` varchar(20) NOT NULL,
  `topic_detail` varchar(50) NOT NULL,
  `topic_description` varchar(50) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


