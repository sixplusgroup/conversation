##2018-11-30
CREATE TABLE `gmair_schedule`.`task` (
  `task_id` VARCHAR(45) NOT NULL,
  `task_name` VARCHAR(45) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`task_id`));

