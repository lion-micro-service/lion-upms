package com.lion.upms.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.lion.common.entity.file.File;
import com.lion.common.expose.file.FileExposeService;
import com.lion.core.*;
import com.lion.core.common.enums.ResultDataState;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.user.User;
import com.lion.upms.entity.user.dto.UserAddDto;
import com.lion.upms.entity.user.dto.UserSearchDto;
import com.lion.upms.entity.user.dto.UserUpdataDto;
import com.lion.upms.entity.user.vo.UserListVo;
import com.lion.upms.entity.user.vo.UserVo;
import com.lion.upms.service.user.UserService;
import com.lion.utils.CurrentUserUtil;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user/console")
@Validated
@Api(tags = {"用户管理"})
public class UserController extends BaseControllerImpl implements BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DubboReference
    private FileExposeService fileExposeService;


    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('SYSTEM_SETTINGS_USER_LIST,SYSTEM_SETTINGS_ROLE_USER,SYSTEM_SETTINGS_DEPARTMENT_USER')")
    @ApiOperation(value = "用户列表",notes = "用户列表")
    public IPageResultData<List<UserListVo>> list(LionPage lionPage, UserSearchDto userSearchDto) {
        return (IPageResultData) userService.list(lionPage, userSearchDto);
    }

    @GetMapping("/current/user/details")
    @ApiOperation(value = "获取当前登陆用户详情",notes = "获取当前登陆用户详情")
    public IResultData<UserVo> currentUserDetails(){
        Long id = CurrentUserUtil.getCurrentUserId();
        return ResultData.instance().setData(convertVo(userService.findById(id)));
    }

    @ApiOperation(value = "修改当前登陆用户密码",notes = "修改当前登陆用户密码")
    @PutMapping("/current/user/passwod/update")
    public IResultData currentUserPasswordUpdate(@ApiParam(value ="新密码" ,name = "password",required = true,type = "string") @RequestParam  @NotBlank(message = "密码不能为空")String password){
        Long id = CurrentUserUtil.getCurrentUserId();
        User user = userService.findById(id);
        user.setPassword(passwordEncoder.encode(password));
        userService.update(user);
        return ResultData.instance();
    }

    @ApiOperation(value = "修改当前登陆用户头像",notes = "修改当前登陆用户头像")
    @PutMapping("/current/user/head/portrait/update")
    public IResultData currentUserHeadPortraitUpdate(Long headPortrait){
        Long id = CurrentUserUtil.getCurrentUserId();
        if (Objects.nonNull(id)){
            userService.updateHeadPortrait(id,headPortrait);
        }
        return ResultData.instance();
    }

    @GetMapping("/details")
    @ApiOperation(value = "获取用户详情",notes = "获取用户详情")
    public IResultData<UserVo> details(@NotNull(message = "id不能为空")Long id){
        User user = userService.findById(id);
        return ResultData.instance().setData(convertVo(user));
    }

    @GetMapping("/check/username/exist")
    @ApiOperation(value = "判断登陆张号是否存在",notes = "判断登陆张号是否存在")
    public IResultData<Boolean> checkUsernameIsExist(@NotBlank(message = "登陆账号不能为空!") String username){
        return ResultData.instance().setData(Objects.nonNull( userService.findUser(username)));
    }

    @GetMapping("/check/email/exist")
    @ApiOperation(value = "判断邮箱是否存在",notes = "判断邮箱是否存在")
    public IResultData<Boolean> checkEmailIsExist(@NotBlank(message = "邮箱不能为空！") String email,@ApiParam(value = "修改需要传id，新增则不需要传") Long id){
        return ResultData.instance().setData(userService.checkEmailIsExist(email, id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_USER_ADD')")
    @ApiOperation(value = "新增用户",notes = "新增用户")
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

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_USER_DELETE')")
    @ApiOperation(value = "删除用户",notes = "删除用户")
    public IResultData delete(@NotNull(message = "id不能为空") @RequestParam(value = "id",required = false)
                              @ApiParam(value = "数组(id=1&id=2)")  List<Long> id){
        id.forEach(i->{
            userService.deleteById(i);
        });
        ResultData resultData = ResultData.instance();
        return resultData;
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_USER_UPDATE')")
    @ApiOperation(value = "更新用户",notes = "更新用户")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) UserUpdataDto userUpdataDto){
        User user = new User();
        BeanUtil.copyProperties(userUpdataDto,user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        userService.update(user);
        return ResultData.instance();
    }

    /**
     * vo转换
     * @param user
     * @return
     */
    private UserVo convertVo(User user){
        UserVo userVo = new UserVo();
        if (Objects.nonNull(user)) {
            BeanUtils.copyProperties(user, userVo);
            if (Objects.nonNull(user.getHeadPortrait())){
                File file = fileExposeService.findById(user.getHeadPortrait());
                userVo.setHeadPortraitVo(file);
            }
        }
        return userVo;
    }
}
