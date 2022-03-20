package com.lion.upms.expose.user;

import com.lion.core.Optional;
import com.lion.upms.entity.user.User;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @description: 用户远程RPC暴露接口
 * @author: Mr.Liu
 * @create: 2020-01-19 10:50
 */
public interface UserExposeService extends com.lion.core.service.BaseService<User> {

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
    public Optional<User> findUser(String username);

    /**
     * 根据用户姓名查询
     * @param name
     * @return
     */
    public List<User> findByName(String name);

    /**
     * 根据id查询
     * @param in
     * @param notIn
     * @return
     */
    public List<User> find(List<Long> in,List<Long> notIn);
}
