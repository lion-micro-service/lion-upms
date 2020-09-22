package com.lion.upms.controller.resources;

import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.resources.enums.Scope;
import com.lion.upms.service.resources.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

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

    /**
     * 左边菜单栏
     * @return
     */
    @GetMapping("/front/menu")
    public IResultData frontMenu(){
       return ResultData.instance().setData("menu",resourcesService.listTree(Scope.CONSOLE));
    }

    /**
     * 资源树形列表
     * @param scope
     * @return
     */
    @GetMapping("/list/tree")
    public IResultData listTree(@RequestParam(value = "scope",defaultValue = "CONSOLE") Scope scope){
        return ResultData.instance().setData("list",resourcesService.listTree(scope));
    }

    /**
     * 判断编码是否存在
     * @param code
     * @return
     */
    @GetMapping("/check/code/exist")
    public IResultData checkCodeIsExist(@NotBlank(message = "编码不能为空") String code){
        Resources resources = resourcesService.findByCode(code.trim().toUpperCase());
        return ResultData.instance().setData("isExist", Objects.nonNull(resources));
    }

    /**
     * 判断名称是否存在
     * @param name
     * @return
     */
    @GetMapping("/check/name/exist")
    public IResultData checkNameIsExist(@NotBlank(message = "名称不能为空") String name){
        Resources resources = resourcesService.findByName(name);
        return ResultData.instance().setData("isExist", Objects.nonNull(resources));
    }

    /**
     * 判断名称是否存在
     * @param url
     * @return
     */
    @GetMapping("/check/url/exist")
    public IResultData checkUrlIsExist(@NotBlank(message = "url不能为空") String url){
        Resources resources = resourcesService.findByUrl(url);
        return ResultData.instance().setData("isExist", Objects.nonNull(resources));
    }

    /**
     * 新建资源
     * @param resources
     * @return
     */
    @PostMapping("/add")
    public IResultData save(@RequestBody @Validated Resources resources){
        resources.setCode(resources.getCode().trim().toUpperCase());
        this.resourcesService.save(resources);
        return ResultData.instance();
    }
}
