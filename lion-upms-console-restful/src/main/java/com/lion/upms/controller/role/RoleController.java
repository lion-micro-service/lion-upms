package com.lion.upms.controller.role;

import com.lion.constant.SearchConstant;
import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.JpqlParameter;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.role.Role;
import com.lion.upms.service.role.RoleDepartmentService;
import com.lion.upms.service.role.RoleResourcesService;
import com.lion.upms.service.role.RoleService;
import com.lion.upms.service.role.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.ApiListing;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: RoleController
 * @description: 角色Controller
 * @date 2020/8/15下午5:51
 */
@RestController
@RequestMapping("/upms/role/console")
@Validated
public class RoleController extends BaseControllerImpl implements BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private RoleResourcesService roleResourcesService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;


    /**
     * 列表
     * @param name
     * @param code
     * @param lionPage
     * @return
     */
    @GetMapping("/list")
    public IResultData list(String name, String code, LionPage lionPage){
        JpqlParameter jpqlParameter = new JpqlParameter();
        if (StringUtils.hasText(name)){
            jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_name",name);
        }
        if (StringUtils.hasText(code)){
            jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_code",code);
        }
        jpqlParameter.setSortParameter("createDateTime", Sort.Direction.DESC);
        lionPage.setJpqlParameter(jpqlParameter);
        return roleService.findNavigator(lionPage);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping("/add")
    public IResultData add(@RequestBody @Validated({Validator.Insert.class}) Role role){
        this.roleService.save(role);
        return ResultData.instance();
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("/update")
    public IResultData update(@RequestBody @Validated({Validator.Update.class})  Role role) {
        this.roleService.update(role);
        return ResultData.instance();
    }

    /**
     * 检查名称是否存在
     * @param name
     * @param id
     * @return
     */
    @GetMapping("/check/name/exist")
    public IResultData checkNameIsExist(@NotBlank(message = "名称不能为空") String name, Long id){
        return ResultData.instance().setData("isExist",roleService.checkNameIsExist(name, id));
    }

    /**
     * 检查编码是否存在
     * @param code
     * @param id
     * @return
     */
    @GetMapping("/check/code/exist")
    public IResultData checkCodeIsExist(@NotBlank(message = "名称不能为空") String code, Long id){
        return ResultData.instance().setData("isExist",roleService.checkCodeIsExist(code, id));
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @GetMapping("/details")
    public IResultData details(@NotNull(message = "id不能为空") Long id){
        return ResultData.instance().setData("role",roleService.findById(id));
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public IResultData delete(@NotNull(message = "id不能为空") @RequestParam(value = "id",required = false) List<Long> id){
        id.forEach(i->{
            Role role = this.roleService.findById(i);
            if (Objects.nonNull(role) && !role.getIsDefault()) {
                roleService.deleteById(i);
            }
        });
        ResultData resultData = ResultData.instance();
        return resultData;
    }

}
