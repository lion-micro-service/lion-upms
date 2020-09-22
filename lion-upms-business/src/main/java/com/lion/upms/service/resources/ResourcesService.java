package com.lion.upms.service.resources;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.resources.enums.Scope;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;

import java.util.List;

/**
 * @author mr.liu
 * @title: resources
 * @description: 资源service
 * @date 2020/8/15下午5:24
 */
public interface ResourcesService extends BaseService<Resources> {

    /**
     * 资源树形结构
     * @param scope
     * @return
     */
    public List<ResourcesTreeVo> listTree(Scope scope);

    /**
     * 根据编码查询资源
     * @param code
     * @return
     */
    public Resources find(String code);
}
