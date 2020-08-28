package com.lion.upms.service.user.impl;

import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.user.UserDao;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

//import io.seata.spring.annotation.GlobalTransactional;

/**
 * @description: 用户Service
 * @author: Mr.Liu
 * @create: 2020-01-17 10:23
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Resource
    private UserDao userDao;


    @Override
//    @GlobalTransactional(name = "${spring.application.name}")
    public Page<User> test(LionPage lionPage) {
        return this.findNavigator(lionPage);
    }

    @Override
    public User findUser(String username) {
        return userDao.findFirstByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findFirstByEmail(email);
    }
}
