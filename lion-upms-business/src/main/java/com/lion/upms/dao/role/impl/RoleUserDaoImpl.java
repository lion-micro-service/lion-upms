package com.lion.upms.dao.role.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.role.RoleUserDaoEx;
import com.lion.upms.entity.role.Role;
import com.lion.upms.entity.role.RoleUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: RoleUserDaoImpl
 * @description: 角色与用户关联复杂sql操作扩展
 * @date 2020/8/15下午5:16
 */
public class RoleUserDaoImpl implements RoleUserDaoEx {

    @Autowired
    private BaseDao<RoleUser> baseDao;
}
