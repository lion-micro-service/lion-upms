package com.lion.upms.entity.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.core.persistence.Validator;
import com.lion.upms.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.time.LocalDate;

/**
 * @author mr.liu
 * @Description: 用户更新模型
 * @date 2020/8/29下午3:28
 */
@Data
@JsonIgnoreProperties(value={"username","password","isAccountNonExpired","isAccountNonLocked","isCredentialsNonExpired","isEnabled","createDateTime","updateDateTime","createUserId","updateUserId"})
@Schema(description = "用户更新")
public class UserUpdataDto extends User {

}
