package com.lion.upm.expose.role.impl;

import com.lion.core.service.impl.BaseExposeServiceImpl;
import com.lion.upm.expose.role.RoleDepartmentExposeService;
import com.lion.upm.expose.role.RoleExposeService;
import com.lion.upms.entity.role.RoleDepartment;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: RoleDepartmentExposeServiceImpl
 * @description: 角色与部门关联RPC暴露service
 * @date 2020/8/17上午11:28
 */
@DubboService(interfaceClass = RoleDepartmentExposeService.class)
public class RoleDepartmentExposeServiceImpl extends BaseExposeServiceImpl<RoleDepartment> implements RoleDepartmentExposeService {
}
