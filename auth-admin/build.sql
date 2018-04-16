-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_admininfo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_admininfo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_admininfo` DEFAULT CHARACTER SET utf8 ;
USE `gmair_admininfo` ;

-- -----------------------------------------------------
-- Table `gmair_admininfo`.`admin_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_admininfo`.`admin_info` (
  `admin_id` VARCHAR(20) NOT NULL,
  `admin_email` VARCHAR(50) NOT NULL,
  `admin_name` VARCHAR(45) NOT NULL,
  `admin_password` VARCHAR(200) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`admin_id`))
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
