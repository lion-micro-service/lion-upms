package com.lion.upms.dao.role.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.role.RoleDaoEx;
import com.lion.upms.entity.role.Role;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: RoleDaoImpl
 * @description: 角色复杂sql操作扩展
 * @date 2020/8/15下午5:17
 */
public class RoleDaoImpl implements RoleDaoEx {

    @Autowired
    private BaseDao<Role> baseDao;
}
