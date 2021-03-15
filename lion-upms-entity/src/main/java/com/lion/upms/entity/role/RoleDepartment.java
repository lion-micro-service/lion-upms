package com.lion.upms.entity.role;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author mr.liu
 * @title: RoleDepartment
 * @description: 角色用户部门关联表
 * @date 2020/8/13下午4:38
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_role_department",indexes = {@Index(columnList = "department_id"),@Index(columnList = "role_id")})
@DynamicUpdate
@DynamicInsert
@Data
@ApiModel(description = "角色用户部门关联表")
public class RoleDepartment extends BaseEntity {
    private static final long serialVersionUID = 8847474715161911685L;

    @ApiModelProperty(value = "角色ID")
    @Column(name = "role_id", nullable = false)
    @NotNull(message = "角色ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long roleId;

    @ApiModelProperty(value = "部门ID")
    @Column(name = "department_id", nullable = false)
    @NotNull(message = "部门ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long departmentId;
}
