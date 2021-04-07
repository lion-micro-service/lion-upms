package com.lion.upms.service.user.impl;

import com.lion.common.expose.parameter.ParameterExposeService;
import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.ResultData;
import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.exception.BusinessException;
import com.lion.upms.dao.user.UserDao;
import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.user.User;
import com.lion.upms.entity.user.dto.UserSearchDto;
import com.lion.upms.entity.user.vo.UserListVo;
import com.lion.upms.service.department.DepartmentService;
import com.lion.upms.service.department.DepartmentUserService;
import com.lion.upms.service.role.RoleService;
import com.lion.upms.service.role.RoleUserService;
import com.lion.upms.service.user.UserService;
import com.lion.utils.ValidatorExceptionUtil;
import io.seata.core.model.ResourceManager;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.annotation.Resource;
import javax.persistence.Id;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

//import io.seata.spring.annotation.GlobalTransactional;

/**
 * @description: 用户Service
 * @author: Mr.Liu
 * @create: 2020-01-17 10:23
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private Validator validator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentUserService departmentUserService;

    @Autowired
    private RoleUserService roleUserService;

    @DubboReference
    private ParameterExposeService parameterExposeService;


    @Override
    public Page<UserListVo> list(LionPage lionPage, UserSearchDto userSearchDto) {
        if (Objects.nonNull(userSearchDto.getDepartmentId()) && userSearchDto.getDepartmentId().size()>0){
            List<Long> listDepartmentId= new ArrayList<Long>();
            userSearchDto.getDepartmentId().forEach(id->{
                List<Department> list = departmentService.findAllChilder(id);
                list.forEach(department->{
                    listDepartmentId.add(department.getId());
                });
            });
            userSearchDto.getDepartmentId().addAll(listDepartmentId);
        }
        Page<UserListVo> page = userDao.list(lionPage, userSearchDto);
        List<UserListVo> list = page.getContent();
        list.forEach(userListVo -> {
            if (Objects.nonNull(userListVo.getDepartment())) {
                userListVo.getDepartment().setParentDepartment(departmentService.findTreeParentDepartment(userListVo.getDepartment().getParentId()));
            }
            userListVo.setRole(roleService.findByUserId(userListVo.getUser().getId()));
        });
        return page;
    }

    @Override
    public <S extends User> S save(S entity) {
        checkUserExist(entity);
//        ValidatorExceptionUtil.isViolation(validator.validate(entity, com.lion.core.persistence.Validator.Insert.class));
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return super.save(entity);
    }

    @Override
    public void update(User entity) {
        checkUserExist(entity);
        super.update(entity);
        if (Objects.isNull(entity.getHeadPortrait())){
            userDao.updateHeadPortrait(entity.getId(),null);
        }
    }

    @Override
    public User findUser(String username) {
        return userDao.findFirstByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findFirstByEmail(email);
    }

    @Override
    public boolean checkEmailIsExist(String email, Long id) {
        User user = findUserByEmail(email);
        if (Objects.isNull(user)){
            return false;
        }else {
            if (Objects.equals(user.getId(),id)){
                return false;
            }else {
                return true;
            }
        }
    }

    @Override
    public int updateHeadPortrait(Long id, Long headPortrait) {
        return userDao.updateHeadPortrait(id, headPortrait);
    }

    @Override
    @GlobalTransactional
    public void testXa() {
        parameterExposeService.testSeataTransactional("test","test111");
        new BusinessException("测试XA分布式事物回滚");
    }

    @Override
    public void deleteById(Serializable id) {
        userDao.deleteByIdAndUsernameNotIn(id, Arrays.asList(new String[]{"admin", "superadmin"}));
        departmentUserService.deleteByUserId(Long.valueOf(String.valueOf(id)));
        roleUserService.deleteByUserId(Long.valueOf(String.valueOf(id)));
    }

    /**
     * 检查用户是否存在
     * @param entity
     */
    private void checkUserExist(User entity){
        if (Objects.nonNull(entity.getUsername()) && StringUtils.hasText(entity.getUsername())){
            User user = findUser(entity.getUsername());
            if (Objects.nonNull(user) && Objects.nonNull(entity.getId())){
                if (!Objects.equals(user.getId(),entity.getId())){
                    new BusinessException("该用户已存在");
                }
            }
        }
        if (Objects.nonNull(entity.getEmail()) && StringUtils.hasText(entity.getEmail())){
            boolean isExist = checkEmailIsExist(entity.getEmail(),entity.getId());
            if (isExist){
                new BusinessException("该邮箱已存在");
            }
        }
    }
}
