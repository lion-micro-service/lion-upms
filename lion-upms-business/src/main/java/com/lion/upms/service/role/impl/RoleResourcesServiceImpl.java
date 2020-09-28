package com.lion.upms.service.role.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.role.RoleResourcesDao;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.role.RoleResources;
import com.lion.upms.entity.role.dto.AddRoleResourcesdDto;
import com.lion.upms.service.resources.ResourcesService;
import com.lion.upms.service.role.RoleResourcesService;
import com.lion.utils.ValidatorExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Id;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: RoleResourcesServiceImpl
 * @description: 角色与资源关联Service
 * @date 2020/8/15下午5:12
 */
@Service
public class RoleResourcesServiceImpl extends BaseServiceImpl<RoleResources> implements RoleResourcesService {

    @Autowired
    private RoleResourcesDao roleResourcesDao;

    @Autowired
    private ResourcesService resourcesService;

    @Resource
    private Validator validator;

    @Override
    public void saveRoleResources(AddRoleResourcesdDto addRoleResourcesdDto) {
        roleResourcesDao.deleteByRoleId(addRoleResourcesdDto.getRoleId());
        List<RoleResources> list = new ArrayList<RoleResources>();
        if (Objects.nonNull(addRoleResourcesdDto.getResourcesId())){
            addRoleResourcesdDto.getResourcesId().forEach(resourcesId->{
                RoleResources roleResources =newRoleResources(addRoleResourcesdDto.getRoleId(),resourcesId);
                List<Resources> resourcesList = resourcesService.getAllParentResources(resourcesId);
                resourcesList.forEach(resources -> {
                    if (!addRoleResourcesdDto.getResourcesId().contains(resources.getId())){
                        RoleResources temp = newRoleResources(addRoleResourcesdDto.getRoleId(),resources.getId());
                        temp.setIsChecked(false);
                        list.add(temp);
                    }
                });
                list.add(roleResources);
            });
        }
        this.saveAll(list);
    }

    @Override
    public List<RoleResources> getAllRoleResources(Long roleId) {
        return roleResourcesDao.findAllByRoleId(roleId);
    }

    /**
     * 初始化角色权限（资源）
     * @param roleId
     * @param resourcesId
     * @return
     */
    private RoleResources newRoleResources(Long roleId,Long resourcesId){
        RoleResources roleResources = new RoleResources();
        roleResources.setResourcesId(resourcesId);
        roleResources.setRoleId(roleId);
        ValidatorExceptionUtil.isViolation(validator.validate(roleResources, com.lion.core.persistence.Validator.Insert.class));
        return roleResources;
    }
}
