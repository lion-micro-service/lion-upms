package com.lion.upms.expose.department.impl;

import com.lion.upms.expose.department.DepartmentExposeService;
import com.lion.upms.entity.department.Department;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: DepartmentExposeServiceImpl
 * @description: 部门rpc暴露service
 * @date 2020/8/17上午11:41
 */
@DubboService(interfaceClass = DepartmentExposeService.class)
public class DepartmentExposeServiceImpl extends com.lion.core.service.impl.BaseServiceImpl<Department> implements DepartmentExposeService, com.lion.core.service.BaseService<Department> {
}
