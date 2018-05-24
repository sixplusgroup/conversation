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
-- Table `gmair_machine`.`qrcode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`qrcode` (
  `code_id` VARCHAR(20) NOT NULL,
  `model_id` VARCHAR(20) NOT NULL,
  `batch_value` VARCHAR(20) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `code_url` VARCHAR(200) NOT NULL,
  `code_occupied` TINYINT(1) NOT NULL DEFAULT 0,
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
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`machine_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`machine_monthly_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`machine_monthly_status` (
  `machine_id` VARCHAR(20) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`machine_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_machine`.`machine_yearly_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_machine`.`machine_yearly_status` (
  `machine_id` INT NOT NULL,
  `belong_year` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
