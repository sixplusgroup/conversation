drop database gmair_chatlog;
create database if not exists gmair_chatlog;

use gmair_chatlog;

# 创建用户表
drop table if exists `usr_jd`;
create table `usr_jd`
(
    usr_id   int         not null comment '主键用户id' auto_increment primary key,
    usr_name varchar(50) not null comment '用户名'
)
    DEFAULT CHARSET = utf8
    comment '京东用户表';

# 创建客服表，手动初始化
drop table if exists `waiter_jd`;
create table `waiter_jd`
(
    waiter_id   int         not null comment '主键客服id' auto_increment primary key,
    waiter_name varchar(50) not null comment '客服名'
)
    DEFAULT CHARSET = utf8
    comment '京东客服表';

insert into `waiter_jd`
set waiter_name='果麦果果';


# 创建用户聊天会话表,转换sessionId为整型id
drop table if exists `usr_session_jd`;
create table `usr_session_jd`
(
    session_id          int         not null comment '主键会话id' auto_increment primary key,
    original_session_id varchar(50) not null comment '京东原会话id',
    usr_id              int         not null comment '用户id',
    waiter_id           int         not null comment '客服id',
    product_id          varchar(20) not null comment '商品id',
    foreign key (usr_id) references usr_jd (usr_id),
    foreign key (waiter_id) references waiter_jd (waiter_id)
)
    DEFAULT CHARSET = utf8
    comment '京东用户会话表';


# 创建会话聊天信息表
drop table if exists `session_message_jd`;
create table `session_message_jd`
(
    message_id     int  not null comment '主键id' auto_increment primary key,
    session_id     int  not null comment '会话id',
    content        text not null comment '聊天内容',
    is_from_waiter boolean default false comment '是否由客服发送',
    timestamp      long not null comment '发送时间',
    label          varchar(10) comment '消息评价，0-negative，1-neutral，2-positive',
    score          double comment '消息评分',
    foreign key (session_id) references usr_session_jd (session_id)
)
    DEFAULT CHARSET = utf8
    comment '京东消息内容表';


# insert into usr_jd
# set usr_name='user1';
#
# insert into waiter_jd
# set waiter_name='waiter1';
#
# insert into usr_session_jd
# set original_session_id='1234567',
#     usr_id=1,
#     waiter_id=1,
#     product_id='12345';
#
# insert into session_message_jd
# set content='test1',
#     session_id=1,
#     timestamp=0;
# insert into session_message_jd
# set content='test2',
#     session_id=1,
#     timestamp=0;
# insert into session_message_jd
# set content='test3',
#     session_id=1,
#     timestamp=0;




