package com.lion.upms.service.role.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.role.RoleDepartmentDao;
import com.lion.upms.entity.role.RoleDepartment;
import com.lion.upms.service.role.RoleDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.liu
 * @title: RoleDepartmentServiceImpl
 * @description: 角色与部门关联Service
 * @date 2020/8/15下午5:12
 */
@Service
public class RoleDepartmentServiceImpl extends BaseServiceImpl<RoleDepartment> implements RoleDepartmentService {

    @Autowired
    private RoleDepartmentDao roleDepartmentDao;
}
