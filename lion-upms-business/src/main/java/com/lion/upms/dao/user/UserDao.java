package com.lion.upms.dao.user;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.user.User;

/**
 * @author mr.liu
 * @title: UserDao
 * @description: 用户dao
 * @date 2020/8/15下午4:02
 */
public interface UserDao extends BaseDao<User> ,UserDaoEx {

    /**
     * 根据用户名(登陆账号)获取用户
     * @param username
     * @return
     */
    User findFirstByUsername(String username);

    /**
     * 根据email查找用户
     * @param email
     * @return
     */
    User findFirstByEmail(String email);
}
