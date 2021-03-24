package com.lion.upms.expose.role.impl;

import com.lion.upms.expose.role.RoleExposeService;
import com.lion.upms.entity.role.Role;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: RoleExposeServiceImpl
 * @description: 角色rpc暴露service
 * @date 2020/8/17上午11:28
 */
@DubboService(interfaceClass = RoleExposeService.class)
public class RoleExposeServiceImpl extends com.lion.core.service.impl.BaseServiceImpl<Role> implements RoleExposeService, com.lion.core.service.BaseService<Role> {
}
