package com.lion.upms.controller.department;

import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.department.DepartmentUser;
import com.lion.upms.entity.department.dto.AddDepartmentUserDto;
import com.lion.upms.service.department.DepartmentService;
import com.lion.upms.service.department.DepartmentUserService;
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
@RequestMapping("/upms/department/console")
@Validated
public class DepartmentController extends BaseControllerImpl implements BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentUserService departmentUserService;

    /**
     * 部门树形结构
     * @return
     */
    @GetMapping("/list/tree")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_LIST')")
    public IResultData listTree(){
        return ResultData.instance().setData("list",departmentService.listTree());
    }

    /**
     * 根据父节点ID检查同节点的名称是否存在
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    @GetMapping("/check/name/exist")
    public IResultData checkNameIsExist(@NotNull(message = "部门父节点id不能为空")Long parentId, @NotBlank(message = "名称不能为空")String name,Long id){
        return ResultData.instance().setData("isExist",departmentService.checkNameIsExist(parentId, name,id));
    }

    /**
     * 新增部门
     * @param department
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_ADD')")
    public IResultData add(@RequestBody @Validated({Validator.Insert.class}) Department department){
        departmentService.save(department);
        return ResultData.instance();
    }

    /**
     * 更新部门
     * @param department
     * @return
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_UPDATE')")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) Department department){
        departmentService.update(department);
        return ResultData.instance();
    }

    /**
     * 获取部门详情
     * @param id
     * @return
     */
    @GetMapping("/details")
    public IResultData details(@NotNull(message = "id不能为空")Long id){
        Department department = this.departmentService.findById(id);
        return ResultData.instance().setData("department",department);
    }

    /**
     * 删除资源
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_DELETE')")
    public IResultData delete(@NotNull(message = "id不能为空") Long id){
        departmentService.delete(id);
        departmentUserService.deleteByDepartmentId(id);
        return ResultData.instance();
    }


    /**
     * 保存部门关联的用户
     * @param addDepartmentUserDto
     * @return
     */
    @PostMapping("/save/user")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_USER')")
    public IResultData saveUser(@RequestBody @Validated AddDepartmentUserDto addDepartmentUserDto){
        departmentUserService.saveUser(addDepartmentUserDto);
        return ResultData.instance();
    }

    /**
     * 根据部门id和userid获取部门所关联的用户
     * @param id
     * @param userId
     * @return 已关联数据和不能关联数据（在其它部门已关联）
     */
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_DEPARTMENT_USER')")
    public IResultData user(@NotNull(message = "部门id不能为空") Long id, @RequestParam(name = "userId",defaultValue = "0",required = false) List<Long> userId){
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
        resultData.setData("oldUserId",oldUserId);
        resultData.setData("notCheckUserId",notCheckUserId);
        return resultData;
    }


}
