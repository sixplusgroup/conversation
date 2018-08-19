create database gmair_mode;

use gmair_mode;

CREATE TABLE `gmair_mode`.`machine_mode` (
  `mode_id` VARCHAR(20) NOT NULL,
  `machine_id` VARCHAR(20) NOT NULL,
  `mode_type` TINYINT(1) NOT NULL,
  `mode_name` VARCHAR(45) NOT NULL,
  `mode_status` TINYINT(1) NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`mode_id`));

CREATE TABLE `gmair_mode`.`model_configuration` (
  `config_id` VARCHAR(20) NOT NULL,
  `pm2_5` INT NOT NULL,
  `co2` INT NOT NULL,
  `temperature` INT NOT NULL,
  `volume_setting` INT NOT NULL,
  `heat_setting` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`config_id`));
