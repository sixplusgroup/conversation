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