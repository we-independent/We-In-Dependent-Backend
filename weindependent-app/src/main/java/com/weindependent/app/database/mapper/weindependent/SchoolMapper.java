package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.SchoolDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SchoolMapper {
     List<SchoolDO> findNearbySchools(@Param("latitude") Double latitude,
                                     @Param("longitude") Double longitude,
                                     @Param("radius") Double radius);
}
