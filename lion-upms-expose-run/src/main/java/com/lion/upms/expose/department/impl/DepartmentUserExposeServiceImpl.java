package com.lion.upms.expose.department.impl;

import com.lion.upms.expose.department.DepartmentUserExposeService;
import com.lion.upms.entity.department.DepartmentUser;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: DepartmentUserExposeServiceImpl
 * @description: 部门与用户关联rpc暴露service
 * @date 2020/8/17上午11:42
 */
@DubboService(interfaceClass = DepartmentUserExposeService.class)
public class DepartmentUserExposeServiceImpl extends com.lion.core.service.impl.BaseServiceImpl<DepartmentUser> implements DepartmentUserExposeService, com.lion.core.service.BaseService<DepartmentUser> {
}
