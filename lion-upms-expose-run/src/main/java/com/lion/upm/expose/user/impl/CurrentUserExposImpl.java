package com.lion.upm.expose.user.impl;

import com.lion.core.ICurrentUser;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.user.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-20 20:32
 */
@DubboService(interfaceClass = ICurrentUser.class)
public class CurrentUserExposImpl implements ICurrentUser<User> {

    @Autowired
    private UserService userService;

    @Override
    public User findUser(String username) {
        return userService.findUser(username);
    }
}