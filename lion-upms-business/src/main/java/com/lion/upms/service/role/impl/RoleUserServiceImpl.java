package com.lion.upms.service.role.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.role.RoleUserDao;
import com.lion.upms.entity.role.RoleUser;
import com.lion.upms.service.role.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.liu
 * @title: RoleUserServiceImpl
 * @description: 角色与用户关联Service
 * @date 2020/8/15下午5:07
 */
@Service
public class RoleUserServiceImpl extends BaseServiceImpl<RoleUser> implements RoleUserService {

    @Autowired
    private RoleUserDao roleUserDao;
}
