-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_formaldehyde_detection
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_formaldehyde_detection
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_formaldehyde_detection`
  DEFAULT CHARACTER SET utf8;
USE `gmair_formaldehyde_detection`;

-- -----------------------------------------------------
-- Table `gmair_formaldehyde_detection`.`check_equipment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_formaldehyde_detection`.`check_equipment` (
  `equipment_id`   VARCHAR(20) NOT NULL,
  `equipment_name` VARCHAR(45) NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`equipment_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_formaldehyde_detection`.`case_profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_formaldehyde_detection`.`case_profile` (
  `case_id`        VARCHAR(20) NOT NULL,
  `case_holder`    VARCHAR(45) NOT NULL,
  `equipment_id`   VARCHAR(45) NOT NULL,
  `check_duration` VARCHAR(45) NOT NULL,
  `check_date`     DATE        NOT NULL,
  `case_city`      VARCHAR(45) NOT NULL,
  `case_location`  VARCHAR(45) NOT NULL,
  `check_trace`    LONGTEXT    NOT NULL,
  `case_status`    TINYINT(1)  NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`case_id`),
  INDEX `fk_case_profile_check_equipment1_idx` (`equipment_id` ASC),
  CONSTRAINT `fk_case_profile_check_equipment1`
  FOREIGN KEY (`equipment_id`)
  REFERENCES `gmair_formaldehyde_detection`.`check_equipment` (`equipment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_formaldehyde_detection`.`case_lng_lat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_formaldehyde_detection`.`case_lng_lat` (
  `record_id`   VARCHAR(20) NOT NULL,
  `case_id`     VARCHAR(20) NOT NULL,
  `longitude`   DOUBLE      NOT NULL,
  `latitude`    DOUBLE      NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`record_id`),
  INDEX `fk_case_lng_lat_case_profile1_idx` (`case_id` ASC),
  CONSTRAINT `fk_case_lng_lat_case_profile1`
  FOREIGN KEY (`case_id`)
  REFERENCES `gmair_formaldehyde_detection`.`case_profile` (`case_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
