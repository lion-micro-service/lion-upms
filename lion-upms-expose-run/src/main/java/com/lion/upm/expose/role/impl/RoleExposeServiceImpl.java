package com.lion.upm.expose.role.impl;

import com.lion.core.service.impl.BaseExposeServiceImpl;
import com.lion.upm.expose.role.RoleExposeService;
import com.lion.upm.expose.role.RoleResourcesExposeService;
import com.lion.upms.entity.role.Role;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: RoleExposeServiceImpl
 * @description: 角色rpc暴露service
 * @date 2020/8/17上午11:28
 */
@DubboService(interfaceClass = RoleExposeService.class)
public class RoleExposeServiceImpl extends BaseExposeServiceImpl<Role> implements RoleExposeService {
}
