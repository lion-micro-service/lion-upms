package com.lion.upms.expose.role.impl;

import com.lion.upms.expose.role.RoleResourcesExposeService;
import com.lion.upms.entity.role.RoleResources;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: RoleResourcesExposeServiceImpl
 * @description: 角色与资源关联RPC暴露service
 * @date 2020/8/17上午11:26
 */
@DubboService(interfaceClass = RoleResourcesExposeService.class)
public class RoleResourcesExposeServiceImpl extends com.lion.core.service.impl.BaseServiceImpl<RoleResources> implements RoleResourcesExposeService, com.lion.core.service.BaseService<RoleResources> {
}
