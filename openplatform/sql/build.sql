-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema open_platform
-- -----------------------------------------------------
-- This database keeps data about information about third-party. They can obtain the machine information from gmair platform.

-- -----------------------------------------------------
-- Schema open_platform
--
-- This database keeps data about information about third-party. They can obtain the machine information from gmair platform.
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `open_platform`
  DEFAULT CHARACTER SET utf8;
USE `open_platform`;

-- -----------------------------------------------------
-- Table `open_platform`.`corp_profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `open_platform`.`corp_profile` (
  `corp_id`     VARCHAR(20) NOT NULL,
  `corp_name`   VARCHAR(45) NOT NULL,
  `corp_email`  VARCHAR(45) NOT NULL,
  `corp_app_id` VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`corp_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `open_platform`.`corp_machine_subscribe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `open_platform`.`corp_machine_subscribe` (
  `subs_id`     VARCHAR(20) NOT NULL,
  `corp_id`     VARCHAR(20) NOT NULL,
  `code_value`  VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`subs_id`),
  INDEX `fk_corp_machine_subscribe_corp_profile_idx` (`corp_id` ASC),
  CONSTRAINT `fk_corp_machine_subscribe_corp_profile`
  FOREIGN KEY (`corp_id`)
  REFERENCES `open_platform`.`corp_profile` (`corp_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
