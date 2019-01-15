## create machine power setting view
CREATE VIEW `gmair_machine`.`power_setting_view`
  AS
    SELECT
      power_setting.setting_id     AS setting_id,
      machine_setting.consumer_id  AS consumer_id,
      machine_setting.setting_name AS setting_name,
      machine_setting.code_value   AS code_value,
      power_setting.power_action   AS power_action,
      power_setting.trigger_hour   AS trigger_hour,
      power_setting.trigger_minute AS trigger_minute
    FROM
      gmair_machine.power_setting, gmair_machine.machine_setting
    WHERE power_setting.setting_id = machine_setting.setting_id
          AND power_setting.block_flag = 0
          AND machine_setting.block_flag = 0;

# 2018-06-12 modify machine_monthly_status
ALTER TABLE `gmair_machine`.`machine_monthly_status`
  CHANGE COLUMN `record_date` `index` INT NOT NULL;

ALTER TABLE `gmair_machine`.`machine_daily_status`
  ADD COLUMN `status_id` VARCHAR(45) NOT NULL
  AFTER `create_time`,
  DROP PRIMARY KEY,
  ADD PRIMARY KEY (`status_id`);
ALTER TABLE `gmair_machine`.`machine_daily_status`
  CHANGE COLUMN `status_id` `status_id` VARCHAR(45) NOT NULL
  FIRST;

ALTER TABLE `gmair_machine`.`machine_hourly_status`
  ADD COLUMN `machine_id` VARCHAR(45) NOT NULL
  AFTER `status_id`;

ALTER TABLE `gmair_machine`.`machine_monthly_status`
  ADD COLUMN `status_id` VARCHAR(45) NOT NULL
  FIRST,
  DROP PRIMARY KEY,
  ADD PRIMARY KEY (`status_id`);

## 2018-06-14 create machine volume setting view
CREATE VIEW `gmair_machine`.`volume_setting_view`
  AS
    SELECT
      volume_setting.setting_id    AS setting_id,
      machine_setting.consumer_id  AS consumer_id,
      machine_setting.setting_name AS setting_name,
      machine_setting.code_value   AS code_value,
      volume_setting.floor_pm2_5   AS floor_pm2_5,
      volume_setting.upper_pm2_5   AS upper_pm2_5,
      volume_setting.speed_value   AS speed_value
    FROM
      gmair_machine.volume_setting, gmair_machine.machine_setting
    WHERE volume_setting.setting_id = machine_setting.setting_id
          AND volume_setting.block_flag = 0
          AND machine_setting.block_flag = 0;

## 2018-06-15 create machine light setting view
CREATE VIEW `gmair_machine`.`light_setting_view`
  AS
    SELECT
      light_setting.setting_id     AS setting_id,
      machine_setting.consumer_id  AS consumer_id,
      machine_setting.setting_name AS setting_name,
      machine_setting.code_value   AS code_value,
      light_setting.light_value    AS light_value
    FROM
      gmair_machine.light_setting, gmair_machine.machine_setting
    WHERE light_setting.setting_id = machine_setting.setting_id
          AND light_setting.block_flag = 0
          AND machine_setting.block_flag = 0;

#2018-06-19 add table board_version
CREATE TABLE `gmair_machine`.`board_version` (
  `machine_id`    VARCHAR(45) NOT NULL,
  `board_version` INT         NOT NULL DEFAULT 0,
  `block_flag`    TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`   DATETIME    NOT NULL,
  PRIMARY KEY (`machine_id`)
);

#2018-06-21 add control table
CREATE TABLE `gmair_machine`.`control_option` (
  `option_id`        VARCHAR(20) NOT NULL,
  `option_name`      VARCHAR(45) NOT NULL,
  `option_component` VARCHAR(45) NOT NULL,
  `block_flag`       TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`      DATETIME    NOT NULL,
  PRIMARY KEY (`option_id`)
);

CREATE TABLE `gmair_machine`.`control_option_action` (
  `value_id`        VARCHAR(20) NOT NULL,
  `control_id`      VARCHAR(20) NOT NULL,
  `action_name`     VARCHAR(45) NOT NULL,
  `action_operator` VARCHAR(45) NOT NULL,
  `block_flag`      TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`value_id`)
);

ALTER TABLE `gmair_machine`.`control_option_action`
  ADD COLUMN `model_id` VARCHAR(20) NOT NULL
  AFTER `control_id`;

#2018-06-25
ALTER TABLE `gmair_machine`.`pre_bind`
  ADD COLUMN `board_version` INT NOT NULL DEFAULT 0 AFTER `code_value`;

ALTER TABLE `gmair_machine`.`pre_bind`
DROP COLUMN `board_version`;

#2018-06-28
ALTER TABLE `gmair_machine`.`control_option_action`
ADD COLUMN `command_value` INT NOT NULL DEFAULT 0 AFTER `action_operator`;


#2018-07-11 add table machine_default_location
CREATE TABLE `gmair_machine`.`machine_default_location` (
  `location_id` INT NOT NULL,
  `city_id` VARCHAR(20) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `block_flag` VARCHAR(45) NOT NULL,
  `create_time` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`location_id`));

ALTER TABLE `gmair_machine`.`machine_default_location`
CHANGE COLUMN `location_id` `location_id` VARCHAR(20) NOT NULL ;

ALTER TABLE `gmair_machine`.`machine_default_location`
CHANGE COLUMN `block_flag` `block_flag` TINYINT(1) NOT NULL ,
CHANGE COLUMN `create_time` `create_time` DATETIME NOT NULL ;

#2018-07-25
ALTER TABLE `gmair_machine`.`code_machine_bind`
CHANGE COLUMN `bind_id` `bind_id` VARCHAR(20) NOT NULL ;


#2018-07-30 add table pm2_5_boundary
CREATE TABLE `gmair_machine`.`pm2_5_boundary` (
  `boundary_id` VARCHAR(20) NOT NULL,
  `model_id` VARCHAR(20) NULL,
  `pm2_5` DOUBLE NULL,
  `create_time` DATETIME NULL,
  `block_flag` TINYINT(1) NULL,
  PRIMARY KEY (`boundary_id`));

ALTER TABLE `gmair_machine`.`pm2_5_boundary`
CHANGE COLUMN `model_id` `model_id` VARCHAR(20) NOT NULL ,
CHANGE COLUMN `pm2_5` `pm2_5` DOUBLE NOT NULL ,
CHANGE COLUMN `create_time` `create_time` DATETIME NOT NULL ,
CHANGE COLUMN `block_flag` `block_flag` TINYINT(1) NOT NULL ;

#2018-07-31 add table pm2_5_latest
CREATE TABLE `gmair_machine`.`pm2_5_latest` (
  `latest_id` VARCHAR(20) NOT NULL,
  `machine_id` VARCHAR(20) NOT NULL,
  `pm2_5` DOUBLE NOT NULL,
  `create_time` DATETIME NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  PRIMARY KEY (`latest_id`));

#2018-08-01
ALTER TABLE `gmair_machine`.`pm2_5_latest`
CHANGE COLUMN `pm2_5` `pm2_5` INT NOT NULL ;

ALTER TABLE `gmair_machine`.`pm2_5_boundary`
CHANGE COLUMN `pm2_5` `pm2_5_info` INT NOT NULL ,
ADD COLUMN `pm2_5_warning` INT NOT NULL AFTER `pm2_5_info`;

#2018-08-03 add table
CREATE TABLE `gmair_machine`.`model_light_config` (
  `config_id` VARCHAR(20) NOT NULL,
  `model_id` VARCHAR(20) NOT NULL,
  `min_light` INT(11) NOT NULL,
  `max_light` INT(11) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`config_id`));


#2018-10-23 add table
CREATE TABLE `out_pm2_5_daily` (
  `record_id` varchar(20) NOT NULL,
  `machine_id` varchar(20) NOT NULL,
  `average_pm25` double NOT NULL DEFAULT '0',
  `over_count` tinyint(5) NOT NULL DEFAULT '0',
  `block_flag` tinyint(5) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `filter_limit_config` (
  `over_count_limit` int(11) NOT NULL,
  `over_pm25_limit` int(11) NOT NULL,
  PRIMARY KEY (`over_count_limit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `gmair_machine`.`filter_light` (
`machine_id` VARCHAR(20) NOT NULL,
`light_status` TINYINT(5) NOT NULL,
`block_flag` TINYINT(5) NOT NULL,
`create_time` DATETIME NOT NULL,
PRIMARY KEY (`machine_id`));

ALTER TABLE `gmair_machine`.`pm2_5_latest`
RENAME TO  `gmair_machine`.`out_pm2_5_hourly` ;

ALTER TABLE `gmair_machine`.`out_pm2_5_hourly`
ADD COLUMN `index` INT(11) NOT NULL AFTER `pm2_5`;

ALTER TABLE `gmair_machine`.`out_pm2_5_hourly`
CHANGE COLUMN `index` `index_hour` INT(11) NOT NULL ;

##2018-11-26
CREATE TABLE `gmair_machine`.`machine_on_off` (
  `config_id` VARCHAR(45) NOT NULL,
  `uid` VARCHAR(45) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  `start_time` TIME DEFAULT NULL,
  `end_time` TIME DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`config_id`));


#2018-01-02
CREATE TABLE `machine_daily_power` (
  `status_id` varchar(25) NOT NULL,
  `machine_id` varchar(45) NOT NULL,
  `power_usage` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#2018-01-14
CREATE TABLE `machine_list_daily` (
  `consumer_id` varchar(45) NOT NULL,
  `bind_name` varchar(45) NOT NULL,
  `code_value` varchar(45) NOT NULL,
  `machine_id` varchar(45) NULL,
  `consumer_name` varchar(45) NULL,
  `consumer_phone` varchar(45) NULL,
  `over_count` int(11) NOT NULL DEFAULT '0',
  `offline` tinyint(1) NOT NULL DEFAULT '0',
  `block_flag` tinyint(10) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`consumer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#2018-01-14
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
VIEW `gmair_machine`.`machine_list_view` AS
    SELECT
        `ccb`.`consumer_id` AS `consumer_id`,
        `ccb`.`bind_name` AS `bind_name`,
        `ccb`.`code_value` AS `code_value`,
        `cmb`.`machine_id` AS `machine_id`,
        `ci`.`consumer_name` AS `consumer_name`,
        `cp`.`phone_number` AS `consumer_phone`,
        `ccb`.`block_flag` AS `block_flag`,
        `ccb`.`create_time` AS `create_time`
    FROM
        (((`gmair_machine`.`consumer_code_bind` `ccb`
        LEFT JOIN `gmair_machine`.`code_machine_bind` `cmb` ON ((`ccb`.`code_value` = `cmb`.`code_value`)))
        LEFT JOIN `gmair_userinfo`.`consumer_info` `ci` ON ((`ccb`.`consumer_id` = `ci`.`consumer_id`)))
        LEFT JOIN `gmair_userinfo`.`consumer_phone` `cp` ON ((`ccb`.`consumer_id` = `cp`.`consumer_id`)))
    WHERE
        ((`ccb`.`ownership` = 0)
            AND (`ccb`.`block_flag` = 0))

#2018-01-15
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
VIEW `gmair_machine`.`machine_list_second_view` AS
    SELECT
        `ccb`.`consumer_id` AS `consumer_id`,
        `ccb`.`bind_name` AS `bind_name`,
        `ccb`.`code_value` AS `code_value`,
        `cmb`.`machine_id` AS `machine_id`,
        `ci`.`consumer_name` AS `consumer_name`,
        `cp`.`phone_number` AS `consumer_phone`,
        `opd`.`over_count` AS `over_count`,
        `ccb`.`block_flag` AS `block_flag`,
        `ccb`.`create_time` AS `create_time`,
        `opd`.`create_time` AS `out_pm25_time`
    FROM
        ((((`gmair_machine`.`consumer_code_bind` `ccb`
        LEFT JOIN `gmair_machine`.`code_machine_bind` `cmb` ON ((`ccb`.`code_value` = `cmb`.`code_value`)))
        LEFT JOIN `gmair_userinfo`.`consumer_info` `ci` ON ((`ccb`.`consumer_id` = `ci`.`consumer_id`)))
        LEFT JOIN `gmair_userinfo`.`consumer_phone` `cp` ON ((`ccb`.`consumer_id` = `cp`.`consumer_id`)))
        LEFT JOIN `gmair_machine`.`out_pm2_5_daily` `opd` ON ((`opd`.`machine_id` = `cmb`.`machine_id`)))
    WHERE
        ((`ccb`.`ownership` = 0)
            AND (`ccb`.`block_flag` = 0)
            AND ((TO_DAYS(CURDATE()) - TO_DAYS(`opd`.`create_time`)) < 1))