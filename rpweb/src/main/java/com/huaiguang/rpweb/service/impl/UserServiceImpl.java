package com.huaiguang.rpweb.service.impl;

import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.mapper.UserMapper;
import com.huaiguang.rpweb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lixu
 * @since 2020-12-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
