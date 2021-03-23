package com.lion.upms.expose.role.impl;

import com.lion.core.service.impl.BaseExposeServiceImpl;
import com.lion.upms.expose.role.RoleUserExposeService;
import com.lion.upms.entity.role.RoleUser;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: RoleUserExposeServiceImpl
 * @description: 角色与用户关联rpc暴露service
 * @date 2020/8/17上午11:24
 */
@DubboService(interfaceClass = RoleUserExposeService.class)
public class RoleUserExposeServiceImpl extends BaseExposeServiceImpl<RoleUser> implements RoleUserExposeService {
}
