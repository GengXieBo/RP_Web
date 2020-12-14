USE rpweb;
CREATE TABLE `slide` (
    `id` char(32) NOT NULL COMMENT '切片id',
    `userid` char(32)slide NOT NULL COMMENT '用户id',
    `path` varchar(50) NOT NULL COMMENT '切片地址',
    `result` varchar(50) NOT NULL COMMENT '计算结果地址',
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='切片表';