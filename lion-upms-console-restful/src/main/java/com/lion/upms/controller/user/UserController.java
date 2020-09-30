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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
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

    /**
     * 列表
     * @param lionPage
     * @param userSearchDto
     * @return
     */
    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('user_console_list1')")
//    @SentinelResource()
    public IResultData list(LionPage lionPage, UserSearchDto userSearchDto) {
        JpqlParameter jpqlParameter = new JpqlParameter();
        if (StringUtils.hasText(userSearchDto.getName())){
            jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_name",userSearchDto.getName());
        }
        jpqlParameter.setSearchParameter(SearchConstant.NOT_IN+"_username",new String[]{"admin","superadmin"});
        jpqlParameter.setSortParameter("createDateTime", Sort.Direction.DESC);
        lionPage.setJpqlParameter(jpqlParameter);
        return (IResultData) userService.list(lionPage, userSearchDto);
    }

    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @GetMapping("/info")
    public IResultData info(@NotNull(message = "id不能为空")Long id){
        return ResultData.instance().setData("user",userService.findById(id));
    }

    /**
     * 判断登陆张号是否存在
     * @param username
     * @return
     */
    @GetMapping("/exist")
    public IResultData checkUsernameIsExist(@NotBlank(message = "登陆账号不能为空!") String username){
        return ResultData.instance().setData("isExist",Objects.nonNull( userService.findUser(username)));
    }

    /**
     * 判断邮箱是否存在
     * @param email
     * @param id
     * @return
     */
    @GetMapping("/email/exist")
    public IResultData checkEmailIsExist(@NotBlank(message = "邮箱不能为空！") String email,Long id){
        return ResultData.instance().setData("isExist",userService.checkEmailIsExist(email, id));
    }

    /**
     * 新增用户
     * @param userAddDto
     * @return
     */
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

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
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
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) UserUpdataDto userUpdataDto){
        User user = new User();
        BeanUtil.copyProperties(userUpdataDto,user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        userService.update(user);
        return ResultData.instance();
    }
}
