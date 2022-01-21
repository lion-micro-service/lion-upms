package com.lion.upms.controller.department;

import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.department.DepartmentUser;
import com.lion.upms.entity.department.dto.AddDepartmentUserDto;
import com.lion.upms.entity.department.vo.DepartmentTreeVo;
import com.lion.upms.service.department.DepartmentService;
import com.lion.upms.service.department.DepartmentUserService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mr.liu
 * @title: DepartmentController
 * @description: 部门controller
 * @date 2020/8/15下午6:01
 */
@RestController
@RequestMapping("/department/console")
@Validated
@Api(tags = {"部门管理"})
public class DepartmentController extends BaseControllerImpl implements BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentUserService departmentUserService;

    @GetMapping("/list/tree")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_LIST')")
    @ApiOperation(value = "部门列表树形结构",notes = "部门列表树形结构")
    public IResultData<List<DepartmentTreeVo>> listTree(){
        return ResultData.instance().setData(departmentService.listTree());
    }

    @GetMapping("/check/name/exist")
    @ApiOperation(value = "根据父节点ID检查同节点的名称是否存在",notes = "根据父节点ID检查同节点的名称是否存在")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Boolean> checkNameIsExist(@NotNull(message = "部门父节点id不能为空")Long parentId, @NotBlank(message = "名称不能为空")String name,@ApiParam(value = "修改需要传id，新增则不需要传") Long id){
        return ResultData.instance().setData(departmentService.checkNameIsExist(parentId, name,id));
    }

    @ApiOperation(value = "新增部门",notes = "新增部门")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_ADD')")
    public IResultData add(@RequestBody @Validated({Validator.Insert.class}) Department department){
        departmentService.save(department);
        return ResultData.instance();
    }

    @ApiOperation(value = "更新部门",notes = "更新部门")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_UPDATE')")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) Department department){
        departmentService.update(department);
        return ResultData.instance();
    }

    @ApiOperation(value = "获取部门详情",notes = "获取部门详情")
    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public IResultData<Department> details(@NotNull(message = "id不能为空")Long id){
        Department department = this.departmentService.findById(id);
        return ResultData.instance().setData(department);
    }

    @ApiOperation(value = "删除资源",notes = "删除资源")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_DELETE')")
    public IResultData delete(@NotNull(message = "id不能为空")Long id){
        departmentService.delete(id);
        departmentUserService.deleteByDepartmentId(id);
        return ResultData.instance();
    }

    @ApiOperation(value = "保存部门关联的用户",notes = "保存部门关联的用户")
    @PostMapping("/save/user")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_USER')")
    public IResultData saveUser(@RequestBody @Validated AddDepartmentUserDto addDepartmentUserDto){
        departmentUserService.saveUser(addDepartmentUserDto);
        return ResultData.instance();
    }

    @ApiOperation(value = "根据部门id和userid获取部门所关联的用户 返回(已关联数据和不能关联数据（在其它部门已关联）)",notes = "根据部门id和userid获取部门所关联的用户 返回(已关联数据和不能关联数据（在其它部门已关联）)")
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_USER')")
    public IResultData<DepartmetIdUserId> user(@NotNull(message = "部门id不能为空") Long id, @RequestParam(name = "userId",defaultValue = "0",required = false) List<Long> userId){
        List<DepartmentUser> oldDepartmentUser = departmentUserService.findDepartmentUser(id,userId);
        List<DepartmentUser> notCheckDepartmentUser = departmentUserService.findNotInDepartmentUser(id,userId);
        ResultData resultData = ResultData.instance();
        List<Long> oldUserId = new ArrayList<Long>();
        List<Long> notCheckUserId = new ArrayList<Long>();
        oldDepartmentUser.forEach(departmentUser -> {
            oldUserId.add(departmentUser.getUserId());
        });
        notCheckDepartmentUser.forEach(departmentUser -> {
            notCheckUserId.add(departmentUser.getUserId());
        });
        DepartmetIdUserId departmetIdUserId = new DepartmetIdUserId();
        departmetIdUserId.setOldUserId(oldUserId);
        departmetIdUserId.setNotCheckUserId(notCheckUserId);
        resultData.setData(departmetIdUserId);
        return resultData;
    }
}

@Schema()
@Data
class DepartmetIdUserId{
    @Schema(description = "已经选择中的user")
    List<Long> oldUserId;
    @Schema(description = "不能选择的user")
    List<Long> notCheckUserId;
}
