-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_assemble
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_assemble
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_assemble`
  DEFAULT CHARACTER SET utf8;
USE `gmair_assemble`;

-- -----------------------------------------------------
-- Table `gmair_assemble`.`barcode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_assemble`.`barcode` (
  `code_id`     VARCHAR(20) NOT NULL,
  `code_value`  VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`code_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_assemble`.`snapshot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_assemble`.`snapshot` (
  `snapshot_id`   VARCHAR(20)  NOT NULL,
  `code_value`    VARCHAR(45)  NOT NULL,
  `snapshot_path` VARCHAR(100) NOT NULL,
  `block_flag`    TINYINT(1)   NOT NULL,
  `create_time`   DATETIME     NOT NULL,
  PRIMARY KEY (`snapshot_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_assemble`.`check_record`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_assemble`.`check_record` (
  `record_id`     VARCHAR(20) NOT NULL,
  `snapshot_id`   VARCHAR(20) NOT NULL,
  `record_status` TINYINT(1)  NOT NULL,
  `block_flag`    TINYINT(1)  NOT NULL,
  `create_time`   DATETIME    NOT NULL,
  PRIMARY KEY (`record_id`),
  INDEX `fk_check_record_snapshot_idx` (`snapshot_id` ASC),
  CONSTRAINT `fk_check_record_snapshot`
  FOREIGN KEY (`snapshot_id`)
  REFERENCES `gmair_assemble`.`snapshot` (`snapshot_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
