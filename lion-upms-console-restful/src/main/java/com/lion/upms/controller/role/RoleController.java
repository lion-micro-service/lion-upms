package com.lion.upms.controller.role;

import com.lion.constant.SearchConstant;
import com.lion.core.*;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.JpqlParameter;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.resources.enums.Type;
import com.lion.upms.entity.role.Role;
import com.lion.upms.entity.role.RoleResources;
import com.lion.upms.entity.role.RoleUser;
import com.lion.upms.entity.role.dto.AddRoleResourcesdDto;
import com.lion.upms.entity.role.dto.AddRoleUserDto;
import com.lion.upms.entity.role.vo.RoleResourcesTreeVo;
import com.lion.upms.service.resources.ResourcesService;
import com.lion.upms.service.role.RoleDepartmentService;
import com.lion.upms.service.role.RoleResourcesService;
import com.lion.upms.service.role.RoleService;
import com.lion.upms.service.role.RoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author mr.liu
 * @title: RoleController
 * @description: 角色Controller
 * @date 2020/8/15下午5:51
 */
@RestController
@RequestMapping("/role/console")
@Validated
@Api(tags = {"角色管理"})
public class RoleController extends BaseControllerImpl implements BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private RoleResourcesService roleResourcesService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private ResourcesService resourcesService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_LIST')")
    @ApiOperation(value = "角色列表",notes = "角色列表")
    public IPageResultData<List<Role>> list(String name, String code, @RequestParam(name = "scope",defaultValue = "CONSOLE") Scope scope, LionPage lionPage){
        JpqlParameter jpqlParameter = new JpqlParameter();
        if (StringUtils.hasText(name)){
            jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_name",name);
        }
        if (StringUtils.hasText(code)){
            jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_code",code);
        }
        jpqlParameter.setSearchParameter(SearchConstant.EQUAL+"_scope",scope);
        jpqlParameter.setSortParameter("createDateTime", Sort.Direction.DESC);
        lionPage.setJpqlParameter(jpqlParameter);
        return (IPageResultData) roleService.findNavigator(lionPage);
    }

    @ApiOperation(value = "新增角色",notes = "新增角色")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_ADD')")
    public IResultData add(@RequestBody @Validated({Validator.Insert.class}) Role role){
        this.roleService.save(role);
        return ResultData.instance();
    }

    @ApiOperation(value = "修改角色",notes = "修改角色")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_UPDATE')")
    public IResultData update(@RequestBody @Validated({Validator.Update.class})  Role role) {
        this.roleService.update(role);
        return ResultData.instance();
    }

    @ApiOperation(value = "检查名称是否存在",notes = "检查名称是否存在")
    @GetMapping("/check/name/exist")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Boolean> checkNameIsExist(@NotBlank(message = "名称不能为空") String name,@ApiParam(value = "新增时不需要传,修改需要传") Long id){
        return ResultData.instance().setData(roleService.checkNameIsExist(name, id));
    }

    @ApiOperation(value = "检查编码是否存在",notes = "检查编码是否存在")
    @GetMapping("/check/code/exist")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Boolean> checkCodeIsExist(@NotBlank(message = "名称不能为空") String code,@ApiParam(value = "新增时不需要传,修改需要传") Long id){
        return ResultData.instance().setData(roleService.checkCodeIsExist(code, id));
    }

    @ApiOperation(value = "根据id获取详情",notes = "根据id获取详情")
    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Role> details(@NotNull(message = "id不能为空") Long id){
        Optional<Role> optional = roleService.findById(id);
        if (!optional.isPresent()) {
            return ResultData.instance();
        }
        Role role = optional.get();
        return ResultData.instance().setData(role);
    }

    @ApiOperation(value = "删除角色",notes = "删除角色")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_DELETE')")
    public IResultData delete(@NotNull(message = "id不能为空") @RequestParam(value = "id",required = false) @ApiParam(value = "数组(id=1&id=2)") List<Long> id){
        id.forEach(i->{
            Optional<Role> optional = roleService.findById(i);
            if (optional.isPresent()) {
                Role role = optional.get();
                if (!role.getIsDefault()) {
                    roleService.deleteById(i);
                    roleUserService.deleteByRoleId(i);
                    roleResourcesService.deleteByRoleId(i);
                }
            }

        });
        ResultData resultData = ResultData.instance();
        return resultData;
    }

    @ApiOperation(value = "获取资源树形结构（角色配置专用）",notes = "获取资源树形结构（角色配置专用）")
    @GetMapping("/resources/tree")
    @PreAuthorize("isAuthenticated()")
    public IResultData<List<RoleResourcesTreeVo>> roleResourcesTree(@RequestParam(name = "scope",defaultValue = "CONSOLE") Scope scope){
        return ResultData.instance().setData(roleService.roleResources(scope));
    }

    @ApiOperation(value = "保存角色权限（资源）",notes = "保存角色权限（资源）")
    @PostMapping("/save/resources")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_RESOURCES')")
    public IResultData addResources(@RequestBody @Validated AddRoleResourcesdDto addRoleResourcesdDto){
        this.roleResourcesService.saveRoleResources(addRoleResourcesdDto);
        return ResultData.instance();
    }

    @ApiOperation(value = "获取角色权限（资源）",notes = "获取角色权限（资源）")
    @GetMapping("/resources")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_RESOURCES')")
    public IResultData<List<Long>> resources(@NotNull(message = "角色id不能为空") Long roleId){
        List<RoleResources> list = this.roleResourcesService.findAllRoleResources(roleId);
        List<Long> returnList = new ArrayList<Long>();
        list.forEach(roleResources->{
            if (roleResources.getIsChecked()) {
                returnList.add(roleResources.getResourcesId());
            }
        });
        List<Resources> resourcesList = resourcesService.findByTypeAndIdIn(Type.CATALOG,returnList);
        resourcesList.forEach(resources -> {
            List<Resources> chiledList = resourcesService.findAllChilderResources(resources.getId());
            chiledList.forEach(r ->{
                if (!returnList.contains(r.getId())){
                    returnList.remove(resources.getId());
                }
            });
        });
        return ResultData.instance().setData(returnList);
    }

    @ApiOperation(value = "保存角色关联的用户",notes = "保存角色关联的用户")
    @PostMapping("/save/user")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_USER')")
    public IResultData saveRoleUser(@RequestBody @Validated AddRoleUserDto addRoleUserDto){
        roleUserService.saveRoleUser(addRoleUserDto);
        return ResultData.instance();
    }

    @ApiOperation(value = "根据角色id和用户id查询角色所关联的用户",notes = "根据角色id和用户id查询角色所关联的用户")
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_ROLE_USER')")
    public IResultData<List<Long>> roleUser(@NotNull(message = "角色id不能为空") Long roleId,@RequestParam(name = "userId",required = false,defaultValue = "0") @ApiParam(value = "数组(userId=1&userId=2)")  List<Long> userId){
        List<RoleUser> list = roleUserService.findRoleUser(roleId, userId);
        List<Long> returnList = new ArrayList<Long>();
        list.forEach(roleUser -> {
            returnList.add(roleUser.getUserId());
        });
        return ResultData.instance().setData( returnList);
    }

}
