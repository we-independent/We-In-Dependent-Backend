package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.EventListQry;
import com.weindependent.app.vo.event.dashboard.DashboardEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardEventMapper {
    BlogArticleDO selectBlogArticleById(Integer id);


    EventDO getById(Integer id);
    /**
     * 新增博客文章
     *
     * @param eventDO 活動
     * @return 结果
     */
    int create(EventDO eventDO);

    int update(EventDO eventDO);

    int delete(@Param("ids") List<Long> ids, @Param("updateUserId") Long updateUserId);

    List<DashboardEventVO> list(EventListQry eventListQry);

    List<UserDO> getRegisteredUsers(Long id);

    List<UserDO> getBookmarkedUsers(Long id);

    EventDO getById(Long id);
}


