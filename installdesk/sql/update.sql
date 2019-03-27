#2019-03-27
CREATE TABLE `gmair_install`.`dispatch_record` (
  `record_id`   VARCHAR(20) NOT NULL,
  `assign_id`   VARCHAR(20) NOT NULL,
  `team_id`     VARCHAR(20) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`record_id`)
);
