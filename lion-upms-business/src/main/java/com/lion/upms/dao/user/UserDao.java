package com.lion.upms.dao.user;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author mr.liu
 * @title: UserDao
 * @description: 用户dao
 * @date 2020/8/15下午4:02
 */
public interface UserDao extends BaseDao<User> ,UserDaoEx {

    /**
     * 根据用户名(登陆账号)获取用户
     * @param username
     * @return
     */
    User findFirstByUsername(String username);

    /**
     * 根据email查找用户
     * @param email
     * @return
     */
    User findFirstByEmail(String email);

    /**
     * 根据id删除(不能删除admin和superadmin)
     * @param id
     * @param usernames
     * @return
     */
    int deleteByIdAndUsernameNotIn(Serializable id, Collection<String> usernames);

    /**
     * 更新用户头像（因使用本框架自身update更新entity不会操作null字段）
     * @param id
     * @param headPortrait
     * @return
     */
    @Transactional
    @Modifying
    @Query(" update User set headPortrait =:headPortrait where id =:id ")
    int updateHeadPortrait(@Param("id") Long id, @Param("headPortrait") Long headPortrait);
}
