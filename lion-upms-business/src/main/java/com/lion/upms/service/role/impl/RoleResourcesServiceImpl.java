package com.lion.upms.service.role.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.role.RoleResourcesDao;
import com.lion.upms.entity.role.RoleResources;
import com.lion.upms.service.role.RoleResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.liu
 * @title: RoleResourcesServiceImpl
 * @description: 角色与资源关联Service
 * @date 2020/8/15下午5:12
 */
@Service
public class RoleResourcesServiceImpl extends BaseServiceImpl<RoleResources> implements RoleResourcesService {

    @Autowired
    private RoleResourcesDao roleResourcesDao;
}
