package com.lion.upms.controller.resources;

import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;
import com.lion.upms.service.resources.ResourcesService;
import com.lion.upms.service.role.RoleResourcesService;
import com.lion.utils.CurrentUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/resources/console")
@Validated
@Api(tags = {"资源管理"})
public class ResourcesController extends BaseControllerImpl implements BaseController {

    @Autowired
    private ResourcesService  resourcesService;

    @GetMapping("/menu")
    @ApiOperation(value = "菜单栏",notes = "菜单栏")
    @PreAuthorize("isAuthenticated()")
    public IResultData<List<ResourcesTreeVo>> frontMenu(){
        Long userId = CurrentUserUtil.getCurrentUserId();
        List<Long> resourcesId = resourcesService.findAllResourcesId(userId);
        return ResultData.instance().setData(resourcesService.listTree(Scope.CONSOLE,resourcesId));
    }

    @GetMapping("/list/tree")
    @PreAuthorize("hasAnyAuthority('SYSTEM_SETTINGS_RESOURCES_LIST','SYSTEM_SETTINGS_ROLE_RESOURCES')")
    @ApiOperation(value = "资源树形列表",notes = "资源树形列表")
    public IResultData<List<ResourcesTreeVo>> listTree(@RequestParam(value = "scope",defaultValue = "CONSOLE") Scope scope){
        return ResultData.instance().setData(resourcesService.listTree(scope));
    }

    @GetMapping("/check/code/exist")
    @ApiOperation(value = "判断编码是否存在",notes = "判断编码是否存在")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Boolean> checkCodeIsExist(@NotBlank(message = "编码不能为空") String code,@ApiParam(value = "修改需要传id，新增则不需要传") Long id){
        return ResultData.instance().setData(resourcesService.checkCodeIsExist(code.trim().toUpperCase(), id));
    }

    @ApiOperation(value = "判断名称是否存在",notes = "判断名称是否存在")
    @GetMapping("/check/name/exist")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Boolean> checkNameIsExist(@NotBlank(message = "名称不能为空") String name,@ApiParam(value = "修改需要传id，新增则不需要传") Long id, @NotNull(message = "父节点id不能为空")Long parentId){
        return ResultData.instance().setData(resourcesService.checkNameIsExist(name, id,parentId));
    }

    @GetMapping("/check/url/exist")
    @ApiOperation(value = "判断url是否存在",notes = "判断url是否存在")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Boolean> checkUrlIsExist(@NotBlank(message = "url不能为空") String url,@ApiParam(value = "修改需要传id，新增则不需要传")  Long id){
        return ResultData.instance().setData(resourcesService.checkUrlIsExist(url, id));
    }

    @ApiOperation(value = "新建资源",notes = "新建资源")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_RESOURCES_ADD')")
    public IResultData add(@RequestBody @Validated({Validator.Insert.class}) Resources resources){
        resources.setCode(resources.getCode().trim().toUpperCase());
        resourcesService.checkIsExist(resources);
        this.resourcesService.save(resources);
        return ResultData.instance();
    }

    @ApiOperation(value = "修改资源",notes = "修改资源")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_RESOURCES_UPDATE')")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) Resources resources){
        resources.setCode(resources.getCode().trim().toUpperCase());
        resourcesService.checkIsExist(resources);
        this.resourcesService.update(resources);
        return ResultData.instance();
    }

    @ApiOperation(value = "根据id获取详情",notes = "根据id获取详情")
    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Resources> details(@NotNull(message = "id不能为空") Long id){
        Resources resources = this.resourcesService.findById(id);
        return ResultData.instance().setData(resources);
    }

    @ApiOperation(value = "删除资源",notes = "删除资源")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_RESOURCES_DELETE')")
    public IResultData delete(@NotNull(message = "id不能为空") @ApiParam(value = "数组(id=1&id=2)") Long id){
        resourcesService.delete(id);
        ResultData resultData = ResultData.instance();
        return resultData;
    }
}
