package com.lion.upm.expose.user;

import com.lion.core.service.BaseExposeService;
import com.lion.upms.entity.user.User;

/**
 * @description: 用户远程RPC暴露接口
 * @author: Mr.Liu
 * @create: 2020-01-19 10:50
 */
public interface UserExposeService extends BaseExposeService<User> {

    /**
     * 创建user
     * @param user
     * @return
     */
    public User createUser(User user);

    /**
     * 根据用户名（登陆账号）查找用户
     * @param username
     * @return
     */
    public User findUser(String username);
}