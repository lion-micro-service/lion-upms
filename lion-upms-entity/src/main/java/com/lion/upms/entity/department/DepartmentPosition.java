package com.lion.upms.entity.department;

import com.lion.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @description: 部门与职位关联
 * @author: mr.liu
 * @create: 2020-09-28 09:58
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_department_position_user")
@DynamicUpdate
@DynamicInsert
@Data
public class DepartmentPosition extends BaseEntity {

}
