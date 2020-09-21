package com.lion.upms.service.resources.impl;

import com.lion.core.common.enums.State;
import com.lion.core.service.BaseService;
import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.resources.ResourcesDao;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.resources.enums.Scope;
import com.lion.upms.entity.resources.enums.Type;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;
import com.lion.upms.service.resources.ResourcesService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.log.LogInputStream;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<ResourcesTreeVo> listTree(Scope scope) {
        List<Resources> list = resourcesDao.findByParentIdAndStateAndScope(0L, State.NORMAL, scope);
        List<ResourcesTreeVo> retuenList = convertVo(list);
        if (Objects.nonNull(retuenList)) {
            retuenList.forEach(resources -> {
                resources.setChildren(listTree(resources.getId(), 0));
            });
        }
        return retuenList;
    }

    /**
     * 获取所有子节点
     * @param parentId
     * @param hierarchy 最深递归深度，防止栈内存溢出
     * @return
     */
    private List<ResourcesTreeVo> listTree(Long parentId,int hierarchy) {
        if (hierarchy == 10){
            return null;
        }
        if (Objects.isNull(parentId)){
            return null;
        }
        List<Resources> childList = listTree(parentId);
        List<ResourcesTreeVo> retuenList = convertVo(childList);
        if (Objects.nonNull(retuenList)) {
            retuenList.forEach(resources -> {
                int finalHierarchy = hierarchy;
                finalHierarchy++;
                resources.setChildren(listTree(resources.getId(), finalHierarchy));
            });
        }
        return retuenList;
    }

    /**
     * 根据父节点ID获取子资源
     * @param parentId
     * @return
     */
    private List<Resources> listTree(Long parentId) {
        return resourcesDao.findByParentIdAndState(parentId, State.NORMAL);
    }

    /**
     * 转换成VO
     * @param list
     * @return
     */
    private List<ResourcesTreeVo> convertVo(List<Resources> list){
        List<ResourcesTreeVo> retuenList = new ArrayList<ResourcesTreeVo>();
        list.forEach(resources -> {
            ResourcesTreeVo  resourcesTreeVo = new ResourcesTreeVo();
            BeanUtils.copyProperties(resources,resourcesTreeVo);
            retuenList.add(resourcesTreeVo);
        });
        return retuenList.size()>0?retuenList:null;
    }

}
