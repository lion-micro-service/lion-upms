package com.lion.upms.service.role.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.exception.BusinessException;
import com.lion.upms.dao.role.RoleDao;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;
import com.lion.upms.entity.role.Role;
import com.lion.upms.entity.role.vo.RoleResourcesTreeVo;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.resources.ResourcesService;
import com.lion.upms.service.role.RoleService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: RoleServiceImpl
 * @description: 角色service
 * @date 2020/8/15下午5:09
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ResourcesService resourcesService;

    @Override
    public Role findByName(String name) {
        return roleDao.findFirstByName(name);
    }

    @Override
    public Role findByCode(String code) {
        return roleDao.findFirstByCode(code);
    }

    @Override
    public void checkIsExist(Role role) {
        if (Objects.isNull(role.getName())){
            return;
        }
        if (checkNameIsExist(role.getName(), role.getId())){
            new BusinessException("名称已存在");
        }
        if (checkCodeIsExist(role.getCode(), role.getId())){
            new BusinessException("编码已存在");
        }
    }

    @Override
    public boolean checkNameIsExist(String name, Long id) {
        Role role = roleDao.findFirstByName(name);
        if (Objects.isNull(role)){
            return false;
        }
        if (Objects.nonNull(id) && role.getId().equals(id)){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCodeIsExist(String code, Long id) {
        Role role = roleDao.findFirstByCode(code);
        if (Objects.isNull(role)){
            return false;
        }
        if (Objects.nonNull(id) && role.getId().equals(id)){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkNameIsExist(String name) {
        return checkNameIsExist(name,null);
    }

    @Override
    public boolean checkCodeIsExist(String code) {
        return checkCodeIsExist(code,null);
    }

    @Override
    public List<RoleResourcesTreeVo> roleResources(Scope scope) {
        List<ResourcesTreeVo> resourcesTreeList = resourcesService.listTree(scope);
        List<RoleResourcesTreeVo> roleResourcesTreeList = new ArrayList<RoleResourcesTreeVo>();
        resourcesTreeList.forEach(r->{
            roleResourcesTreeList.add(convertVo(r));
        });
        return roleResourcesTreeList;
    }

    @Override
    public <S extends Role> S save(S entity) {
        this.checkIsExist(entity);
        return super.save(entity);
    }

    @Override
    public void update(Role entity) {
        this.checkIsExist(entity);
        super.update(entity);
    }

    /**
     * 设置子节点
     * @param list
     * @return
     */
    private List<RoleResourcesTreeVo> roleResourcesTreeVoChilder(List<ResourcesTreeVo> list){
        List<RoleResourcesTreeVo> roleResourcesTreeList = new ArrayList<RoleResourcesTreeVo>();
        list.forEach(r->{
            roleResourcesTreeList.add(convertVo(r));
        });
        return roleResourcesTreeList.size()>0?roleResourcesTreeList:null;
    }

    /**
     * bean VO 转换
     * @param resourcesTreeVo
     * @return
     */
    private RoleResourcesTreeVo convertVo(ResourcesTreeVo resourcesTreeVo){
        RoleResourcesTreeVo roleResourcesTreeVo = new RoleResourcesTreeVo();
        roleResourcesTreeVo.setTitle(resourcesTreeVo.getName());
        roleResourcesTreeVo.setKey(resourcesTreeVo.getId());
        if (Objects.nonNull(resourcesTreeVo.getChildren()) && resourcesTreeVo.getChildren().size()>0) {
            roleResourcesTreeVo.setChildren(roleResourcesTreeVoChilder(resourcesTreeVo.getChildren()));
        }
        return roleResourcesTreeVo;
    }
}
