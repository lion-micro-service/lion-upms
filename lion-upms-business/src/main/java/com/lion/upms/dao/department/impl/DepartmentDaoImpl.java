package com.lion.upms.dao.department.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.department.DepartmentDaoEx;
import com.lion.upms.entity.department.Department;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: DepartmentDaoImpl
 * @description: 部门复杂sql操作扩展
 * @date 2020/8/15下午5:36
 */
public class DepartmentDaoImpl implements DepartmentDaoEx {

    @Autowired
    private BaseDao<Department> baseDao;
}
