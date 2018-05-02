-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_resource
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_resource
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_resource` DEFAULT CHARACTER SET utf8 ;
USE `gmair_resource` ;

-- -----------------------------------------------------
-- Table `gmair_resource`.`file_location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_resource`.`file_location` (
  `file_id` VARCHAR(20) NOT NULL,
  `file_url` VARCHAR(200) NOT NULL,
  `actual_path` VARCHAR(100) NOT NULL,
  `filename` VARCHAR(50) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`file_id`))
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
