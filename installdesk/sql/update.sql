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

# 2019
ALTER TABLE `gmair_install`.`install_assign`
  ADD COLUMN `assign_source` VARCHAR(45) NULL
  AFTER `assign_date`;

#2019-5-11
CREATE VIEW `gmair_install`.`assign_member_view` AS
    SELECT
        `install_assign`.`assign_id` AS `assign_id`,
        `install_assign`.`code_value` AS `code_value`,
        `install_team`.`team_id` AS `team_id`,
        `install_team`.`team_name` AS `team_name`,
        `team_member`.`member_id` AS `member_id`,
        `team_member`.`member_name` AS `member_name`,
        `install_assign`.`assign_status` AS `assign_status`,
        `install_assign`.`assign_date` AS `assign_date`,
        `install_assign`.`block_flag` AS `block_flag`,
        `install_assign`.`create_time` AS `create_time`,
        `install_assign`.`consumer_consignee` AS `consumer_consignee`,
        `install_assign`.`consumer_phone` AS `consumer_phone`,
        `install_assign`.`consumer_address` AS `consumer_address`
    FROM
        ((`gmair_install`.`install_assign`
        JOIN `gmair_install`.`install_team`)
        JOIN `gmair_install`.`team_member`)
    WHERE
        ((`gmair_install`.`install_assign`.`team_id` = `gmair_install`.`install_team`.`team_id`)
            AND (`gmair_install`.`install_assign`.`member_id` = `gmair_install`.`team_member`.`member_id`));




