package com.lion.upms.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.lion.annotation.AuthorizationIgnore;
import com.lion.constant.SearchConstant;
import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.ResultData;
import com.lion.core.ResultDataState;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.JpqlParameter;
import com.lion.core.persistence.Validator;
import com.lion.exception.BusinessException;
import com.lion.upms.entity.user.User;
import com.lion.upms.entity.user.dto.UserAddDto;
import com.lion.upms.entity.user.dto.UserUpdataDto;
import com.lion.upms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/upms/user/console")
@Validated
public class UserController extends BaseControllerImpl implements BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    @AuthorizationIgnore
//    @PreAuthorize("hasAuthority('user_console_list1')")
//    @SentinelResource()
    public IResultData list(LionPage lionPage,String name) {
        JpqlParameter jpqlParameter = new JpqlParameter();
        jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_name",name);
        lionPage.setJpqlParameter(jpqlParameter);
        return (IResultData) userService.test(lionPage);
    }

    @AuthorizationIgnore
    @GetMapping("/info")
    public IResultData info(@NotNull(message = "id不能为空")Long id){
        return ResultData.instance().setData("user",userService.findById(id));
    }

    @AuthorizationIgnore
    @GetMapping("/exist")
    public IResultData checkUsernameIsExist(@NotBlank(message = "登陆账号不能为空!") String username){
        return ResultData.instance().setData("isExist",Objects.nonNull( userService.findUser(username)));
    }

    @AuthorizationIgnore
    @GetMapping("/email/exist")
    public IResultData checkEmailIsExist(@NotBlank(message = "邮箱不能为空！") String email,Long id){
        return ResultData.instance().setData("isExist",userService.checkEmailIsExist(email, id));
    }

    @AuthorizationIgnore
    @PostMapping("/add")
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

    @AuthorizationIgnore
    @DeleteMapping("/delete")
    public IResultData delete(@NotNull(message = "id不能为空") @RequestParam(value = "id",required = false) List<Long> id){
        id.forEach(i->{
            userService.deleteById(i);
        });
        ResultData resultData = ResultData.instance();
        return resultData;
    }

    @AuthorizationIgnore
    @PutMapping("/update")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) UserUpdataDto userUpdataDto){
        User user = userService.findById(userUpdataDto.getId());
        if (Objects.isNull(user)){
            new BusinessException("该用户不存在");
        }
        BeanUtil.copyProperties(userUpdataDto,user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        userService.update(user);
        return ResultData.instance();
    }

}
