-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_enterprise
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_enterprise
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_enterprise` DEFAULT CHARACTER SET utf8 ;
USE `gmair_enterprise` ;

-- -----------------------------------------------------
-- Table `gmair_enterprise`.`merchant_profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_enterprise`.`merchant_profile` (
  `merchant_id` VARCHAR(20) NOT NULL,
  `merchant_name` VARCHAR(45) NOT NULL,
  `merchant_code` VARCHAR(45) NOT NULL,
  `merchant_address` VARCHAR(100) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`merchant_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_enterprise`.`merchant_contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_enterprise`.`merchant_contact` (
  `contact_id` VARCHAR(20) NOT NULL,
  `merchant_id` VARCHAR(20) NOT NULL,
  `contact_name` VARCHAR(45) NOT NULL,
  `contact_phone` VARCHAR(45) NOT NULL,
  `preferred` TINYINT(1) NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`contact_id`),
  INDEX `fk_merchant_contact_merchant_profile_idx` (`merchant_id` ASC),
  CONSTRAINT `fk_merchant_contact_merchant_profile`
  FOREIGN KEY (`merchant_id`)
  REFERENCES `gmair_enterprise`.`merchant_profile` (`merchant_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
