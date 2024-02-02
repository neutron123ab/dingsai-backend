create table user
(
    id               bigint auto_increment comment '用户id'
        primary key,
    username         varchar(20)                          not null comment '用户名',
    password         varchar(100)                         null comment '密码',
    avatar_id        varchar(40)                          not null comment '用户头像id',
    telephone_number varchar(20)                          not null comment '电话号码',
    role             tinyint(1) default 0                 not null comment '角色（0-普通用户，1-管理员）',
    create_time      timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted       tinyint(1) default 0                 not null comment '是否删除(0-未删除，1-已删除)',
    constraint user_pk
        unique (username)
)
    comment '用户表';

create table trading_items
(
    id                  bigint auto_increment comment '商品id'
        primary key,
    title               varchar(30)                          not null comment '产品标题',
    type                varchar(20)                          not null comment '类型（海报、手绘、视频）',
    expect_price        decimal(10, 2)                       not null comment '需求方预期价格',
    topic               varchar(64)                          null comment '设计主题',
    element_requirement text                                 null comment '元素要求',
    remark              text                                 null comment '备注',
    original_file_id    varchar(50)                          not null comment '原始文件id',
    finished_file_id    varchar(50)                          null comment '成品文件id',
    create_time         timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    status              tinyint(1) default 0                 not null comment '当前状态（待接单-0、已接单-1、已交付-2）',
    is_delete           tinyint(1) default 0                 null comment '逻辑删除(0-未删除，1-已删除)'
)
    comment '交易物品表';

create table t_order
(
    id              bigint auto_increment comment '订单id'
        primary key,
    demand_user_id  bigint                                   not null comment '需求方id',
    demand_taker_id bigint                                   not null comment '接单人id',
    item_id         bigint                                   not null comment '需求id',
    status          tinyint(1)     default 0                 not null comment '订单状态（0-未接单 1-已接单 2-订单完成）',
    create_time     datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete       int            default 0                 not null comment '逻辑删除（0-未删除，1-已删除）',
    real_price      decimal(10, 2) default 0.00              not null comment '成交价',
    constraint order_pk3
        unique (item_id)
)
    comment '订单表';

