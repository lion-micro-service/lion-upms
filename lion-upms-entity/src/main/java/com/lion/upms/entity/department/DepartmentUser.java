package com.lion.upms.entity.department;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Table(name = "t_department_user",indexes = {@Index(columnList = "department_id"),@Index(columnList = "user_id")})

@DynamicInsert
@Data
@Schema(description = "部门与用户关联表")
public class DepartmentUser extends BaseEntity {
    private static final long serialVersionUID = 5483603548923984650L;

    @Schema(description = "部门ID")
    @Column(name = "department_id", nullable = false)
    @NotNull(message = "部门ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long departmentId;

    @Schema(description = "用户ID")
    @Column(name = "user_id", nullable = false)
    @NotNull(message = "用户ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long userId;
}
