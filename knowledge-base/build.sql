DROP TABLE IF EXISTS `knowledge`;
CREATE TABLE `knowledge` (
  `id`    bigint(20)      AUTO_INCREMENT  NOT NULL,
  `status`           int(10)         NOT NULL,
  `title`           varchar(200)    NOT NULL,
  `content`         text      NOT NULL,
  `views`           int(100)        DEFAULT 0,
  `create_time` DATETIME default CURRENT_TIMESTAMP,
  `modify_time` DATETIME,
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
    `create_time` DATETIME default CURRENT_TIMESTAMP,
    `solve_time` DATETIME,
    `responser_id` int NOT NULL,
    `type` TINYINT NOT NULL,
     PRIMARY KEY (`id`),
     FOREIGN KEY(knowledge_id) REFERENCES knowledge(id) on delete cascade
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
    `id` int(10) NOT NULL AUTO_INCREMENT,
    `tag_name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `tag_relation`;
CREATE TABLE `tag_relation` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `knowledge_id` bigint(10) NOT NULL,
   `tag_id` int(10) NOT NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY(knowledge_id) REFERENCES knowledge(id) on delete cascade,
   FOREIGN KEY(tag_id) REFERENCES tag(id) on delete cascade
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
alter table tag_relation add index index_knowledge_id(knowledge_id) ;
alter table tag_relation add index index_tag_id(tag_id) ;

# 鉴权
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
`user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
`user_name` varchar(50) NOT NULL ,
`user_pwd` varchar(32) NOT NULL ,
`block_flag` TINYINT(1) NOT NULL DEFAULT '0',
`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(`user_id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`(
`role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
`role_name` varchar(50) NOT NULL ,
`role_description` varchar(100),
 primary key (`role_id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`(
`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
`authorize` varchar (50) NOT NULL ,
`describe` varchar (100),
primary key (`id`)
) ENGINE =InnoDB
DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `permission_assignment`;
CREATE TABLE  `permission_assignment` (
  `rid` int(11) unsigned NOT NULL COMMENT '角色ID',
  `pid` int(11) unsigned NOT NULL COMMENT '权限ID'
) ENGINE=InnoDB COMMENT='权限与角色关联表';

DROP TABLE IF EXISTS `user_assignment`;
CREATE TABLE `user_assignment` (
  `uid` int(11) unsigned NOT NULL COMMENT '用户ID',
  `rid` int(11) unsigned NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB  COMMENT='用户与角色关联表';

INSERT INTO `user` (`user_id`, `user_name`,`user_pwd`) VALUES
(1,'manager1',md5('manager1')),
(2,'editor1',md5('editor1')),
(3,'user1',md5('user1'));

INSERT INTO `role` (role_id, role_name, role_description) VALUES
(1,'knowledgeBase_manager','知识库管理人员'),
(2,'knowledgeBase_editor','知识库编辑人员'),
(3,'knowledgeBase_user','知识库使用人员');

INSERT INTO `permission`(`id`,`authorize`,`describe`) VALUES
(1,'comment_create','创建评论'),
(2,'comment_modify','修改评论的状态'),
(3,'comment_getOwn','获取自己的评论列表'),
(4,'comment_getAll','获取所有的评论列表');

INSERT INTO `user_assignment`(`uid`,`rid`) VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO `permission_assignment`(`rid`,`pid`) VALUES
(1,1),
(1,3),
(2,2),
(2,4),
(3,1),
(3,3);

