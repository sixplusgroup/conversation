CREATE TABLE `gmair_install`.`user_session` (
  `open_id`     VARCHAR(100) NOT NULL,
  `session_key` VARCHAR(100) NOT NULL,
  `block_flag`  TINYINT(1)   NOT NULL,
  `create_time` DATETIME     NOT NULL,
  PRIMARY KEY (`open_id`)
);
