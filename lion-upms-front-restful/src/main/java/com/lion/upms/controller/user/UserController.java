package com.lion.upms.controller.user;

import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.upms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mr.liu
 * @title: UserController
 * @description: 用户controller
 * @date 2020/8/15下午5:45
 */
@RestController
@RequestMapping("/upms/user/front")
public class UserController extends BaseControllerImpl implements BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
//    @AuthorizationIgnore
//    @PreAuthorize("hasAuthority('user_console_list1')")
//    @SentinelResource()
    public IResultData list(LionPage lionPage) {
        IResultData resultData = (IResultData) userService.navigator(lionPage);
        return resultData;
    }

}
