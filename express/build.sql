-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_express
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_express
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_express`
  DEFAULT CHARACTER SET utf8;
USE `gmair_express`;

-- -----------------------------------------------------
-- Table `gmair_express`.`express_company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_express`.`express_company` (
  `company_id`   VARCHAR(20) NOT NULL,
  `company_name` VARCHAR(45) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`company_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_express`.`order_express`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_express`.`order_express` (
  `express_id`     VARCHAR(20) NOT NULL,
  `order_id`       VARCHAR(20) NOT NULL,
  `company_id`     VARCHAR(20) NULL,
  `express_no`     VARCHAR(45) NOT NULL,
  `express_status` TINYINT(1)  NULL,
  `block_flag`     TINYINT(1)  NULL,
  `create_time`    DATETIME    NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_order_express_express_company_idx` (`company_id` ASC),
  CONSTRAINT `fk_order_express_express_company`
  FOREIGN KEY (`company_id`)
  REFERENCES `gmair_express`.`express_company` (`company_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_express`.`parcel_express`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_express`.`parcel_express` (
  `express_id`     VARCHAR(20) NOT NULL,
  `parent_express` VARCHAR(20) NOT NULL,
  `code_value`     VARCHAR(45) NOT NULL,
  `express_status` TINYINT(1)  NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`express_id`),
  INDEX `fk_parcel_express_order_express1_idx` (`parent_express` ASC),
  CONSTRAINT `fk_parcel_express_order_express1`
  FOREIGN KEY (`parent_express`)
  REFERENCES `gmair_express`.`order_express` (`express_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

