-- MySQL Script generated by MySQL Workbench
-- Mon Jan  7 19:14:11 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`co2_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`co2_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `average_co2` DOUBLE NOT NULL,
  `max_co2` INT(11) NOT NULL,
  `min_co2` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`power_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`power_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `power_on_minute` INT(11) NOT NULL,
  `power_off_minute` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`volume_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`volume_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `average_volume` DOUBLE NOT NULL,
  `max_volume` INT(11) NOT NULL,
  `min_volume` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`humid_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`humid_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `average_humid` DOUBLE NOT NULL,
  `max_humid` INT(11) NOT NULL,
  `min_humid` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`heat_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`heat_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `heat_on_minute` INT(11) NOT NULL,
  `heat_off_minute` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`indoor_pm25_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`indoor_pm25_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `average_pm25` DOUBLE NOT NULL,
  `max_pm25` INT(11) NOT NULL,
  `min_pm25` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`mode_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`mode_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `manual_minute` INT(11) NOT NULL,
  `cosy_minute` INT(11) NOT NULL,
  `warm_minute` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `gmair_data_analysis`.`temp_daily`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`temp_daily` (
  `status_id` VARCHAR(50) NOT NULL,
  `machine_id` VARCHAR(50) NOT NULL,
  `average_temp` DOUBLE NOT NULL,
  `max_temp` INT(11) NOT NULL,
  `min_temp` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


#2019-01-11
ALTER TABLE `gmair_data_analysis`.`mode_daily`
CHANGE COLUMN `manual_minute` `auto_minute` INT(11) NOT NULL ,
CHANGE COLUMN `cosy_minute` `manual_minute` INT(11) NOT NULL ,
CHANGE COLUMN `warm_minute` `sleep_minute` INT(11) NOT NULL ;

ALTER TABLE `gmair_data_analysis`.`mode_hourly`
CHANGE COLUMN `manual_minute` `auto_minute` INT(11) NOT NULL ,
CHANGE COLUMN `cosy_minute` `manual_minute` INT(11) NOT NULL ,
CHANGE COLUMN `warm_minute` `sleep_minute` INT(11) NOT NULL ;


#2019-01-21

CREATE TABLE IF NOT EXISTS `gmair_data_analysis`.`component_mean` (
  `record_id` VARCHAR(50) NOT NULL,
  `date_index` VARCHAR(50) NOT NULL,
  `component` VARCHAR(50) NOT NULL,
  `component_times` INT(11) NOT NULL,
  `component_mean` DOUBLE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`record_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;