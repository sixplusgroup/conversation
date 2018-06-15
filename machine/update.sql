## create machine power setting view
create view `gmair_machine`.`power_setting_view`
as
SELECT
power_setting.setting_id as setting_id,
machine_setting.consumer_id as consumer_id,
machine_setting.setting_name as setting_name,
machine_setting.code_value as code_value,
power_setting.power_action as power_action,
power_setting.trigger_hour as trigger_hour,
power_setting.trigger_minute as trigger_minute
FROM
gmair_machine.power_setting, gmair_machine.machine_setting
where power_setting.setting_id = machine_setting.setting_id
and power_setting.block_flag = 0
and machine_setting.block_flag=0;


# 2018-06-12 modify machine_monthly_status
ALTER TABLE `gmair_machine`.`machine_monthly_status`
CHANGE COLUMN `record_date` `index` INT NOT NULL ;

ALTER TABLE `gmair_machine`.`machine_daily_status`
ADD COLUMN `status_id` VARCHAR(45) NOT NULL AFTER `create_time`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`status_id`);
ALTER TABLE `gmair_machine`.`machine_daily_status`
CHANGE COLUMN `status_id` `status_id` VARCHAR(45) NOT NULL FIRST;

ALTER TABLE `gmair_machine`.`machine_hourly_status`
ADD COLUMN `machine_id` VARCHAR(45) NOT NULL AFTER `status_id`;

ALTER TABLE `gmair_machine`.`machine_monthly_status`
ADD COLUMN `status_id` VARCHAR(45) NOT NULL FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`status_id`);

## 2018-06-14 create machine volume setting view
create view `gmair_machine`.`volume_setting_view`
as
SELECT
volume_setting.setting_id as setting_id,
machine_setting.consumer_id as consumer_id,
machine_setting.setting_name as setting_name,
machine_setting.code_value as code_value,
volume_setting.floor_pm2_5 as floor_pm2_5,
volume_setting.upper_pm2_5 as upper_pm2_5,
volume_setting.speed_value as speed_value
FROM
gmair_machine.volume_setting, gmair_machine.machine_setting
where volume_setting.setting_id = machine_setting.setting_id
and volume_setting.block_flag = 0
and machine_setting.block_flag=0;


## 2018-06-15 create machine light setting view
create view `gmair_machine`.`light_setting_view`
as
SELECT
light_setting.setting_id as setting_id,
machine_setting.consumer_id as consumer_id,
machine_setting.setting_name as setting_name,
machine_setting.code_value as code_value,
light_setting.light_value as light_value
FROM
gmair_machine.light_setting, gmair_machine.machine_setting
where light_setting.setting_id = machine_setting.setting_id
and light_setting.block_flag = 0
and machine_setting.block_flag=0;


