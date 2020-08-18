package com.lion.upms.service.user;

import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.service.BaseService;
import com.lion.upms.entity.user.User;
import org.springframework.data.domain.Page;

/**
 * @description: 用户Service
 * @author: Mr.Liu
 * @create: 2020-01-15 14:52
 */
public interface UserService extends BaseService<User> {

    /**
     * test
     * @param lionPage
     * @return
     */
    public Page<User> test(LionPage lionPage);

    /**
     * 根据用户名获取用户
     * @param username 登陆用户名
     * @return
     */
    User findUser(String username);

}
