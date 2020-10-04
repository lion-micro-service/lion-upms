package com.lion.upms.controller.resources;

import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.service.resources.ResourcesService;
import com.lion.upms.service.role.RoleResourcesService;
import com.lion.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @Autowired
    private RoleResourcesService roleResourcesService;

    /**
     * 左边菜单栏
     * @return
     */
    @GetMapping("/front/menu")
    public IResultData frontMenu(){
        Long userId = CurrentUserUtil.getCurrentUserId();
        List<Long> resourcesId = resourcesService.findAllResourcesId(userId);
        return ResultData.instance().setData("menu",resourcesService.listTree(Scope.CONSOLE,resourcesId));
    }

    /**
     * 资源树形列表
     * @param scope
     * @return
     */
    @GetMapping("/list/tree")
    @PreAuthorize("hasAnyAuthority('SYSTEM_SETTINGS_RESOURCES_LIST,SYSTEM_SETTINGS_ROLE_RESOURCES')")
    public IResultData listTree(@RequestParam(value = "scope",defaultValue = "CONSOLE") Scope scope){
        return ResultData.instance().setData("list",resourcesService.listTree(scope));
    }

    /**
     * 判断编码是否存在
     * @param code
     * @param id
     * @return
     */
    @GetMapping("/check/code/exist")
    public IResultData checkCodeIsExist(@NotBlank(message = "编码不能为空") String code, Long id){
        return ResultData.instance().setData("isExist", resourcesService.checkCodeIsExist(code.trim().toUpperCase(), id));
    }

    /**
     * 判断名称是否存在
     * @param name
     * @param id
     * @param parentId
     * @return
     */
    @GetMapping("/check/name/exist")
    public IResultData checkNameIsExist(@NotBlank(message = "名称不能为空") String name, Long id, @NotNull(message = "父节点id不能为空")Long parentId){
        return ResultData.instance().setData("isExist", resourcesService.checkNameIsExist(name, id,parentId));
    }

    /**
     * 判断名称是否存在
     * @param url
     * @param id
     * @return
     */
    @GetMapping("/check/url/exist")
    public IResultData checkUrlIsExist(@NotBlank(message = "url不能为空") String url, Long id){
        return ResultData.instance().setData("isExist", resourcesService.checkUrlIsExist(url, id));
    }

    /**
     * 新建资源
     * @param resources
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_RESOURCES_ADD')")
    public IResultData add(@RequestBody @Validated({Validator.Insert.class}) Resources resources){
        resources.setCode(resources.getCode().trim().toUpperCase());
        resourcesService.checkIsExist(resources);
        this.resourcesService.save(resources);
        return ResultData.instance();
    }

    /**
     * 修改资源
     * @param resources
     * @return
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_RESOURCES_UPDATE')")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) Resources resources){
        resources.setCode(resources.getCode().trim().toUpperCase());
        resourcesService.checkIsExist(resources);
        this.resourcesService.update(resources);
        return ResultData.instance();
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @GetMapping("/details")
    public IResultData details(@NotNull(message = "id不能为空") Long id){
        Resources resources = this.resourcesService.findById(id);
        return ResultData.instance().setData("resources",resources);
    }

    /**
     * 删除资源
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_RESOURCES_DELETE')")
    public IResultData delete(@NotNull(message = "id不能为空") Long id){
        resourcesService.delete(id);
        roleResourcesService.deleteByResourcesId(id);
        ResultData resultData = ResultData.instance();
        return resultData;
    }
}
