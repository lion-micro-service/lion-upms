package com.lion.upms.service.user;

import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.service.BaseService;
import com.lion.upms.entity.user.User;
import com.lion.upms.entity.user.dto.UserSearchDto;
import com.lion.upms.entity.user.vo.UserListVo;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

/**
 * @description: 用户Service
 * @author: Mr.Liu
 * @create: 2020-01-15 14:52
 */
@Validated
public interface UserService extends BaseService<User> {

    /**
     * 分页列表
     * @param lionPage
     * @param userSearchDto
     * @return
     */
    public Page<UserListVo> list(LionPage lionPage, UserSearchDto userSearchDto);

    /**
     * 根据用户名获取用户
     * @param username 登陆用户名
     * @return
     */
    Optional<User> findUser(@NotBlank(message = "用户登陆账号不能为空") String username);

    /**
     * 根据email查找用户
     * @param email
     * @return
     */
    Optional<User> findUserByEmail(@NotBlank(message = "邮箱不能为空")String email);

    /**
     * 检查邮箱是否已存在
     * @param email
     * @param id
     * @return
     */
    boolean checkEmailIsExist(@NotBlank(message = "邮箱不能为空")String email,Long id);

    /**
     * 修改用户头像
     * @param id
     * @param headPortrait
     * @return
     */
    int updateHeadPortrait(Long id, Long headPortrait);

    void testXa();
}
