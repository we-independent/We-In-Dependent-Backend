package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.websiteoperation.BlogHeroVO;

import java.util.List;

public interface BlogHeroMapper {
    List<BlogHeroVO> selectCurrentBlogHeroVOList();
}
