-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_monitor
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_monitor
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_monitor` DEFAULT CHARACTER SET utf8 ;
USE `gmair_monitor` ;

-- -----------------------------------------------------
-- Table `gmair_monitor`.`monitor_logo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_monitor`.`monitor_logo` (
  `logo_id` VARCHAR(20) NOT NULL,
  `qrcode` VARCHAR(45) NOT NULL,
  `path` VARCHAR(100) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`logo_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_monitor`.`monitor_ads`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_monitor`.`monitor_ads` (
  `ads_id` VARCHAR(20) NOT NULL,
  `qrcode` VARCHAR(45) NOT NULL,
  `content` VARCHAR(100) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`ads_id`))
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
