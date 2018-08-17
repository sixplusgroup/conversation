create database gmair_mode;

use database gmair_mode;

CREATE TABLE `gmair_mode`.`machine_mode` (
  `mode_id` VARCHAR(20) NOT NULL,
  `machine_id` VARCHAR(20) NOT NULL,
  `mode_type` TINYINT(1) NOT NULL,
  `mode_name` VARCHAR(45) NOT NULL,
  `mode_status` TINYINT(1) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`mode_id`));
