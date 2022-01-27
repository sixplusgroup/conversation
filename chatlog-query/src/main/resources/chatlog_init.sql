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

# 创建会话表，转换字符sessionId为整型id
drop table if exists `session_jd`;
create table `session_jd`
(
    session_id          int         not null comment '主键会话id' auto_increment primary key,
    original_session_id varchar(50) not null comment '京东原会话id'
)
    DEFAULT CHARSET = utf8
    comment '京东会话表';

# 创建聊天信息表
drop table if exists `message_jd`;
create table `message_jd`
(
    message_id     int  not null comment '主键id' auto_increment primary key,
    session_id     int  not null comment '会话id',
    content        text not null comment '聊天内容',
    is_from_waiter boolean default false comment '是否由客服发送',
    timestamp      long not null comment '发送时间',
    analysis       text comment '消息评价',
    foreign key (session_id) references session_jd (session_id)
)
    DEFAULT CHARSET = utf8
    comment '京东消息内容表';

# 创建用户聊天会话表
drop table if exists `usr_session_jd`;
create table `usr_session_jd`
(
    usr_id             int         not null comment '用户id',
    session_id         int         not null comment '会话id',
    waiter_id          int         not null comment '客服id',
    product_id         varchar(20) not null comment '商品id',
    analysis           text comment '会话评价',
    foreign key (usr_id) references usr_jd (usr_id),
    foreign key (waiter_id) references waiter_jd (waiter_id),
    foreign key (session_id) references session_jd (session_id)
)
    DEFAULT CHARSET = utf8
    comment '京东用户会话表';

create index usr_chats on `usr_session_jd` (usr_id, session_id);



