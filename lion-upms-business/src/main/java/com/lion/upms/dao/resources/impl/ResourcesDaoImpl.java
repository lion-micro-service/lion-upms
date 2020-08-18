package com.lion.upms.dao.resources.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.dao.resources.ResourcesDaoEx;
import com.lion.upms.entity.resources.Resources;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mr.liu
 * @title: ResourcesDaoImpl
 * @description: 资源复杂sql操作扩展
 * @date 2020/8/15下午5:33
 */
public class ResourcesDaoImpl implements ResourcesDaoEx {

    @Autowired
    private BaseDao<Resources> baseDao;
}
