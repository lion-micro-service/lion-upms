package com.lion.upms.entity.resources.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lion.upms.entity.resources.Resources;
import lombok.Data;

import java.util.List;

/**
 * @author mr.liu
 * @Description: 资源树形结构vo
 * @date 2020/9/3下午3:55
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true,value = {"createDateTime","updateDateTime","createUserId","updateUserId"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourcesTreeVo extends Resources {

    /**
     * 资源子节点
     */
    private List<ResourcesTreeVo> children;
}
