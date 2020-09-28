package com.lion.upms.controller.department;

import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.department.dto.DepartmentUserDto;
import com.lion.upms.service.department.DepartmentService;
import com.lion.upms.service.department.DepartmentUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    public IResultData delete(@NotNull(message = "id不能为空") Long id){
        departmentService.delete(id);
        return ResultData.instance();
    }


    /**
     * 保存部门关联的用户
     * @param departmentUserDto
     * @return
     */
    @PostMapping("/save/user")
    public IResultData saveUser(@RequestBody @Validated DepartmentUserDto departmentUserDto){

        return ResultData.instance();
    }

    /**
     * 根据部门id和userid获取部门所关联的用户
     * @param id
     * @param userId
     * @return 已关联数据和不能关联数据（在其它部门已关联）
     */
    public IResultData user(@NotNull(message = "部门id不能为空") Long id, @RequestParam(name = "userId",defaultValue = "0",required = false) List<Long> userId){
        return ResultData.instance();
    }


}
