-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_order
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_order
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_order` DEFAULT CHARACTER SET utf8 ;
USE `gmair_order` ;

-- -----------------------------------------------------
-- Table `gmair_order`.`platform_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`platform_order` (
  `order_id` VARCHAR(20) NOT NULL,
  `order_no` VARCHAR(50) NULL,
  `consignee` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `province` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `district` VARCHAR(45) NULL,
  `total_price` DOUBLE NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`order_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`order_item` (
  `item_id` VARCHAR(20) NOT NULL,
  `order_id` VARCHAR(20) NOT NULL,
  `item_name` VARCHAR(50) NOT NULL,
  `quantity` DOUBLE NOT NULL DEFAULT 1,
  `item_price` DOUBLE NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`item_id`),
  INDEX `fk_order_item_platform_order1_idx` (`order_id` ASC),
  CONSTRAINT `fk_order_item_platform_order1`
  FOREIGN KEY (`order_id`)
  REFERENCES `gmair_order`.`platform_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
