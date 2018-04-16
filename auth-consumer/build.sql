-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_userinfo
-- -----------------------------------------------------
-- this database is used to keep user information

-- -----------------------------------------------------
-- Schema gmair_userinfo
--
-- this database is used to keep user information
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_userinfo` DEFAULT CHARACTER SET utf8 ;
USE `gmair_userinfo` ;

-- -----------------------------------------------------
-- Table `gmair_userinfo`.`consumer_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_userinfo`.`consumer_info` (
  `consumer_id` VARCHAR(20) NOT NULL,
  `consumer_name` VARCHAR(45) NOT NULL,
  `consumer_username` VARCHAR(45) NULL,
  `consumer_wechat` VARCHAR(45) NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`consumer_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_userinfo`.`consumer_phone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_userinfo`.`consumer_phone` (
  `phone_id` VARCHAR(20) NOT NULL,
  `consumer_id` VARCHAR(20) NOT NULL,
  `phone_number` VARCHAR(20) NOT NULL,
  `mobile_preferred` TINYINT(1) NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`phone_id`),
  INDEX `fk_consumer_phone_consumer_info_idx` (`consumer_id` ASC),
  CONSTRAINT `fk_consumer_phone_consumer_info`
  FOREIGN KEY (`consumer_id`)
  REFERENCES `gmair_userinfo`.`consumer_info` (`consumer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_userinfo`.`consumer_addr`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_userinfo`.`consumer_addr` (
  `addr_id` VARCHAR(20) NOT NULL,
  `consumer_id` VARCHAR(20) NOT NULL,
  `address_detail` VARCHAR(100) NULL,
  `address_province` VARCHAR(45) NULL,
  `address_city` VARCHAR(45) NULL,
  `address_district` VARCHAR(45) NULL,
  `addr_preferred` TINYINT(1) NOT NULL DEFAULT 0,
  `block_flag` VARCHAR(45) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`addr_id`),
  INDEX `fk_consumer_addr_consumer_info1_idx` (`consumer_id` ASC),
  CONSTRAINT `fk_consumer_addr_consumer_info1`
  FOREIGN KEY (`consumer_id`)
  REFERENCES `gmair_userinfo`.`consumer_info` (`consumer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

USE `gmair_userinfo` ;

-- -----------------------------------------------------
-- Placeholder table for view `gmair_userinfo`.`view_consumer_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_userinfo`.`view_consumer_info` (`id` INT);

-- -----------------------------------------------------
-- View `gmair_userinfo`.`view_consumer_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gmair_userinfo`.`view_consumer_info`;
USE `gmair_userinfo`;
CREATE  OR REPLACE VIEW `view_consumer_info` AS
  (select ci.consumer_id, ci.consumer_name, ci.consumer_username, ci.consumer_wechat, cp.phone_number, ca.address_detail, ca.address_province, ca.address_city, ca.address_district, ci.block_flag, ci.create_time
   from gmair_userinfo.consumer_info ci
     left join gmair_userinfo.consumer_phone cp on ci.consumer_id = cp.consumer_id
     left join gmair_userinfo.consumer_addr ca on ci.consumer_id = ca.consumer_id
  );

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
