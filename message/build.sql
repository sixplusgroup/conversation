-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_message
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_message
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_message` DEFAULT CHARACTER SET utf8 ;
USE `gmair_message` ;

-- -----------------------------------------------------
-- Table `gmair_message`.`text_message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_message`.`text_message` (
  `message_id` VARCHAR(20) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `text` VARCHAR(150) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`message_id`))
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
