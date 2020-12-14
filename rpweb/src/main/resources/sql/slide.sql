USE rpweb;
CREATE TABLE `slide` (
    `id` char(32) NOT NULL COMMENT '切片id',
    `userid` char(32) NOT NULL COMMENT '用户id',
    `path` varchar(50) NOT NULL COMMENT '切片地址',
    `result` varchar(50) NOT NULL COMMENT '计算结果地址',
    `flag` char(2) NOT NULL COMMENT '计算标志位(0:未计算, 1:正在计算, 2:已计算完)',
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='切片表';