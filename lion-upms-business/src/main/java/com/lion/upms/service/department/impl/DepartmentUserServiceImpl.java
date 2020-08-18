package com.lion.upms.service.department.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.department.DepartmentUserDao;
import com.lion.upms.entity.department.DepartmentUser;
import com.lion.upms.service.department.DepartmentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.liu
 * @title: DepartmentUserServiceImpl
 * @description: 部门与用户关联service
 * @date 2020/8/15下午6:00
 */
@Service
public class DepartmentUserServiceImpl extends BaseServiceImpl<DepartmentUser> implements DepartmentUserService {

    @Autowired
    private DepartmentUserDao departmentUserDao;
}
