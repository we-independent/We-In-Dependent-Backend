package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.EventSpeakerDO;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardSpeakerMapper {
    int insert(EventSpeakerDO speaker);
    int update(EventSpeakerDO speaker);
    int delete(@Param("ids") List<Long> ids);
    List<DashboardSpeakerVO> list();
    DashboardSpeakerVO getById(@Param("id") Long id);

    EventSpeakerDO getByUserId(@Param("userId") Long userId);
    List<Long> getBannerIdsBySpeakerIds(List<Long> ids);
}
