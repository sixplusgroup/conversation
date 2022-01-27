INSERT INTO `tz_sys_user` VALUES (1, 'admin', '{bcrypt}$2a$10$AV9Xz.3ck4RsXiad5ArcBO5.ZKwlpcnJzs740BHY..fsSp0PnM/Zu', '1727177598@qq.com', '13001798480', 1, 1, '2021-12-04 11:11:11', 1);

INSERT INTO `tz_sys_menu` VALUES (1, 0, '系统管理', '', '', 0, 'system', 3);
INSERT INTO `tz_sys_menu` VALUES (2, 1, '管理员列表', 'sys/user', '', 1, 'admin', 1);
INSERT INTO `tz_sys_menu` VALUES (3, 1, '角色管理', 'sys/role', '', 1, 'role', 2);
INSERT INTO `tz_sys_menu` VALUES (4, 1, '菜单管理', 'sys/menu', '', 1, 'menu', 3);
INSERT INTO `tz_sys_menu` VALUES (6, 1, '定时任务', 'sys/schedule', '', 1, 'job', 5);
INSERT INTO `tz_sys_menu` VALUES (7, 6, '查看', NULL, 'sys:schedule:page,sys:schedule:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (8, 6, '新增', NULL, 'sys:schedule:save', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (9, 6, '修改', NULL, 'sys:schedule:update', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (10, 6, '删除', NULL, 'sys:schedule:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (11, 6, '暂停', NULL, 'sys:schedule:pause', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (12, 6, '恢复', NULL, 'sys:schedule:resume', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (13, 6, '立即执行', NULL, 'sys:schedule:run', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (14, 6, '日志列表', NULL, 'sys:schedule:log', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:page,sys:user:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (16, 2, '新增', '', 'sys:user:save,sys:role:list', 2, '', 1);
INSERT INTO `tz_sys_menu` VALUES (17, 2, '修改', '', 'sys:user:update,sys:role:list', 2, '', 2);
INSERT INTO `tz_sys_menu` VALUES (18, 2, '删除', '', 'sys:user:delete', 2, '', 3);
INSERT INTO `tz_sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:page,sys:role:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (27, 1, '参数管理', 'sys/config', 'sys:config:page,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'config', 6);
INSERT INTO `tz_sys_menu` VALUES (29, 1, '系统日志', 'sys/log', 'sys:log:page', 1, 'log', 7);
INSERT INTO `tz_sys_menu` VALUES (34, 0, '产品管理', '', '', 0, 'admin', 0);
INSERT INTO `tz_sys_menu` VALUES (51, 34, '规格管理', 'prod/spec', '', 1, '', 2);
INSERT INTO `tz_sys_menu` VALUES (57, 51, '查看', '', 'prod:spec:page', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (58, 51, '添加', '', 'prod:spec:save', 2, '', 1);
INSERT INTO `tz_sys_menu` VALUES (59, 51, '修改', '', 'prod:spec:update,prod:spec:info', 2, '', 2);
INSERT INTO `tz_sys_menu` VALUES (60, 51, '删除', '', 'prod:spec:delete', 2, '', 3);
INSERT INTO `tz_sys_menu` VALUES (63, 0, '门店管理', '', '', 0, 'store', 0);
INSERT INTO `tz_sys_menu` VALUES (70, 34, '产品管理', 'prod/prodList', '', 1, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (71, 70, '产品管理', '', 'prod:prod:page', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (72, 70, '查看产品', '', 'prod:prod:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (73, 70, '新增产品', '', 'prod:prod:save', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (74, 70, '修改产品', '', 'prod:prod:update', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (75, 70, '删除产品', '', 'prod:prod:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (91, 0, '订单管理', '', '', 0, 'order', 2);
INSERT INTO `tz_sys_menu` VALUES (92, 91, '订单管理', 'order/order', '', 1, NULL, 1);
INSERT INTO `tz_sys_menu` VALUES (93, 92, '查看', '', 'order:order:page', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (99, 92, '保存', '', 'order:order:save', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (100, 92, '修改', '', 'order:order:update', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (101, 92, '删除', '', 'order:order:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (107, 92, '详情', '', 'order:order:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (108, 92, '支付', '', 'order:order:pay', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (125, 0, '用户管理', '', '', 0, 'vip', 0);
INSERT INTO `tz_sys_menu` VALUES (126, 125, '用户管理', 'user/user', '', 1, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (127, 126, '查看', '', 'admin:user:page', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (128, 126, '新增', '', 'admin:user:save', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (129, 126, '修改', '', 'admin:user:update,admin:user:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (130, 126, '删除', '', 'admin:user:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (131, 63, '自提点管理', 'shop/pickAddr', '', 1, '', 0);
INSERT INTO `tz_sys_menu` VALUES (132, 131, '查看', '', 'shop:pickAddr:page', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (133, 131, '保存', '', 'shop:pickAddr:save', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (134, 131, '修改', '', 'shop:pickAddr:update,shop:pickAddr:info', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (135, 131, '删除', '', 'shop:pickAddr:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (136, 34, '分类管理', 'prod/category', '', 1, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (137, 136, '查看', '', 'prod:category:page', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (138, 136, '新增', '', 'prod:category:save', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (139, 136, '修改', '', 'prod:category:info,prod:category:update', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (140, 136, '删除', '', 'prod:category:delete', 2, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (146, 92, '发货', '', 'order:order:delivery', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (163, 63, '运费模板', 'shop/transport', '', 1, NULL, 0);
INSERT INTO `tz_sys_menu` VALUES (164, 163, '查看', '', 'shop:transport:page,shop:shopDetail:info,shop:transfee:info,admin:area:page,shop:transcity:info', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (165, 163, '修改', '', 'shop:transport:update,shop:transport:info,shop:transfee:update,admin:area:page', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (166, 163, '新增', '', 'shop:transport:save,shop:transfee:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (167, 163, '删除', '', 'shop:transport:delete,shop:transfee:delete', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (174, 63, '轮播图管理', 'admin/indexImg', '', 1, '', 0);
INSERT INTO `tz_sys_menu` VALUES (175, 174, '查看', '', 'admin:indexImg:page', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (176, 174, '新增', '', 'admin:indexImg:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (177, 174, '修改', '', 'admin:indexImg:info,admin:indexImg:update', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (178, 174, '删除', '', 'admin:indexImg:delete', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (184, 92, '导出待发货订单', '', 'order:order:waitingConsignmentExcel', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (185, 92, '导出销售记录', '', 'order:order:soldExcel', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (201, 63, '热搜管理', 'shop/hotSearch', '', 1, '', 0);
INSERT INTO `tz_sys_menu` VALUES (202, 201, '查询热搜', '', 'admin:hotSearch:page', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (203, 201, '查询热搜详情', '', 'admin:hotSearch:page', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (204, 201, '添加热搜', '', 'admin:hotSearch:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (205, 201, '修改热搜', '', 'admin:hotSearch:update', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (206, 201, '删除热搜', '', 'admin:hotSearch:delete', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (230, 126, '添加', '', 'user:addr:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (239, 34, '分组管理', 'prod/prodTag', 'prod:prodTag', 1, '', 0);
INSERT INTO `tz_sys_menu` VALUES (240, 239, '添加商品分组', '', 'prod:prodTag:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (241, 239, '修改商品分组', '', 'prod:prodTag:update', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (242, 239, '删除商品分组', '', 'prod:prodTag:delete', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (243, 239, '查看商品分组', '', 'prod:prodTag:info,prod:prodTag:page', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (300, 63, '公告管理', 'shop/notice', '', 1, '', 0);
INSERT INTO `tz_sys_menu` VALUES (301, 300, '添加公告', '', 'shop:notice:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (302, 300, '修改公告', '', 'shop:notice:update', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (303, 300, '查看公告', '', 'shop:notice:info,shop:notice:page', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (305, 300, '删除公告', '', 'shop:notice:delete', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (306, 34, '评论管理', 'prod/prodComm', '', 1, '', 1);
INSERT INTO `tz_sys_menu` VALUES (307, 306, '查看', '', 'prod:prodComm:page,prod:prodComm:info', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (308, 306, '添加', '', 'prod:prodComm:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (309, 306, '修改', '', 'prod:prodComm:update', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (310, 306, '删除', '', 'prod:prodComm:delete', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (312, 1, '地址管理', 'sys/area', '', 1, 'dangdifill', 0);
INSERT INTO `tz_sys_menu` VALUES (313, 312, '新增地址', '', 'admin:area:save', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (314, 312, '修改地址', '', 'admin:area:update', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (315, 312, '删除地址', '', 'admin:area:delete', 2, '', 0);
INSERT INTO `tz_sys_menu` VALUES (316, 312, '查看地址', '', 'admin:area:info,admin:area:page,admin:area:list', 2, '', 0);

INSERT INTO `tz_delivery` VALUES (14, '顺丰快递公司', 'http://www.sf-express.com', '2015-08-20 11:58:03', '2017-03-22 17:12:27', 'http://www.kuaidi100.com/query?type=shunfeng&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (15, '申通快递公司', 'http://www.sto-express.com', '2015-08-20 11:58:24', '2015-08-20 12:04:23', 'http://www.kuaidi100.com/query?type=shentong&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (16, '中通速递', 'http://www.zto.cn', '2015-08-20 11:58:48', '2015-08-20 12:04:31', 'http://www.kuaidi100.com/query?type=zhongtong&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (17, '安信达', 'http://www.anxinda.com', '2015-12-22 10:19:33', '2015-12-22 10:37:26', 'http://www.kuaidi100.com/query?type=anxindakuaixi&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (18, 'EMS', 'http://www.ems.com.cn', '2015-12-22 10:38:15', '2015-12-22 10:38:15', 'http://www.kuaidi100.com/query?type=ems&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (19, '凡客如风达', 'http://www.rufengda.com', '2015-12-22 10:38:55', '2015-12-22 10:38:55', 'http://www.kuaidi100.com/query?type=rufengda&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (20, '汇通快递', 'http://www.htky365.com', '2015-12-22 10:39:46', '2015-12-22 10:39:46', 'http://www.kuaidi100.com/query?type=huitongkuaidi&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (21, '天天快递', 'http://www.ttkdex.com', '2015-12-22 10:40:44', '2015-12-22 10:40:44', 'http://www.kuaidi100.com/query?type=tiantian&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (22, '佳吉快运', 'http://www.jiaji.com', '2015-12-22 10:42:55', '2015-12-22 10:42:55', 'http://www.kuaidi100.com/query?type=jiajiwuliu&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (23, '速尔快递', 'http://www.sure56.com', '2015-12-22 10:43:35', '2015-12-22 10:43:35', 'http://www.kuaidi100.com/query?type=suer&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (24, '信丰物流', 'http://www.xf-express.com.cn', '2015-12-22 10:44:17', '2015-12-22 10:44:17', 'http://www.kuaidi100.com/query?type=xinfengwuliu&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (25, '韵达快递', 'http://www.yundaex.com', '2015-12-22 10:44:51', '2015-12-22 10:44:51', 'http://www.kuaidi100.com/query?type=yunda&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (26, '优速快递', 'http://www.uc56.com', '2015-12-22 10:45:20', '2015-12-22 10:45:20', 'http://www.kuaidi100.com/query?type=youshuwuliu&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (27, '中邮物流', 'http://www.cnpl.com.cn', '2015-12-22 10:45:58', '2015-12-22 10:45:58', 'http://www.kuaidi100.com/query?type=zhongyouwuliu&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (28, '圆通快递', 'http://www.yto.net.cn', '2015-12-22 11:44:18', '2015-12-22 11:44:18', 'http://www.kuaidi100.com/query?type=yuantong&postid={dvyFlowId}&id=11');
INSERT INTO `tz_delivery` VALUES (29, '宅急送', 'http://www.zjs.com.cn', '2015-12-22 11:45:55', '2015-12-22 11:45:55', 'http://www.kuaidi100.com/query?type=zhaijisong&postid={dvyFlowId}&id=11')

# 2021 12 4
ALTER TABLE `gmair_membership`.`tz_integral_add` ADD COLUMN `status` int(2) NOT NULL DEFAULT 0;