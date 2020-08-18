package com.lion.upms.entity.role;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author mr.liu
 * @title: RoleResources
 * @description: 角色资源关联表
 * @date 2020/8/13下午4:25
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_role_resources",uniqueConstraints ={@UniqueConstraint(columnNames = "role_id")},indexes = {@Index(columnList = "resources_id")})
@DynamicUpdate
@DynamicInsert
@Data
public class RoleResources extends BaseEntity {

    private static final long serialVersionUID = -772859768017737144L;

    @Column(name = "role_id",columnDefinition = " BIGINT(20) comment '角色ID' ", nullable = false)
    @NotNull(message = "角色ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long roleId;

    @Column(name = "resources_id",columnDefinition = " BIGINT(20) comment '资源ID' ", nullable = false)
    @NotNull(message = "资源ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long resourcesId;
}
