package com.lion.upms.service.user;

import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.service.BaseService;
import com.lion.upms.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @description: 用户Service
 * @author: Mr.Liu
 * @create: 2020-01-15 14:52
 */
@Validated
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
    User findUser(@NotBlank(message = "用户登陆账号不能为空") String username);

    /**
     * 根据email查找用户
     * @param email
     * @return
     */
    User findUserByEmail(@NotBlank(message = "邮箱不能为空")String email);

}
