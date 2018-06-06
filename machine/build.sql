-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_machine
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_machine
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_machine` DEFAULT CHARACTER SET utf8 ;
USE `gmair_machine` ;

-- -----------------------------------------------------
-- Table `gmair_machine`.`goods`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`goods` (
  `goods_id` VARCHAR(20) NOT NULL,
  `goods_name` VARCHAR(45) NOT NULL,
  `goods_description` VARCHAR(50) NOT NULL,
  `goods_price` DOUBLE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`goods_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`goods_model`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`goods_model` (
  `model_id` VARCHAR(20) NOT NULL,
  `goods_id` VARCHAR(20) NOT NULL,
  `model_code` VARCHAR(45) NOT NULL,
  `model_name` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`model_id`),
  INDEX `fk_goods_model_goods_idx` (`goods_id` ASC),
  CONSTRAINT `fk_goods_model_goods`
  FOREIGN KEY (`goods_id`)
  REFERENCES `gmair_machine`.`goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`codeValue`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`codeValue` (
  `code_id` VARCHAR(20) NOT NULL,
  `model_id` VARCHAR(20) NOT NULL,
  `batch_value` VARCHAR(20) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `code_url` VARCHAR(200) NOT NULL,
  `code_status` TINYINT(1) NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`code_id`),
  INDEX `fk_qrcode_goods_model1_idx` (`model_id` ASC),
  CONSTRAINT `fk_qrcode_goods_model1`
  FOREIGN KEY (`model_id`)
  REFERENCES `gmair_machine`.`goods_model` (`model_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`idle_machine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`idle_machine` (
  `machine_id` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`machine_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`code_machine_bind`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`code_machine_bind` (
  `bind_id` INT NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `machine_id` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bind_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`consumer_code_bind`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`consumer_code_bind` (
  `bind_id` VARCHAR(20) NOT NULL,
  `consumer_id` VARCHAR(20) NOT NULL,
  `bind_name` VARCHAR(45) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `ownership` TINYINT(1) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bind_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`machine_daily_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`machine_daily_status` (
  `machine_id` VARCHAR(20) NOT NULL,
  `average_pm2_5` DOUBLE NULL,
  `index` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`machine_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`machine_monthly_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`machine_monthly_status` (
  `machine_id` VARCHAR(20) NOT NULL,
  `average_pm2_5` DOUBLE NOT NULL,
  `record_date` DATE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`machine_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`pre_bind`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`pre_bind` (
  `bind_id` VARCHAR(20) NOT NULL,
  `machine_id` VARCHAR(20) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bind_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`machine_setting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`machine_setting` (
  `setting_id` VARCHAR(20) NOT NULL,
  `consumer_id` VARCHAR(20) NOT NULL,
  `setting_name` VARCHAR(45) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`setting_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`volume_setting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`volume_setting` (
  `setting_id` VARCHAR(20) NOT NULL,
  `floor_pm2_5` INT NOT NULL,
  `upper_pm2_5` INT NOT NULL,
  `speed_value` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`setting_id`),
  CONSTRAINT `fk_volume_setting_machine_setting1`
  FOREIGN KEY (`setting_id`)
  REFERENCES `gmair_machine`.`machine_setting` (`setting_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`light_setting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`light_setting` (
  `setting_id` VARCHAR(20) NOT NULL,
  `light_value` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`setting_id`),
  CONSTRAINT `fk_light_setting_machine_setting1`
  FOREIGN KEY (`setting_id`)
  REFERENCES `gmair_machine`.`machine_setting` (`setting_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`power_setting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`power_setting` (
  `setting_id` VARCHAR(20) NOT NULL,
  `power_action` TINYINT(1) NOT NULL,
  `trigger_hour` INT NOT NULL,
  `trigger_minute` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`setting_id`),
  CONSTRAINT `fk_power_setting_machine_setting1`
  FOREIGN KEY (`setting_id`)
  REFERENCES `gmair_machine`.`machine_setting` (`setting_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`machine_hourly_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`machine_hourly_status` (
  `status_id` VARCHAR(20) NOT NULL,
  `average_pm2_5` DOUBLE NOT NULL,
  `index` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`status_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`global_machine_setting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`global_machine_setting` (
  `gms_id` VARCHAR(20) NOT NULL,
  `gms_name` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`gms_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`machine_notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`machine_notification` (
  `notification_id` VARCHAR(20) NOT NULL,
  `notification_title` VARCHAR(45) NOT NULL,
  `notification_content` LONGTEXT NOT NULL,
  `is_read` TINYINT(1) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`notification_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`screen_record`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`screen_record` (
  `record_id` VARCHAR(20) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `refresh_date` DATE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`record_id`))
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
