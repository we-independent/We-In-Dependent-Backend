package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.BlogHeroDO;
import com.weindependent.app.vo.websiteoperation.DashboardBlogHeroVO;

import java.util.List;

public interface DashboardBlogHeroMapper {


    List<DashboardBlogHeroVO> selectCurrentBlogHeroDOList();

    List<DashboardBlogHeroVO> selectPreviousBlogHeroDOList();

    BlogHeroDO selectBlogHeroDOById(Integer id);

    int insert(BlogHeroDO BlogHeroDO);

    int update(BlogHeroDO BlogHeroDO);

    int deleteByIds(List<Integer> ids, int updateUserId);
}
