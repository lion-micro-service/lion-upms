package com.lion.upms.controller.resources;

import com.lion.annotation.AuthorizationIgnore;
import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.upms.entity.resources.enums.Scope;
import com.lion.upms.service.resources.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mr.liu
 * @title: ResourcesController
 * @description: 资源controller
 * @date 2020/8/15下午5:54
 */
@RestController
@RequestMapping("/upms/resources/console")
@Validated
public class ResourcesController extends BaseControllerImpl implements BaseController {

    @Autowired
    private ResourcesService  resourcesService;

    @GetMapping("/front/menu")
    @AuthorizationIgnore
    public IResultData frontMenu(){
       return ResultData.instance().setData("menu",resourcesService.listTree(Scope.CONSOLE));
    }

}
