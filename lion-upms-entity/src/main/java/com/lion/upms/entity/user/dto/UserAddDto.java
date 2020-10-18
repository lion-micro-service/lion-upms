package com.lion.upms.entity.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.upms.entity.user.User;
import lombok.Data;

/**
 * @author mr.liu
 * @Description: 用户新增模型
 * @date 2020/8/29下午3:28
 */
@Data
@JsonIgnoreProperties(value={"isAccountNonExpired","isAccountNonLocked","isCredentialsNonExpired","isEnabled"})
public class UserAddDto extends User {
}
