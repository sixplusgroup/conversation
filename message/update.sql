#2018.04.09 add table message_signature
CREATE TABLE `gmair_message`.`message_signature` (
  `sign_id`     VARCHAR(20) NOT NULL,
  `sign_name`   VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`sign_id`)
);

#2018.04.09 add table message_template
CREATE TABLE `gmair_message`.`message_template` (
  `template_id`     VARCHAR(20)  NOT NULL,
  `message_catalog` TINYINT(1)   NOT NULL DEFAULT 0,
  `message_content` VARCHAR(100) NOT NULL,
  `block_flag`      TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time`     DATETIME     NOT NULL,
  PRIMARY KEY (`template_id`)
);
