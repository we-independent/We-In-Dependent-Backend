package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventMapper {
    List<RecentEventVO> getRecent(@Param("size") int size, @Param("offset") int offset);
    EventVO getById(Long id);
}
