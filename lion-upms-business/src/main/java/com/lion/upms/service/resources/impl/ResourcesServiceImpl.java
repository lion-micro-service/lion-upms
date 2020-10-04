package com.lion.upms.service.resources.impl;

import com.lion.core.LionPage;
import com.lion.core.common.enums.State;
import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.exception.BusinessException;
import com.lion.upms.dao.resources.ResourcesDao;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;
import com.lion.upms.service.resources.ResourcesService;
import com.lion.utils.CurrentUserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
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
        return listTree(scope,null);
    }

    @Override
    public List<ResourcesTreeVo> listTree(Scope scope, List<Long> resourcesId) {
        List<Resources> list = new ArrayList<Resources>();
        if (Objects.isNull(resourcesId)){
           list = resourcesDao.findByParentIdAndStateAndScopeOrderBySort(0L, State.NORMAL, scope);
        }else if (resourcesId.size()>0){
            list = resourcesDao.findByParentIdAndStateAndScopeAndIdInOrderBySort(0L, State.NORMAL, scope,resourcesId);
        }
        List<ResourcesTreeVo> retuenList = convertVo(list);
        if (Objects.nonNull(retuenList)) {
            retuenList.forEach(resources -> {
                resources.setChildren(listTree(resources.getId(), 0,resourcesId));
            });
        }
        return retuenList;
    }

    @Override
    public Resources findByCode(String code) {
        return resourcesDao.findFirstByCode(code);
    }

    @Override
    public Resources findByName(String name) {
        return resourcesDao.findFirstByName(name);
    }

    @Override
    public Resources findByUrl(String url) {
        return resourcesDao.findFirstByUrl(url);
    }

    @Override
    public Boolean checkCodeIsExist(String code, Long id) {
        if (!StringUtils.hasText(code)){
            return false;
        }
        Resources resources = resourcesDao.findFirstByCode(code);
        if (Objects.isNull(resources)){
            return false;
        }
        if (Objects.nonNull(id) && resources.getId().equals(id)){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkNameIsExist(String name, Long id, Long parentId) {
        if (!StringUtils.hasText(name)){
            return false;
        }
        Resources resources = resourcesDao.findFirstByNameAndParentId(name,parentId);
        if (Objects.isNull(resources)){
            return false;
        }
        if (Objects.nonNull(id) && resources.getId().equals(id)){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkUrlIsExist(String url, Long id) {
        if (!StringUtils.hasText(url)){
            return false;
        }
        Resources resources = resourcesDao.findFirstByUrl(url);
        if (Objects.isNull(resources)){
            return false;
        }
        if (Objects.nonNull(id) && resources.getId().equals(id)){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkCodeIsExist(String code) {
        return checkCodeIsExist(code, null);
    }

    @Override
    public Boolean checkNameIsExist(String name, Long parentId) {
        return checkNameIsExist(name, null, parentId);
    }

    @Override
    public Boolean checkUrlIsExist(String url) {
        return checkUrlIsExist(url, null);
    }

    @Override
    public void checkIsExist(Resources resources) {
        if (checkCodeIsExist(resources.getCode(), resources.getId())){
            new BusinessException("编码已存在");
        }
        if (checkNameIsExist(resources.getName(), resources.getId(),resources.getParentId())){
            new BusinessException("名称在同节点已存在");
        }
        if (checkUrlIsExist(resources.getUrl(), resources.getId())){
            new BusinessException("url已存在");
        }
    }

    @Override
    public Boolean delete(Long id) {
        Resources resources = this.findById(id);
        if (Objects.nonNull(resources) && resources.getIsDefault()){
            new BusinessException("该资源不能删除（默认资源）");
        }
        if (Objects.isNull(resources)){
            return false;
        }
        ResourcesTreeVo resourcesTreeVo = new ResourcesTreeVo();
        BeanUtils.copyProperties(resources, resourcesTreeVo);
        resourcesTreeVo.setChildren(listTree(resources.getId(), 0));
        List<ResourcesTreeVo> list = new ArrayList<ResourcesTreeVo>();
        list.add(resourcesTreeVo);
        delete(list);
        return true;
    }

    @Override
    public List<Resources> findAllParentResources(Long id) {
        List<Resources> list = new ArrayList<Resources>();
        List<Resources> resourceslist = resourcesDao.findParentResourcesById(id, new LionPage(0,1, Sort.unsorted()));
        list.addAll(resourceslist);
        if (Objects.nonNull(resourceslist) && resourceslist.size()>0) {
            resourceslist.forEach(resources -> {
                findParentResources(resources.getId(), list);
            });
        }
        return list;
    }

    @Override
    public List<Long> findAllResourcesId(Long userId) {
        List<Long> list = new ArrayList<Long>();
        List<Resources> listResources = findAllResources(userId);
        listResources.forEach(resources -> {
            list.add(resources.getId());
        });
        return list;
    }

    @Override
    public List<Resources> findAllResources(Long userId) {
        if (Objects.isNull(userId)){
            return Collections.emptyList();
        }
        return resourcesDao.findAllResources(userId);
    }

    /**
     *  获取父节点
     * @param id
     * @param list
     */
    private void findParentResources(Long id, List<Resources> list){
        List<Resources> resourceslist = resourcesDao.findParentResourcesById(id, new LionPage(0,1, Sort.unsorted()));
        if (Objects.nonNull(resourceslist) && resourceslist.size()>0){
            list.addAll(resourceslist);
            resourceslist.forEach(resources -> {
                findParentResources(resources.getParentId(),list);
            });
        }
    }

    /**
     * 删除资源
     * @param list
     */
    private void delete(List<ResourcesTreeVo> list){
        list.forEach(r->{
            this.deleteById(r.getId());
            if (Objects.nonNull(r.getChildren()) && r.getChildren().size()>0){
                delete(r.getChildren());
            }
            //todo 删除角色所关联的资源
        });
    }

    /**
     * 获取所有子节点
     * @param parentId
     * @param hierarchy 最深递归深度，防止栈内存溢出
     * @param resourcesId
     * @return
     */
    private List<ResourcesTreeVo> listTree(Long parentId,int hierarchy, List<Long> resourcesId) {
        if (hierarchy == 10){
            return null;
        }
        if (Objects.isNull(parentId)){
            return null;
        }
        List<Resources> childList = listTree(parentId,resourcesId);
        List<ResourcesTreeVo> retuenList = convertVo(childList);
        if (Objects.nonNull(retuenList)) {
            retuenList.forEach(resources -> {
                int finalHierarchy = hierarchy;
                finalHierarchy++;
                resources.setChildren(listTree(resources.getId(), finalHierarchy,resourcesId));
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
        return listTree(parentId, hierarchy, null);
    }

    /**
     * 根据父节点ID获取子资源
     * @param parentId
     * @return
     */
    private List<Resources> listTree(Long parentId) {
        return listTree(parentId,null);
    }

    /**
     * 根据父节点ID和资源id获取子资源
     * @param parentId
     * @param resourcesId
     * @return
     */
    private List<Resources> listTree(Long parentId,List<Long> resourcesId) {
        if (Objects.isNull(resourcesId)) {
            return resourcesDao.findByParentIdAndStateOrderBySort(parentId, State.NORMAL);
        }else if (resourcesId.size()>0){
            return resourcesDao.findByParentIdAndStateAndIdInOrderBySort(parentId, State.NORMAL,resourcesId);
        }
        return Collections.emptyList();
    }

    /**
     * 转换成VO
     * @param list
     * @return
     */
    private List<ResourcesTreeVo> convertVo(List<Resources> list){
        List<ResourcesTreeVo> returnList = new ArrayList<ResourcesTreeVo>();
        list.forEach(resources -> {
            ResourcesTreeVo  resourcesTreeVo = new ResourcesTreeVo();
            BeanUtils.copyProperties(resources,resourcesTreeVo);
            returnList.add(resourcesTreeVo);
        });
        return returnList.size()>0?returnList:null;
    }

}
