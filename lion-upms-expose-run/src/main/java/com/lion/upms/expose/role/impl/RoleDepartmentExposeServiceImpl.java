package com.lion.upms.expose.role.impl;

import com.lion.upms.expose.role.RoleDepartmentExposeService;
import com.lion.upms.entity.role.RoleDepartment;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: RoleDepartmentExposeServiceImpl
 * @description: 角色与部门关联RPC暴露service
 * @date 2020/8/17上午11:28
 */
@DubboService(interfaceClass = RoleDepartmentExposeService.class)
public class RoleDepartmentExposeServiceImpl extends com.lion.core.service.impl.BaseServiceImpl<RoleDepartment> implements RoleDepartmentExposeService, com.lion.core.service.BaseService<RoleDepartment> {
}
