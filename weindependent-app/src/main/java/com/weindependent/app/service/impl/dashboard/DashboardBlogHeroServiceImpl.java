package com.weindependent.app.service.impl.dashboard;

import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.database.dataobject.BlogHeroDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogHeroMapper;
import com.weindependent.app.dto.BlogHeroEditDTO;
import com.weindependent.app.service.DashboardBlogHeroService;
import com.weindependent.app.vo.websiteoperation.DashboardBlogHeroVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class DashboardBlogHeroServiceImpl implements DashboardBlogHeroService {

    @Autowired
    DashboardBlogHeroMapper heroMapper;

    @Override
    public List<DashboardBlogHeroVO> getCurrentBlogHeroDOList() {
        return heroMapper.selectCurrentBlogHeroDOList();
    }

    @Override
    public List<DashboardBlogHeroVO> getPreviousBlogHeroDOList() {
        return heroMapper.selectPreviousBlogHeroDOList();
    }

    @Override
    public BlogHeroDO getBlogHeroById(Integer id) {
        return heroMapper.selectBlogHeroDOById(id);
    }

    @Override
    public int insert(BlogHeroEditDTO newHero) {
        BlogHeroDO newHeroDO = new BlogHeroDO();
        BeanUtils.copyProperties(newHero, newHeroDO);
        newHeroDO.setCreateUserId(StpUtil.getLoginIdAsInt());
        newHeroDO.setUpdateUserId(StpUtil.getLoginIdAsInt());
        heroMapper.insert(newHeroDO);
        return 1;
    }

    @Override
    public int update(BlogHeroEditDTO updateHero) {
        BlogHeroDO updateHeroDO = new BlogHeroDO();
        BeanUtils.copyProperties(updateHero, updateHeroDO);
        updateHeroDO.setUpdateUserId(StpUtil.getLoginIdAsInt());
        return heroMapper.update(updateHeroDO);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return heroMapper.deleteByIds(ids, StpUtil.getLoginIdAsInt());
    }


    @Transactional
    @Override
    public int updateByDeletionAndInsertion(BlogHeroEditDTO updateHero) {
        heroMapper.deleteByIds(Collections.singletonList(updateHero.getId()), StpUtil.getLoginIdAsInt());
        BlogHeroDO newHeroDO = new BlogHeroDO();
        BeanUtils.copyProperties(updateHero, newHeroDO);
        newHeroDO.setCreateUserId(StpUtil.getLoginIdAsInt());
        newHeroDO.setUpdateUserId(StpUtil.getLoginIdAsInt());
        return heroMapper.insert(newHeroDO);
    }
}
