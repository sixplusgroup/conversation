ALTER TABLE `gmair_monitor`.`monitor_logo`
  CHANGE COLUMN `logo_id` `profile_id` VARCHAR(20) NOT NULL,
  CHANGE COLUMN `path` `logo_path` VARCHAR(100) NOT NULL,
  ADD COLUMN `qrcode_path` VARCHAR(100) NOT NULL
  AFTER `logo_path`, RENAME TO `gmair_monitor`.`monitor_profile`;
