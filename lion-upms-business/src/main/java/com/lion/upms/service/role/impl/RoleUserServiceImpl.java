package com.lion.upms.service.role.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.role.RoleUserDao;
import com.lion.upms.entity.role.RoleUser;
import com.lion.upms.entity.role.dto.AddRoleUserDto;
import com.lion.upms.service.role.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: RoleUserServiceImpl
 * @description: 角色与用户关联Service
 * @date 2020/8/15下午5:07
 */
@Service
public class RoleUserServiceImpl extends BaseServiceImpl<RoleUser> implements RoleUserService {

    @Autowired
    private RoleUserDao roleUserDao;

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void saveRoleUser(AddRoleUserDto addRoleUserDto) {
        if (Objects.nonNull(addRoleUserDto.getOldUserId()) && addRoleUserDto.getOldUserId().size()>0) {
            roleUserDao.deleteByRoleIdAndUserIdIn(addRoleUserDto.getRoleId(), addRoleUserDto.getOldUserId());
        }
        if (Objects.nonNull(addRoleUserDto.getNewUserId()) && addRoleUserDto.getNewUserId().size()>0) {
            LinkedHashSet<Long> set = new LinkedHashSet<Long>(addRoleUserDto.getNewUserId());//去重
            set.forEach(userId -> {
                RoleUser roleUser = new RoleUser();
                roleUser.setRoleId(addRoleUserDto.getRoleId());
                roleUser.setUserId(userId);
                this.save(roleUser);
            });
        }
    }

    @Override
    public List<RoleUser> findRoleUser(Long roleId, List<Long> userId) {
        return roleUserDao.findByRoleIdAndUserIdIn(roleId, userId);
    }

}
