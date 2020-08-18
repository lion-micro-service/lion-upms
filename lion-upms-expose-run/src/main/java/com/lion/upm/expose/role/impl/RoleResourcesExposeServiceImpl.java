package com.lion.upm.expose.role.impl;

import com.lion.core.service.impl.BaseExposeServiceImpl;
import com.lion.upm.expose.role.RoleResourcesExposeService;
import com.lion.upms.entity.role.RoleResources;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: RoleResourcesExposeServiceImpl
 * @description: 角色与资源关联RPC暴露service
 * @date 2020/8/17上午11:26
 */
@DubboService(interfaceClass = RoleResourcesExposeService.class)
public class RoleResourcesExposeServiceImpl extends BaseExposeServiceImpl<RoleResources> implements RoleResourcesExposeService {
}
