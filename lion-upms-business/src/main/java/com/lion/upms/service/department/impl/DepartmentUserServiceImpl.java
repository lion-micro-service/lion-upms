package com.lion.upms.service.department.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.upms.dao.department.DepartmentUserDao;
import com.lion.upms.entity.department.DepartmentUser;
import com.lion.upms.entity.department.dto.AddDepartmentUserDto;
import com.lion.upms.service.department.DepartmentUserService;
import com.lion.utils.ValidatorExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: DepartmentUserServiceImpl
 * @description: 部门与用户关联service
 * @date 2020/8/15下午6:00
 */
@Service
public class DepartmentUserServiceImpl extends BaseServiceImpl<DepartmentUser> implements DepartmentUserService {

    @Autowired
    private DepartmentUserDao departmentUserDao;

    @Autowired
    private Validator validator;

    @Override
    public void saveUser(AddDepartmentUserDto addDepartmentUserDto) {
        if (Objects.nonNull(addDepartmentUserDto.getOldUserId()) && addDepartmentUserDto.getOldUserId().size()>0){
            departmentUserDao.deleteByDepartmentIdAndUserIdIn(addDepartmentUserDto.getDepartmentId(),addDepartmentUserDto.getOldUserId());
        }
        if (Objects.nonNull(addDepartmentUserDto.getNewUserId()) && addDepartmentUserDto.getNewUserId().size()>0){
            addDepartmentUserDto.getNewUserId().forEach(userId->{
                DepartmentUser departmentUser = new DepartmentUser();
                departmentUser.setDepartmentId(addDepartmentUserDto.getDepartmentId());
                departmentUser.setUserId(userId);
                ValidatorExceptionUtil.isViolation(validator.validate(departmentUser, com.lion.core.persistence.Validator.Insert.class));
                this.save(departmentUser);
            });
        }
    }

    @Override
    public List<DepartmentUser> findDepartmentUser(Long departmentId, List<Long> userId) {
        return departmentUserDao.findByDepartmentIdAndUserIdIn(departmentId, userId);
    }

    @Override
    public List<DepartmentUser> findNotInDepartmentUser(Long departmentId, List<Long> userId) {
        return departmentUserDao.findNotInDepartmentUser(departmentId, userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        departmentUserDao.deleteByUserId(userId);
    }

    @Override
    public void deleteByDepartmentId(Long departmentId) {
        departmentUserDao.deleteByDepartmentId(departmentId);
    }


}
