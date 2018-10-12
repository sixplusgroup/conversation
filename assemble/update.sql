#2018.10.12
ALTER TABLE `gmair_assemble`.`snapshot`
ADD COLUMN `check_status` TINYINT(1) NOT NULL DEFAULT '0' AFTER `snapshot_path`;
