package com.lion.upms.expose.user.impl;

import com.lion.upms.expose.user.UserExposeService;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.user.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.lion.core.Optional;

/**
 * @description: 用户远程RPC接口暴露实现
 * @author: Mr.Liu
 * @create: 2020-01-19 11:01
 */
@DubboService(interfaceClass = UserExposeService.class)
public class UserExposeServiceImpl extends com.lion.core.service.impl.BaseServiceImpl<User> implements UserExposeService, com.lion.core.service.BaseService<User> {

    @Autowired
    private UserService userService;

    @Override
    public User createUser(User user) {
        return userService.save(user);
    }

    @Override
    public Optional<User> findUser(String username) {
        return userService.findUser(username);
    }
}
