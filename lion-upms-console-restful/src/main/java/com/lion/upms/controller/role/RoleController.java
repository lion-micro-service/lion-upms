package com.lion.upms.controller.role;

import com.lion.constant.SearchConstant;
import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.JpqlParameter;
import com.lion.upms.service.role.RoleDepartmentService;
import com.lion.upms.service.role.RoleResourcesService;
import com.lion.upms.service.role.RoleService;
import com.lion.upms.service.role.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mr.liu
 * @title: RoleController
 * @description: 角色Controller
 * @date 2020/8/15下午5:51
 */
@RestController
@RequestMapping("/upms/role/console")
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
}
