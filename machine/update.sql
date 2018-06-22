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

#2018-06-22 CREATE VIEW
CREATE VIEW `gmair_machine`.`control_option_view`
  AS
    SELECT
      control_option.option_id AS option_id,
      control_option_action.value_id AS value_id,
      control_option_action.model_id AS model_id,
      control_option.option_name AS option_name,
      control_option.option_component AS option_component,
      control_option_action.action_name AS action_name,
      control_option_action.action_operator AS action_operator,
      control_option.block_flag AS block_flag,
      control_option_action.create_time as create_time
    FROM
      gmair_machine.control_option, gmair_machine.control_option_action
    WHERE control_option.option_id = control_option_action.control_id
          AND control_option.block_flag = 0
          AND control_option_action.block_flag = 0;


