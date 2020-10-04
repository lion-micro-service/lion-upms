package com.lion.upm.expose.user.impl;

import com.lion.security.LionSimpleGrantedAuthority;
import com.lion.security.LionUserDetails;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.resources.ResourcesService;
import com.lion.upms.service.user.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description: 用户接口暴露实现
 * @author: Mr.Liu
 * @create: 2020-01-17 10:29
 */
@DubboService(interfaceClass = UserDetailsService.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourcesService resourcesService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userService.findUser(username);
        if (Objects.isNull(user)){
            return null;
        }
        LionUserDetails userDetails = new LionUserDetails(user.getUsername(),user.getPassword(),getUserGrantedAuthority(user.getId()));
        return userDetails;
    }

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    private List<GrantedAuthority> getUserGrantedAuthority(Long userId){
        List<Resources> listResources = resourcesService.findAllResources(userId);
        List<GrantedAuthority> listGrantedAuthority = new ArrayList<GrantedAuthority>();
        listResources.forEach(resources -> {
            LionSimpleGrantedAuthority grantedAuthority = new LionSimpleGrantedAuthority();
            grantedAuthority.setAuthority(resources.getCode());
            listGrantedAuthority.add(grantedAuthority);
        });
        return listGrantedAuthority;
    }




}
