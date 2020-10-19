package com.lion.upms.entity.department;

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
 * @title: DepartmentUser
 * @description: 部门与用户关联表
 * @date 2020/8/14下午2:11
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_department_user",indexes = {@Index(columnList = "department_id"),@Index(columnList = "user_id")})
@DynamicUpdate
@DynamicInsert
@Data
@ApiModel(description = "部门与用户关联表")
public class DepartmentUser extends BaseEntity {
    private static final long serialVersionUID = 5483603548923984650L;

    @ApiModelProperty(value = "部门ID")
    @Column(name = "department_id",columnDefinition = " BIGINT(18) comment '部门ID' ", nullable = false)
    @NotNull(message = "部门ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long departmentId;

    @ApiModelProperty(value = "用户ID")
    @Column(name = "user_id",columnDefinition = " BIGINT(18) comment '用户ID' ", nullable = false)
    @NotNull(message = "用户ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long userId;
}
