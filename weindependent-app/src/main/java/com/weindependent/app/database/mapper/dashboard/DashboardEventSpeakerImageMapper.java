package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.ImageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardEventSpeakerImageMapper {
    int insert(ImageDO image);
    int update(ImageDO image);
    int delete(@Param("ids") List<Long> ids);
    int setNotDeletedById(@Param("id") Long id);
    int setDeletedById(@Param("id") Long id);
}
