#2018-10-19
ALTER TABLE `gmair_formaldehyde_detection`.`case_profile`
CHANGE COLUMN `case_city` `case_city_name` VARCHAR(45) NOT NULL ,
ADD COLUMN `case_city_id` VARCHAR(45) NOT NULL AFTER `check_date`,
ADD COLUMN `video_id` VARCHAR(45) NOT NULL AFTER `case_status`;
