package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.event.EventSpeakerVO;
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
    List<RecentEventVO> getUpcomingByMonth(
                                    @Param("userId") Integer userId,
                                    @Param("year") Integer year,
                                   @Param("month") Integer month);
    EventVO getById(@Param("id") Long id,
                    @Param("userId") Integer userId);
    List<EventSpeakerVO> getSpeakersByEventId(@Param("eventId") Long eventId);
    int register(Long id, Long userId);
    int unregister(Long id, Integer userId);
    int bookmark(Long id, Integer userId);
    int unbookmark(Long id, Integer userId);
    List<RecentEventVO> getRegisteredUpcomingEvents(Integer userId);
    List<RecentEventVO> getRegisteredPastEvents(Integer userId);
    List<RecentEventVO> getViewedEvents(int userId);
    List<RecentEventVO> getBookmarkedPastEvents(int userId);
    List<RecentEventVO> getBookmarkedUpcomingEvents(int userId);
//    List<RecentEventVO> searchEventsNatural(@Param("keyword") String keyword);
//    List<RecentEventVO> searchEventsBoolean(@Param("keyword") String keyword);
//    List<RecentEventVO> getPastEventsFiltered(
//            @Param("tagIds") List<Integer> tagIds
//    );
    List<RecentEventVO> searchAndFilter(@Param("keyword") String keyword,
                                        @Param("tagIds") List<Integer> tagIds);
    boolean isRegistered(Long eventId, int userId);
}
