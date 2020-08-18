package com.lion.upms.dao.department.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.department.DepartmentUserDaoEx;
import com.lion.upms.entity.department.DepartmentUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: DepartmentUserDaoImpl
 * @description: 部门与用户关联复杂sql操作扩展
 * @date 2020/8/15下午5:59
 */
public class DepartmentUserDaoImpl implements DepartmentUserDaoEx {

    @Autowired
    private BaseDao<DepartmentUser> baseDao;
}
