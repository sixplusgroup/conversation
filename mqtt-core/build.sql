-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_mqtt
-- -----------------------------------------------------

CREATE SCHEMA IF NOT EXISTS `gmair_mqtt`
  DEFAULT CHARACTER SET utf8;
USE `gmair_mqtt`;

-- -----------------------------------------------------
-- Table `gmair_mqtt`.`api_topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_mqtt`.`api_topic` (
  `bound_id` VARCHAR(20) NOT NULL,
  `api_name` VARCHAR(20) NOT NULL,
  `api_url` VARCHAR(50) NOT NULL,
  `api_topic` VARCHAR(20) NOT NULL,
  `api_description` VARCHAR(50) NOT NULL,
  `block_flag`   TINYINT(1)   NOT NULL,
  `create_time`  DATETIME     NOT NULL,
  PRIMARY KEY (`bound_id`)
) ENGINE = InnoDB;