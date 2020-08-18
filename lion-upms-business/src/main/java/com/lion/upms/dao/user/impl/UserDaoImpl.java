package com.lion.upms.dao.user.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.user.UserDaoEx;
import com.lion.upms.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: UserDaoImpl
 * @description: 用户dao复杂sql操作扩展
 * @date 2020/8/15下午5:00
 */
public class UserDaoImpl implements UserDaoEx {

    @Autowired
    private BaseDao<User> baseDao;
}
