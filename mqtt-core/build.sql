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
-- Table `gmair_mqtt`.`mqtt_topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_mqtt`.`mqtt_topic` (
  `topic_id` VARCHAR(20) NOT NULL,
  `topic_detail` VARCHAR(20) NOT NULL,
  `topic_description` VARCHAR(50) NOT NULL,
  `block_flag`   TINYINT(1)   NOT NULL,
  `create_time`  DATETIME     NOT NULL,
  PRIMARY KEY (`topic_id`)
) ENGINE = InnoDB;