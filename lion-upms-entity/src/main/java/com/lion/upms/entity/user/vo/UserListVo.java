package com.lion.upms.entity.user.vo;

import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.department.vo.DepartmentTreeVo;
import com.lion.upms.entity.role.Role;
import com.lion.upms.entity.user.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import sun.util.resources.ga.LocaleNames_ga;

import java.util.List;
import java.util.Objects;

/**
 * @description: 用户列表数据模型
 * @author: mr.liu
 * @create: 2020-09-29 15:50
 **/
@Data
public class UserListVo {

    /**
     * 用户
     */
    private User user;

    /**
     * 部门
     */
    private DepartmentTreeVo department;

    /**
     * 角色
     */
    private List<Role> role;

    /**
     * vue组件使用，临时解决方案。 table组件似乎不支持rowKey={record => record.user.id}写法
     */
    private Long key;

    public UserListVo(User user, Department department) {
        this.user = user;
        if (Objects.nonNull(department)) {
            this.department = new DepartmentTreeVo();
            BeanUtils.copyProperties(department, this.department);
        }
        this.role = role;
    }

    public Long getKey() {
        return user.getId();
    }
}
