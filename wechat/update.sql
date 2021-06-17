DROP TABLE IF EXISTS `text_template`;
CREATE TABLE `text_template` (
  `template_id`  VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci   NOT NULL,
  `message_type` VARCHAR(45) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL,
  `response`     LONGTEXT CHARACTER SET utf8
  COLLATE utf8_general_ci   NOT NULL,
  `block_flag`   TINYINT(1) NOT NULL DEFAULT 0,
  `create_time`  DATETIME   NOT NULL,
  PRIMARY KEY (`template_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

##2018.04.17
CREATE VIEW text_reply_view
  AS
    SELECT
      auto_reply.keyword        AS keyword,
      auto_reply.message_type   AS message_type,
      text_template.template_id AS template_id,
      text_template.response    AS response
    FROM auto_reply, text_template
    WHERE auto_reply.template_id = text_template.template_id;

##2018.04.17
CREATE VIEW picture_reply_view
  AS
    SELECT
      picture_template.template_id AS template_id,
      auto_reply.message_type      AS message_type,
      auto_reply.keyword           AS keyword,
      picture_template.picture_url AS picture_url
    FROM auto_reply, picture_template
    WHERE auto_reply.template_id = picture_template.template_id;

##2018.04.17
CREATE VIEW article_reply_view
  AS
    SELECT
      article_template.template_id         AS template_id,
      auto_reply.message_type              AS message_type,
      auto_reply.keyword                   AS keyword,
      article_template.article_title       AS article_title,
      article_template.article_url         AS article_url,
      article_template.picture_url         AS picture_url,
      article_template.description_content AS description_content
    FROM auto_reply, article_template
    WHERE auto_reply.template_id = article_template.template_id;

#2018.04.23 alter table to store more information
ALTER TABLE `gmair_wechat`.`wechat_user`
  ADD COLUMN `user_country` VARCHAR(20) NOT NULL
  AFTER `user_province`,
  ADD COLUMN `headimg_url` VARCHAR(50) NOT NULL
  AFTER `user_country`,
  ADD COLUMN `user_unioinid` VARCHAR(20) NOT NULL
  AFTER `headimg_url`;

#2018.04.26 create table to store resources
CREATE TABLE `gmair_wechat`.`wechat_resource` (
  `resource_id`   VARCHAR(55) NOT NULL,
  `resource_name` VARCHAR(45) NOT NULL,
  `block_flag`    TINYINT(1)  NOT NULL,
  `create_time`   DATETIME    NOT NULL
);

#2020.01.09 create table to store message template

CREATE TABLE message_template
(
  template_id     VARCHAR(100),
  message_type    INT(3),
  message_content LONGTEXT,
  block_flag      TINYINT(1),
  create_time     DATETIME
);

#====================================
#2020.04.15
CREATE TABLE `gmair_wechat`.`video_template` (
  `template_id` VARCHAR(20)  NOT NULL,
  `video_url`   VARCHAR(200) NOT NULL,
  `video_title` VARCHAR(45)  NOT NULL,
  `video_desc`  VARCHAR(200) NOT NULL,
  `block_flag`  TINYINT(1)   NOT NULL,
  `create_time` DATETIME     NOT NULL,
  PRIMARY KEY (`template_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_bin;

# 2021-1-10
CREATE TABLE `gmair_wechat`.`reply_record` (
  `record_id`     VARCHAR(20) NOT NULL,
  `user_id`       VARCHAR(45) NOT NULL,
  `template_id`   VARCHAR(45) NULL,
  `reply_content` LONGTEXT    NOT NULL,
  `block_flag`    TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`   DATETIME    NULL,
  PRIMARY KEY (`record_id`)
);
