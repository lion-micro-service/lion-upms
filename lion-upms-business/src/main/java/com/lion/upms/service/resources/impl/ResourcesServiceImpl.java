package com.lion.upms.service.resources.impl;

import com.lion.core.service.BaseService;
import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.resources.ResourcesDao;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.service.resources.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.liu
 * @title: ResourcesServiceImpl
 * @description: 资源service
 * @date 2020/8/15下午5:25
 */
@Service
public class ResourcesServiceImpl extends BaseServiceImpl<Resources> implements ResourcesService {

    @Autowired
    private ResourcesDao resourcesDao;
}
