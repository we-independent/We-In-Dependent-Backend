package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.DonateVolunteerDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DonateVolunteerMapper {

    // 插入一条志愿者申请，返回自增ID会写入DO的id字段
    int insert(DonateVolunteerDO donateVolunteer);

}
