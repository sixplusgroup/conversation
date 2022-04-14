DROP DATABASE IF EXISTS rbac;
create DATABASE rbac character set utf8;
use rbac;

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
(1,'updateKnowledge','更新知识'),
(2,'approveKnowledge','通过一条知识的审批'),
(3,'getKnowledge','查看一条知识'),
(4,'getUnApproveKnowledge','获得待审核知识列表');

INSERT INTO `user_assignment`(`uid`,`rid`) VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO `permission_assignment`(`rid`,`pid`) VALUES
(1,2),
(2,1),
(3,3),
(1,4);

