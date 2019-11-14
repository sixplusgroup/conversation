# 2019-11-13
CREATE TABLE `open_platform`.`corp_notification` (
  `notification_id`  VARCHAR(20) NOT NULL,
  `corp_id`          VARCHAR(20) NOT NULL,
  `notification_url` VARCHAR(50) NOT NULL,
  `block_flag`       TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`      DATETIME    NOT NULL,
  PRIMARY KEY (`notification_id`)
);
