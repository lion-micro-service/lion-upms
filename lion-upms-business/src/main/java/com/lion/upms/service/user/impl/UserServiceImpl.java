package com.lion.upms.service.user.impl;

import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.ResultData;
import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.exception.BusinessException;
import com.lion.upms.dao.user.UserDao;
import com.lion.upms.entity.user.User;
import com.lion.upms.service.user.UserService;
import com.lion.utils.ValidatorExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

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

    @Override
//    @GlobalTransactional(name = "${spring.application.name}")
    public Page<User> test(LionPage lionPage) {
        return this.findNavigator(lionPage);
    }

    @Override
    public <S extends User> S save(S entity) {
        User user = entity;
        if (Objects.nonNull(user.getUsername()) && StringUtils.hasText(user.getUsername())){
            User User = findUser(user.getUsername());
            if (Objects.nonNull(user)){
                new BusinessException("该用户已存在");
            }
        }
        if (Objects.nonNull(user.getEmail()) && StringUtils.hasText(user.getEmail())){
            boolean isExist = checkEmailIsExist(user.getEmail(),null);
            if (isExist){
                new BusinessException("该邮箱已存在");
            }
        }
        ValidatorExceptionUtil.isViolation(validator.validate(user, com.lion.core.persistence.Validator.Insert.class));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return super.save(entity);
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
}
