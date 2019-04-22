#2019-03-27
CREATE TABLE `gmair_install`.`dispatch_record` (
  `record_id`   VARCHAR(20) NOT NULL,
  `assign_id`   VARCHAR(20) NOT NULL,
  `team_id`     VARCHAR(20) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`record_id`)
);

#2019-04-01
CREATE TABLE `gmair_install`.`user_session` (
  `open_id`     VARCHAR(50) NOT NULL,
  `session_key` VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`open_id`)
);

#2019-04-03
CREATE TABLE `gmair_install`.`team_watch` (
  `watch_id`    VARCHAR(20) NOT NULL,
  `member_id`   VARCHAR(20) NOT NULL,
  `team_id`     VARCHAR(20) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`watch_id`)
);

#2019-04-14
CREATE TABLE `gmair_install`.`assign_action` (
  `action_id`      VARCHAR(20) NOT NULL,
  `assign_id`      VARCHAR(20) NOT NULL,
  `action_message` VARCHAR(50) NOT NULL,
  `block_flag`     TINYINT(1)  NOT NULL,
  `create_time`    DATETIME    NOT NULL,
  PRIMARY KEY (`action_id`)
);

ALTER SCHEMA `gmair_install`
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

#2019-04-19
CREATE TABLE `gmair_install`.`assign_snapshot` (
  `snapshot_id`     VARCHAR(20) NOT NULL,
  `assign_id`       VARCHAR(20) NOT NULL,
  `code_value`      VARCHAR(45) NOT NULL,
  `picture_path`    LONGTEXT    NOT NULL,
  `wifi_configured` TINYINT(1)  NOT NULL DEFAULT 0,
  `install_method`  VARCHAR(45) NOT NULL,
  `description`     VARCHAR(45) NULL,
  `block_flag`      TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`snapshot_id`)
);

#2019-04-22
CREATE TABLE `gmair_install`.`assin_feedback` (
  `feedback_id`     VARCHAR(20) NOT NULL,
  `consumer_name`   VARCHAR(45) NOT NULL,
  `consumer_phone`  VARCHAR(45) NOT NULL,
  `assign_rank`     INT         NOT NULL,
  `feedback_detail` LONGTEXT    NOT NULL,
  `block_flag`      TINYINT(1)  NOT NULL,
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`feedback_id`)
);

ALTER TABLE `gmair_install`.`install_assign`
  ADD COLUMN `assign_description` VARCHAR(45) NULL
  AFTER `assign_date`;


