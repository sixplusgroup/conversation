CREATE TABLE `gmair_admininfo`.`role` (
  `role_id` varchar(20) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  `role_description` varchar(45) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE `gmair_admininfo`.`role_assign` (
  `assign_id` varchar(20) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `role_id` varchar(20) NOT NULL,
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`assign_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

create view `gmair_admininfo`.`admin_role_view`
as
select role_assign.role_id, role.role_name, role.role_description, role_assign.user_id, role_assign.block_flag, role_assign.create_time
from `gmair_admininfo`.role, `gmair_admininfo`.role_assign
where role.role_id = role_assign.role_id

## 2020-2-13
ALTER TABLE `gmair_admininfo`.`admin_info`
ADD COLUMN `admin_role` int(20) NULL DEFAULT 0 AFTER `create_time`;