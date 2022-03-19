package com.lion.upms.expose.user.impl;

import com.lion.common.expose.file.FileExposeService;
import com.lion.core.Optional;
import com.lion.upms.dao.user.UserDao;
import com.lion.upms.expose.user.UserExposeService;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.user.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description: 用户远程RPC接口暴露实现
 * @author: Mr.Liu
 * @create: 2020-01-19 11:01
 */
@DubboService(interfaceClass = UserExposeService.class)
public class UserExposeServiceImpl extends com.lion.core.service.impl.BaseServiceImpl<User> implements UserExposeService, com.lion.core.service.BaseService<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Override
    public User createUser(User user) {
        return userService.save(user);
    }

    @Override
    public Optional<User> findUser(String username) {
        java.util.Optional<User> optional = userService.findUser(username);
        if (optional.isPresent()) {
            return Optional.of(optional.get());
        }
        return Optional.empty();
    }

    @Override
    public List<User> find(List<Long> in, List<Long> notIn) {
        return userDao.find(in,notIn);
    }
}
