package com.weindependent.app.controller.dashboard;


import cn.dev33.satoken.annotation.SaCheckRole;
import com.weindependent.app.database.dataobject.BlogHeroDO;
import com.weindependent.app.dto.BlogHeroEditDTO;
import com.weindependent.app.service.DashboardBlogHeroService;
import com.weindependent.app.vo.websiteoperation.DashboardBlogHeroVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/dashboard")
public class DashboardBlogHeroController {

    @Resource
    private DashboardBlogHeroService dashboardBlogHeroService;

    @SaCheckRole("admin")
    @Operation(summary = "查询目前 hero section 推送文章")
    @GetMapping("/blog-hero-current")
    public List<DashboardBlogHeroVO> getCurrentBlogHeroDOList() {
        return dashboardBlogHeroService.getCurrentBlogHeroDOList();
    }

    @SaCheckRole("admin")
    @Operation(summary = "查询历史 hero section 推送文章")
    @GetMapping("/blog-hero-previous")
    public List<DashboardBlogHeroVO> getPreviousBlogHeroDOList() {
        return dashboardBlogHeroService.getPreviousBlogHeroDOList();
    }

    @SaCheckRole("admin")
    @Operation(summary = "按照 id 查询单个 hero section 推送文章")
    @GetMapping(value = "/blog-hero/{id}")
    public BlogHeroDO getInfo(@PathVariable("id") Integer id) {
        return dashboardBlogHeroService.getBlogHeroById(id);
    }

    @SaCheckRole("admin")
    @Operation(summary = "新增 hero section 推送文章")
    @PostMapping(value = "/blog-hero")
    public boolean add(@RequestBody BlogHeroEditDTO newHero) {
        return dashboardBlogHeroService.insert(newHero) > 0;
    }

//    @SignatureAuth
//    @SaCheckRole("admin")
//    @Operation(summary = "修改 hero section 推送文章")
//    @PutMapping(value = "/blog-hero")
//    public boolean edit(@RequestBody @Valid BlogHeroEditDTO updateHero) {
//        return dashboardBlogHeroService.update(updateHero) > 0;
//    }

    @SaCheckRole("admin")
    @Operation(summary = "修改 hero section 推送文章")
    @PutMapping(value = "/blog-hero")
    public boolean edit(@RequestBody @Valid BlogHeroEditDTO updateHero) {
        return dashboardBlogHeroService.updateByDeletionAndInsertion(updateHero)>1;
    }

    //删除多个
    @SaCheckRole("admin")
    @Operation(summary = "删除一或多项 hero section 推送文章")
    @DeleteMapping("/blog-hero/{ids}")
    public boolean remove(@PathVariable List<Integer> ids) {
        return dashboardBlogHeroService.deleteByIds(ids) > 0;
    }


}
