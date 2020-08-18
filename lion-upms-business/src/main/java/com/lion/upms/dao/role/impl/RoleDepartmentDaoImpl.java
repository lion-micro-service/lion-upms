package com.lion.upms.dao.role.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.role.RoleDepartmentDaoEx;
import com.lion.upms.entity.role.RoleDepartment;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: RoleDepartmentDaoImpl
 * @description: 角色与部门复杂sql操作扩展
 * @date 2020/8/15下午5:22
 */
public class RoleDepartmentDaoImpl implements RoleDepartmentDaoEx {

    @Autowired
    private BaseDao<RoleDepartment>  baseDao;
}
