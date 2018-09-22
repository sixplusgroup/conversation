-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_drift` DEFAULT CHARACTER SET utf8 ;
USE `gmair_drift` ;

-- -----------------------------------------------------
-- Table `mydb`.`drift_goods`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_goods` (
  `goods_id` VARCHAR(45) NOT NULL,
  `goods_name` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`goods_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`drift_repository`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_repository` (
  `repository_id` VARCHAR(45) NOT NULL,
  `goods_id` VARCHAR(45) NOT NULL,
  `pool_size` INT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`repository_id`),
  INDEX `fk_drift_repository_drift_goods_idx` (`goods_id` ASC) VISIBLE,
  CONSTRAINT `fk_drift_repository_drift_goods`
    FOREIGN KEY (`goods_id`)
    REFERENCES `gmair_drift`.`drift_goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`drift_activity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_activity` (
  `activity_id` VARCHAR(45) NOT NULL,
  `goods_id` VARCHAR(45) NOT NULL,
  `activity_name` VARCHAR(45) NOT NULL,
  `repository_size` INT NOT NULL,
  `threshold` DOUBLE NOT NULL,
  `start_time` DATE NOT NULL,
  `end_time` DATE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`activity_id`),
  INDEX `fk_drift_activity_drift_goods1_idx` (`goods_id` ASC) VISIBLE,
  CONSTRAINT `fk_drift_activity_drift_goods1`
    FOREIGN KEY (`goods_id`)
    REFERENCES `gmair_drift`.`drift_goods` (`goods_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`drift_reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_reservation` (
  `reservation_id` VARCHAR(45) NOT NULL,
  `consumer_id` VARCHAR(45) NOT NULL,
  `activity_id` VARCHAR(45) NOT NULL,
  `expected_date` DATE NOT NULL,
  `interval` INT NOT NULL,
  `consignee_name` VARCHAR(45) NOT NULL,
  `consignee_phone` VARCHAR(45) NOT NULL,
  `consighee_address` VARCHAR(45) NOT NULL,
  `province_id` VARCHAR(45) NOT NULL,
  `city_id` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`reservation_id`),
  INDEX `fk_drift_reservation_drift_activity1_idx` (`activity_id` ASC) VISIBLE,
  CONSTRAINT `fk_drift_reservation_drift_activity1`
    FOREIGN KEY (`activity_id`)
    REFERENCES `gmair_drift`.`drift_activity` (`activity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
