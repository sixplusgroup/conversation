-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gmair_drift
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_drift` DEFAULT CHARACTER SET utf8 ;
USE `gmair_drift`;

-- -----------------------------------------------------
-- Table `gmair_drift`.`drift_equipment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_equipment` (
  `equip_id` VARCHAR(45) NOT NULL,
  `equip_name` VARCHAR(45) NOT NULL,
  `equip_price` DOUBLE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`equip_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;

-- -----------------------------------------------------
-- Table `gmair_drift`.`drift_repository`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_repository` (
  `repository_id` VARCHAR(45) NOT NULL,
  `equip_id` VARCHAR(45) NOT NULL,
  `pool_size` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`repository_id`),
  INDEX `fk_drift_repository_drift_equipment_idx` (`equip_id` ASC),
  CONSTRAINT `fk_drift_repository_drift_equipment`
  FOREIGN KEY (`equip_id`)
  REFERENCES `gmair_drift`.`drift_equipment` (`equip_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_drift`.`drift_activity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_activity` (
  `activity_id` VARCHAR(45) NOT NULL,
  `activity_name` VARCHAR(45) NOT NULL,
  `repository_size` INT NOT NULL,
  `threshold` DOUBLE NOT NULL,
  `start_time` DATE NOT NULL,
  `end_time` DATE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`activity_id`)
)ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
