DROP TABLE IF EXISTS `knowledge`;
CREATE TABLE `knowledge` (
  `id`    bigint(20)      AUTO_INCREMENT  NOT NULL,
  `state`           int(10)         NOT NULL,
  `title`           varchar(200)    NOT NULL,
  `content`         mediumtext      NOT NULL,
  `knowledge_type`  int(10)         NOT NULL,
  `views`           int(100)        DEFAULT 0,
  PRIMARY KEY (`id`),
  FULLTEXT KEY title_content_fulltext(title, content)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `knowledge_type`;
CREATE TABLE `knowledge_type` (
    `id`    int(10)      AUTO_INCREMENT  NOT NULL,
    `type_name`         varchar(200) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
