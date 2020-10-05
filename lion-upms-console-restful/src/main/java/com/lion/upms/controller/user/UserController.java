package com.lion.upms.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.lion.constant.SearchConstant;
import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.ResultData;
import com.lion.core.common.enums.ResultDataState;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.JpqlParameter;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.user.User;
import com.lion.upms.entity.user.dto.UserAddDto;
import com.lion.upms.entity.user.dto.UserSearchDto;
import com.lion.upms.entity.user.dto.UserUpdataDto;
import com.lion.upms.service.user.UserService;
import com.lion.utils.CurrentUser;
import com.lion.utils.CurrentUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: UserController
 * @description: 用户controller
 * @date 2020/8/15下午5:45
 */
@RestController
@RequestMapping("/upms/user/console")
@Validated
@Api(tags = "用户管理")
public class UserController extends BaseControllerImpl implements BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 列表
     * @param lionPage
     * @param userSearchDto
     * @return
     */
    @ApiOperation(value = "用户列表")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('SYSTEM_SETTINGS_USER_LIST,SYSTEM_SETTINGS_ROLE_USER,SYSTEM_SETTINGS_DEPARTMENT_USER')")
//    @SentinelResource()
    public IResultData list(@ApiIgnore LionPage lionPage, UserSearchDto userSearchDto) {
        return (IResultData) userService.list(lionPage, userSearchDto);
    }

    /**
     * 获取当前登陆用户详情
     * @return
     */
    @GetMapping("/current/user/details")
    public IResultData currentUserDetails(){
        Long id = CurrentUserUtil.getCurrentUserId();
        return ResultData.instance().setData("user",userService.findById(id));
    }

    /**
     * 修改当前登陆用户密码
     * @return
     */
    @PutMapping("/current/user/passwod/update")
    public IResultData currentUserPasswordUpdate(@NotBlank(message = "密码不能为空")String password){
        Long id = CurrentUserUtil.getCurrentUserId();
        User user = userService.findById(id);
        user.setPassword(passwordEncoder.encode(password));
        userService.update(user);
        return ResultData.instance();
    }

    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @GetMapping("/details")
    public IResultData details(@NotNull(message = "id不能为空")Long id){
        return ResultData.instance().setData("user",userService.findById(id));
    }

    /**
     * 判断登陆张号是否存在
     * @param username
     * @return
     */
    @GetMapping("/check/username/exist")
    public IResultData checkUsernameIsExist(@NotBlank(message = "登陆账号不能为空!") String username){
        return ResultData.instance().setData("isExist",Objects.nonNull( userService.findUser(username)));
    }

    /**
     * 判断邮箱是否存在
     * @param email
     * @param id
     * @return
     */
    @GetMapping("/check/email/exist")
    public IResultData checkEmailIsExist(@NotBlank(message = "邮箱不能为空！") String email,Long id){
        return ResultData.instance().setData("isExist",userService.checkEmailIsExist(email, id));
    }

    /**
     * 新增用户
     * @param userAddDto
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_USER_ADD')")
    public IResultData add( @RequestBody @Validated({Validator.Insert.class}) UserAddDto userAddDto){
        User user = new User();
        BeanUtil.copyProperties(userAddDto,user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        userService.save(user);
        ResultData resultData = ResultData.instance();
        if (Objects.isNull(user.getId())){
            resultData.setStatus(ResultDataState.ERROR.getKey());
            resultData.setMessage("保存失败！");
        }
        return resultData;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_USER_DELETE')")
    public IResultData delete(@NotNull(message = "id不能为空") @RequestParam(value = "id",required = false) List<Long> id){
        id.forEach(i->{
            userService.deleteById(i);
        });
        ResultData resultData = ResultData.instance();
        return resultData;
    }

    /**
     * 更新用户
     * @param userUpdataDto
     * @return
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_USER_UPDATE')")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) UserUpdataDto userUpdataDto){
        User user = new User();
        BeanUtil.copyProperties(userUpdataDto,user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        userService.update(user);
        return ResultData.instance();
    }
}
