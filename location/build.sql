-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_location
-- -----------------------------------------------------
-- provide province, city, district information

-- -----------------------------------------------------
-- Schema gmair_location
--
-- provide province, city, district information
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_location` DEFAULT CHARACTER SET utf8 ;
USE `gmair_location` ;

-- -----------------------------------------------------
-- Table `gmair_location`.`province_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_location`.`province_list` (
  `province_id` VARCHAR(20) NOT NULL,
  `province_name` VARCHAR(45) NOT NULL,
  `province_pinyin` VARCHAR(45) NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`province_id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_location`.`city_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_location`.`city_list` (
  `city_id` VARCHAR(20) NOT NULL,
  `province_id` VARCHAR(20) NOT NULL,
  `city_name` VARCHAR(45) NOT NULL,
  `city_pinyin` VARCHAR(45) NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`city_id`),
  INDEX `fk_city_list_province_list_idx` (`province_id` ASC),
  CONSTRAINT `fk_city_list_province_list`
  FOREIGN KEY (`province_id`)
  REFERENCES `gmair_location`.`province_list` (`province_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_location`.`district_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_location`.`district_list` (
  `district_id` VARCHAR(20) NOT NULL,
  `city_id` VARCHAR(20) NOT NULL,
  `district_name` VARCHAR(45) NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`district_id`),
  INDEX `fk_district_list_city_list1_idx` (`city_id` ASC),
  CONSTRAINT `fk_district_list_city_list1`
  FOREIGN KEY (`city_id`)
  REFERENCES `gmair_location`.`city_list` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
