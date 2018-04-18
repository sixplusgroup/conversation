-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_install
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_install
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_install`
  DEFAULT CHARACTER SET utf8;
USE `gmair_install`;

-- -----------------------------------------------------
-- Table `gmair_install`.`install_team`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_install`.`install_team` (
  `team_id`          VARCHAR(20)  NOT NULL,
  `team_name`        VARCHAR(45)  NOT NULL,
  `team_area`        VARCHAR(45)  NOT NULL,
  `team_description` VARCHAR(200) NULL,
  `block_flag`       TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time`      DATETIME     NOT NULL,
  PRIMARY KEY (`team_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_install`.`install_assign`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_install`.`install_assign` (
  `assign_id`   VARCHAR(20) NOT NULL,
  `code_value`  VARCHAR(45) NOT NULL,
  `team_id`     VARCHAR(20) NOT NULL,
  `member_id`   VARCHAR(20) NULL,
  `assign_date` DATETIME    NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`assign_id`),
  INDEX `fk_install_assign_install_team1_idx` (`team_id` ASC),
  CONSTRAINT `fk_install_assign_install_team1`
  FOREIGN KEY (`team_id`)
  REFERENCES `gmair_install`.`install_team` (`team_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_install`.`team_member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_install`.`team_member` (
  `member_id`    VARCHAR(20) NOT NULL,
  `team_id`      VARCHAR(20) NOT NULL,
  `member_phone` VARCHAR(20) NOT NULL,
  `member_name`  VARCHAR(45) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`member_id`),
  INDEX `fk_team_member_install_team_idx` (`team_id` ASC),
  CONSTRAINT `fk_team_member_install_team`
  FOREIGN KEY (`team_id`)
  REFERENCES `gmair_install`.`install_team` (`team_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_install`.`install_feedback`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_install`.`install_feedback` (
  `feedback_id`      VARCHAR(20)  NOT NULL,
  `code_value`       VARCHAR(20)  NOT NULL,
  `member_phone`     VARCHAR(45)  NOT NULL,
  `feedback_content` VARCHAR(100) NOT NULL,
  `block_flag`       TINYINT(1)   NOT NULL,
  `create_time`      DATETIME     NOT NULL,
  PRIMARY KEY (`feedback_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_install`.`install_snapshot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_install`.`install_snapshot` (
  `snapshot_id`     VARCHAR(20)  NOT NULL,
  `assign_id`       VARCHAR(20)  NULL,
  `code_value`      VARCHAR(45)  NULL,
  `wechat_id`       VARCHAR(50)  NOT NULL,
  `member_phone`    VARCHAR(45)  NOT NULL,
  `check_list`      VARCHAR(200) NOT NULL,
  `indoor_hole`     VARCHAR(200) NOT NULL,
  `outdoor_hole`    VARCHAR(200) NOT NULL,
  `indoor_pre_air`  VARCHAR(200) NOT NULL,
  `indoor_post_air` VARCHAR(200) NOT NULL,
  `hole_direction`  VARCHAR(200) NOT NULL,
  `block_flag`      TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time`     DATETIME     NOT NULL,
  PRIMARY KEY (`snapshot_id`),
  INDEX `fk_install_snapshot_install_assign1_idx` (`assign_id` ASC),
  CONSTRAINT `fk_install_snapshot_install_assign1`
  FOREIGN KEY (`assign_id`)
  REFERENCES `gmair_install`.`install_assign` (`assign_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
