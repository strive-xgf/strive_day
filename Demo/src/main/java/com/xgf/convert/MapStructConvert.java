package com.xgf.convert;

import com.xgf.bean.*;
import org.mapstruct.*;

import java.math.BigDecimal;

/**
 * @author xgf
 * @create 2021-11-10 00:31
 * @description mapstruct 数据映射 【 todo map基于名称映射，名称相同类型不同编译报错】
 **/

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface MapStructConvert {

    /**
     * 多个相同名称字段需要制定用什么
     * @return 大大
     */
    @Mappings({
            @Mapping(target = "createdTime", source = "person.createdTime"),
            @Mapping(target = "updatedTime", source = "person.updatedTime"),
    })
    User personWorkToUser(Person person, WorkInfo work, BigDecimal bigDecimal);


    /**
     * @BeanMapping(ignoreByDefault = true) 只赋值显示的转换mapping
     */
    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "userUuid", source = "person.personUuid"),
            @Mapping(target = "hobby", source = "person.hobby"),
            @Mapping(target = "createdTime", source = "person.createdTime"),
            @Mapping(target = "updatedTime", source = "person.updatedTime"),
    })
    User personToUser(Person person);

    /**
     * @MappingTarget 数据更新，规则 nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL空值时赋空
     */
    @Mappings({
            @Mapping(target = "userUuid", source = "person.personUuid"),
            @Mapping(target = "createdTime", source = "person.createdTime"),
            @Mapping(target = "updatedTime", source = "person.updatedTime"),
    })
    void assembleUser(@MappingTarget User user, Hobby hobby, WorkInfo workInfo, Person person);

    UserDTO copyBean(User user);
}
