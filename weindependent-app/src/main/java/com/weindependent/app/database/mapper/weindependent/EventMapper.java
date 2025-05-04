package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventMapper {
    List<RecentEventVO> getRecent(@Param("offset") int offset,
                                  @Param("size") int size,
                                  @Param("userId") int userId);

    EventVO getById(@Param("id") Long id,
                    @Param("userId") Integer userId);
    int register(Long id, Integer userId);
    int unregister(Long id, Integer userId);
    int bookmark(Long id, Integer userId);
    int unbookmark(Long id, Integer userId);
}
