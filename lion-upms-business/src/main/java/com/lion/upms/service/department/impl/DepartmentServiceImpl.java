package com.lion.upms.service.department.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.department.DepartmentDao;
import com.lion.upms.entity.department.Department;
import com.lion.upms.service.department.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.liu
 * @title: DepartmentServiceImpl
 * @description: 部门service
 * @date 2020/8/15下午5:38
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department> implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;
}
