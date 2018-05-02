
USE `gmair_resource` ;

CREATE TABLE `gmair_resource`.`tempfile_location` (
  `file_id` VARCHAR(20) NOT NULL,
  `file_url` VARCHAR(200) NOT NULL,
  `actual_path` VARCHAR(100) NOT NULL,
  `filename` VARCHAR(50) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`file_id`));
