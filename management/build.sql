-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_management
-- -----------------------------------------------------
-- Management background platform

-- -----------------------------------------------------
-- Schema gmair_management
--
-- Management background platform
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_management` DEFAULT CHARACTER SET utf8 ;
USE `gmair_management` ;

-- -----------------------------------------------------
-- Table `gmair_management`.`message_template`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_management`.`message_template` (
  `template_id` VARCHAR(20) NOT NULL,
  `template_catalog` TINYINT(1) NOT NULL,
  `content` LONGTEXT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`template_id`))
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
