DROP TABLE IF EXISTS `knowledge`;
CREATE TABLE `knowledge` (
  `id`    bigint(20)      AUTO_INCREMENT  NOT NULL,
  `state`           int(10)         NOT NULL,
  `title`           varchar(200)    NOT NULL,
  `content`         text      NOT NULL,
  `knowledge_type`  int(10)         NOT NULL,
  `views`           int(100)        DEFAULT 0,
  PRIMARY KEY (`id`),
  FULLTEXT KEY title_content_fulltext(title, content) # 创建联合全文索引列
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

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`(
    `id`  int(10)   NOT NULL AUTO_INCREMENT,
    `knowledge_id` bigint not null,
    `content` varchar(255) NOT NULL ,
    `status` TINYINT NOT NULL,
    `create_time` DATETIME NOT NULL,
    `solve_time` DATETIME,
    `responser_id` int NOT NULL,
    `type` TINYINT NOT NULL,
     PRIMARY KEY (`id`),
     FOREIGN KEY(knowledge_id) REFERENCES knowledge(id) on delete cascade
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;