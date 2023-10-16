create table user
(
    id           bigint auto_increment comment '主键'
        primary key,
    username     varchar(256)                       null comment '姓名',
    userAccount  varchar(256)                       null comment '登陆账号',
    avatarUrl    varchar(1024)                      null comment '账号头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(256)                       null comment '用户登录 密码',
    phone        varchar(256)                       null comment '手机号码',
    email        varchar(256)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户账号状态',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '修改时间',
    isDelete     tinyint  default 0                 null comment '是否删除',
    userRole     int      default 0                 not null comment '0代表普通用户，1代表管理员'
)
    comment '用户表';