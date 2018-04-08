DROP TABLE if EXISTS `text_template`;
CREATE TABLE `text_template` (
`template_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`message_type`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`response`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`block_flag`  tinyint(1) NOT NULL DEFAULT 0 ,
`create_time`  datetime NOT NULL ,
PRIMARY KEY (`template_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;
