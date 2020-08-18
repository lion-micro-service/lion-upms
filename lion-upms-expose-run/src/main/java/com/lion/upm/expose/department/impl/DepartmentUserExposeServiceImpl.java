package com.lion.upm.expose.department.impl;

import com.lion.core.service.impl.BaseExposeServiceImpl;
import com.lion.upm.expose.department.DepartmentUserExposeService;
import com.lion.upms.entity.department.DepartmentUser;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: DepartmentUserExposeServiceImpl
 * @description: 部门与用户关联rpc暴露service
 * @date 2020/8/17上午11:42
 */
@DubboService(interfaceClass = DepartmentUserExposeService.class)
public class DepartmentUserExposeServiceImpl extends BaseExposeServiceImpl<DepartmentUser> implements DepartmentUserExposeService {
}