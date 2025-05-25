package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventMapper {
    List<RecentEventVO> getUpcoming(Integer userId);
    List<RecentEventVO> getPast(Integer userId);
    void recordUserViewEvent(Integer userId, Long eventId);
    List<RecentEventVO> getByMonth(@Param("year") Integer year,
                                   @Param("month") Integer month);
    EventVO getById(@Param("id") Long id,
                    @Param("userId") Integer userId);
    int register(Long id, Long userId);
    int unregister(Long id, Integer userId);
    int bookmark(Long id, Integer userId);
    int unbookmark(Long id, Integer userId);
    List<RecentEventVO> getRegisteredUpcomingEvents(Integer userId);
    List<RecentEventVO> getRegisteredPastEvents(Integer userId);
    List<RecentEventVO> getViewedEvents(int userId);
    List<RecentEventVO> getBookmarkedEvents(int userId);
    List<EventVO> searchEventsNatural(@Param("keyword") String keyword);
    List<EventVO> searchEventsBoolean(@Param("keyword") String keyword);
}
