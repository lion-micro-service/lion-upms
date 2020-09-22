package com.lion.upms.entity.resources.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lion.core.IEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: Type
 * @description: TODO
 * @date 2020/8/14上午11:42
 */
public enum Type implements IEnum {
    CATALOG(0, "目录"), MENU(1, "菜单"),FUNCTION(2,"功能");

    private final int key;

    private final String desc;

    private Type(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getDesc(){
        return desc;
    }

    @Override
    public Map<String, Object> jsonValue() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("desc", desc);
        map.put("name", getName());
        return map;
    }

    @JsonCreator
    public static Type getScope(String name){
        for(Type item : values()){
            if(Objects.equals(item.getName(),name)){
                return item;
            }
        }
        return null;
    }
}
