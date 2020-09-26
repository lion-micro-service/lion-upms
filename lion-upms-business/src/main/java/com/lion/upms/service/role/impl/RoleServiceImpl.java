package com.lion.upms.service.role.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.exception.BusinessException;
import com.lion.upms.dao.role.RoleDao;
import com.lion.upms.entity.role.Role;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.role.RoleService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
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
    public <S extends Role> S save(S entity) {
        this.checkIsExist(entity);
        return super.save(entity);
    }
}
