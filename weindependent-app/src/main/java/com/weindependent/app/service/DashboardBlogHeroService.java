package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.BlogHeroDO;
import com.weindependent.app.dto.BlogHeroEditDTO;
import com.weindependent.app.vo.websiteoperation.DashboardBlogHeroVO;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
@Service
public interface DashboardBlogHeroService {
    List<DashboardBlogHeroVO> getCurrentBlogHeroDOList();

    List<DashboardBlogHeroVO> getPreviousBlogHeroDOList();

    BlogHeroDO getBlogHeroById(Integer id);
    int insert(BlogHeroEditDTO newHero);
    int update(BlogHeroEditDTO updateHero);
    int deleteByIds(List<Integer> ids);

    int updateByDeletionAndInsertion(@Valid BlogHeroEditDTO updateHero);
}
