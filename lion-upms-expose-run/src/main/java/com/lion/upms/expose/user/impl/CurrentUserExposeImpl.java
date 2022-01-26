package com.lion.upms.expose.user.impl;

import com.lion.core.ICurrentUser;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.user.UserService;
import com.lion.utils.BeanToMapUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-20 20:32
 */
@DubboService(interfaceClass = ICurrentUser.class)
public class CurrentUserExposeImpl implements ICurrentUser<User> {

    @Autowired
    private UserService userService;

    @Override
    public Map<String, Object> findUserToMap(String username) {
        Optional optional = userService.findUser(username);
        return BeanToMapUtil.transBeanToMap(optional.isPresent()?optional.get():null) ;
    }


}