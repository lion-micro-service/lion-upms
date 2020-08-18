package com.lion.upms.dao.role.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.role.RoleResourcesDaoEx;
import com.lion.upms.entity.role.RoleResources;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: RoleResourcesDaoImpl
 * @description: 角色与资源关联复杂sql操作扩展
 * @date 2020/8/15下午5:20
 */
public class RoleResourcesDaoImpl implements RoleResourcesDaoEx {

    @Autowired
    private BaseDao<RoleResources> baseDao;
}
