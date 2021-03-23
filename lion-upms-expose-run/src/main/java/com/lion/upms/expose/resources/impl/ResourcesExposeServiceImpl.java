package com.lion.upms.expose.resources.impl;

import com.lion.core.service.impl.BaseExposeServiceImpl;
import com.lion.upms.expose.resources.ResourcesExposeService;
import com.lion.upms.entity.resources.Resources;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author mr.liu
 * @title: ResourcesExposeServiceImpl
 * @description: 资源rpc暴露service
 * @date 2020/8/17上午11:29
 */
@DubboService(interfaceClass = ResourcesExposeService.class)
public class ResourcesExposeServiceImpl extends BaseExposeServiceImpl<Resources> implements ResourcesExposeService {
}
