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

#2019-5-17
CREATE VIEW `gmair_install`.`assign_member_view` AS
    SELECT
        `install_assign`.`assign_id` AS `assign_id`,
        `install_assign`.`code_value` AS `code_value`,
        `install_assign`.`assign_detail` AS `assign_detail`,
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
        ((`gmair_install`.`install_assign`.`block_flag` = FALSE)
            AND (`gmair_install`.`install_assign`.`team_id` = `gmair_install`.`install_team`.`team_id`)
            AND (`gmair_install`.`install_assign`.`member_id` = `gmair_install`.`team_member`.`member_id`));

#2019-5-27
CREATE VIEW `gmair_install`.`assign_report_view` AS
    SELECT
        `install_assign`.`assign_id` AS `assign_id`,
        `install_assign`.`assign_detail` AS `assign_detail`,
        `install_assign`.`assign_status` AS `assign_status`,
        `install_assign`.`assign_date` AS `assign_date`,
        `install_assign`.`block_flag` AS `block_flag`,
        `install_assign`.`consumer_consignee` AS `consumer_consignee`,
        `install_assign`.`consumer_phone` AS `consumer_phone`,
        `install_assign`.`consumer_address` AS `consumer_address`,
        `install_assign`.`assign_source` AS `assign_source`,
        `install_team`.`team_id` AS `team_id`,
        `install_team`.`team_name` AS `team_name`,
        `team_member`.`member_id` AS `member_id`,
        `team_member`.`member_name` AS `member_name`,
        `assign_snapshot`.`code_value` AS `code_value`,
        `assign_snapshot`.`create_time` AS `create_time`
    FROM
        (((`gmair_install`.`install_assign`
        JOIN `gmair_install`.`assign_snapshot`)
        JOIN `gmair_install`.`install_team`)
        JOIN `gmair_install`.`team_member`)
    WHERE
        ((`gmair_install`.`install_assign`.`block_flag` = FALSE)
            AND (`gmair_install`.`assign_snapshot`.`block_flag` = FALSE )
            AND (`gmair_install`.`install_assign`.`assign_status` = 3 )
            AND (`gmair_install`.`install_assign`.`team_id` = `gmair_install`.`install_team`.`team_id`)
            AND (`gmair_install`.`assign_snapshot`.`assign_id` = `gmair_install`.`install_assign`.`assign_id`)
            AND (`gmair_install`.`install_assign`.`member_id` = `gmair_install`.`team_member`.`member_id`))
            ORDER BY `gmair_install`.`install_assign`.`team_id`,`gmair_install`.`install_assign`.`member_id`,`gmair_install`.`assign_snapshot`.`create_time`;


#2019-09-23
CREATE TABLE `gmair_install`.`picture_md5` (
  `image_id` VARCHAR(45) NOT NULL,
  `picture_path` LONGTEXT NOT NULL,
  `md5` VARCHAR(100) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`image_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

#2019-11-14
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`%`
    SQL SECURITY DEFINER
VIEW `gmair_install`.`assign_member_view` AS
    SELECT
        `gmair_install`.`install_assign`.`assign_id` AS `assign_id`,
        `gmair_install`.`install_assign`.`code_value` AS `code_value`,
        `gmair_install`.`install_assign`.`assign_detail` AS `assign_detail`,
        `gmair_install`.`install_team`.`team_id` AS `team_id`,
        `gmair_install`.`install_team`.`team_name` AS `team_name`,
        `gmair_install`.`team_member`.`member_id` AS `member_id`,
        `gmair_install`.`team_member`.`member_name` AS `member_name`,
        `gmair_install`.`install_assign`.`assign_status` AS `assign_status`,
        `gmair_install`.`install_assign`.`assign_date` AS `assign_date`,
        `gmair_install`.`install_assign`.`block_flag` AS `block_flag`,
        `gmair_install`.`install_assign`.`create_time` AS `create_time`,
        `gmair_install`.`install_assign`.`consumer_consignee` AS `consumer_consignee`,
        `gmair_install`.`install_assign`.`consumer_phone` AS `consumer_phone`,
        `gmair_install`.`install_assign`.`consumer_address` AS `consumer_address`,
        `gmair_install`.`install_assign`.`assign_source` AS `assign_source`
    FROM
        ((`gmair_install`.`install_assign`
        LEFT JOIN `gmair_install`.`install_team` ON ((`gmair_install`.`install_assign`.`team_id` = `gmair_install`.`install_team`.`team_id`)))
        LEFT JOIN `gmair_install`.`team_member` ON ((`gmair_install`.`install_assign`.`member_id` = `gmair_install`.`team_member`.`member_id`)))
    WHERE
        (`gmair_install`.`install_assign`.`block_flag` = 0)

#2019-01-03
CREATE TABLE `gmair_install`.`company` (
  `company_id` VARCHAR(45) NOT NULL,
  `company_name` VARCHAR(45) NOT NULL,
  `message_title` VARCHAR(45) NOT NULL,
  `company_detail` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`company_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

#2019-01-04
ALTER TABLE `gmair_install`.`install_assign`
ADD COLUMN `company_id` VARCHAR(45) NULL AFTER `member_id`;

#2020-05-13
DROP TABLE IF EXISTS `fix_snapshot`;
CREATE TABLE `fix_snapshot` (
    `snapshot_id` varchar(20) NOT NULL,
    `assign_id` varchar(20) NOT NULL,
    `code_value` varchar(45) NOT NULL,
    `picture_path` longtext NOT NULL,
    `description` varchar(45) DEFAULT NULL,
    `block_flag` tinyint(1) NOT NULL DEFAULT '0',
    `create_time` datetime NOT NULL,
    PRIMARY KEY (`snapshot_id`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `survey_snapshot`;
CREATE TABLE `survey_snapshot` (
   `snapshot_id` varchar(20) NOT NULL,
   `assign_id` varchar(20) NOT NULL,
   `picture_path` longtext NOT NULL,
   `description` varchar(45) DEFAULT NULL,
   `block_flag` tinyint(1) NOT NULL DEFAULT '0',
   `create_time` datetime NOT NULL,
   PRIMARY KEY (`snapshot_id`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `change_machine_snapshot`;
CREATE TABLE `change_machine_snapshot` (
   `snapshot_id` varchar(20) NOT NULL,
   `assign_id` varchar(20) NOT NULL,
   `old_code_value` varchar(45) NOT NULL,
   `new_code_value` varchar(45) NOT NULL,
   `picture_path` longtext NOT NULL,
   `wifi_configured` tinyint(1) NOT NULL DEFAULT '0',
   `description` varchar(45) DEFAULT NULL,
   `waybill_number` varchar(100) NOT NULL,
   `block_flag` tinyint(1) NOT NULL DEFAULT '0',
   `create_time` datetime NOT NULL,
   PRIMARY KEY (`snapshot_id`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `disassemble_snapshot`;
CREATE TABLE `disassemble_snapshot` (
    `snapshot_id` varchar(20) NOT NULL,
    `assign_id` varchar(20) NOT NULL,
    `code_value` varchar(45) NOT NULL,
    `picture_path` longtext NOT NULL,
    `description` varchar(45) DEFAULT NULL,
    `waybill_number` varchar(100) NULL,
    `block_flag` tinyint(1) NOT NULL DEFAULT '0',
    `create_time` datetime NOT NULL,
    PRIMARY KEY (`snapshot_id`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS = 1;










