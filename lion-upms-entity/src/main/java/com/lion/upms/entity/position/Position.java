package com.lion.upms.entity.position;

import com.lion.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @description: 职位
 * @author: mr.liu
 * @create: 2020-09-28 09:55
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_position")

@DynamicInsert
@Data
public class Position extends BaseEntity {

}
