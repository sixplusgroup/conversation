-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_bill
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_bill
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_bill`
  DEFAULT CHARACTER SET utf8;
USE `gmair_bill`;

-- -----------------------------------------------------
-- Table `gmair_bill`.`bill_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_bill`.`bill_info` (
  `bill_id`      VARCHAR(20) NOT NULL,
  `order_id`     VARCHAR(20) NOT NULL,
  `order_price`  DOUBLE      NOT NULL,
  `actual_price` DOUBLE      NOT NULL,
  `bill_status`  TINYINT(1)  NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`bill_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_bill`.`payment_channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_bill`.`payment_channel` (
  `channel_id`   VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`channel_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_bill`.`deal_snapshot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_bill`.`deal_snapshot` (
  `snapshot_id`     VARCHAR(20) NOT NULL,
  `bill_id`         VARCHAR(20) NOT NULL,
  `channel_id`      VARCHAR(20) NOT NULL,
  `channel_name`    VARCHAR(45) NOT NULL,
  `deal_account`    VARCHAR(45) NOT NULL,
  `snapshot_status` TINYINT(1)  NOT NULL,
  `block_flag`      TINYINT(1)  NOT NULL,
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`snapshot_id`),
  INDEX `fk_deal_snapshot_payment_channel_idx` (`channel_id` ASC),
  INDEX `fk_deal_snapshot_bill_info1_idx` (`bill_id` ASC),
  CONSTRAINT `fk_deal_snapshot_payment_channel`
  FOREIGN KEY (`channel_id`)
  REFERENCES `gmair_bill`.`payment_channel` (`channel_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_deal_snapshot_bill_info1`
  FOREIGN KEY (`bill_id`)
  REFERENCES `gmair_bill`.`bill_info` (`bill_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
