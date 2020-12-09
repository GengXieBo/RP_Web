package com.huaiguang.rpweb.mapper;

import com.huaiguang.rpweb.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author lixu
 * @since 2020-12-09
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
