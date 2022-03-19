package com.lion.upms.dao.user;

import com.lion.upms.entity.user.User;
import com.lion.upms.entity.user.dto.UserSearchDto;
import com.lion.upms.entity.user.vo.UserListVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author mr.liu
 * @title: UserDaoEx
 * @description: 用户dao复杂sql扩展接口
 * @date 2020/8/15下午4:59
 */
public interface UserDaoEx {

    /**
     * 用户分页查询列表
     * @param pageable
     * @param userSearchDto
     * @return
     */
    public Page<UserListVo> list(Pageable pageable, UserSearchDto userSearchDto);

    /**
     * 根据id查询
     * @param in
     * @param notIn
     * @return
     */
    public List<User> find(List<Long> in, List<Long> notIn);
}
