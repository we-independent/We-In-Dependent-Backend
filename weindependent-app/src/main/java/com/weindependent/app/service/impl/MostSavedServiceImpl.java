package com.weindependent.app.service.impl;

import com.weindependent.app.convertor.EditorPickConvertor;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.EditorPickDO;
import com.weindependent.app.database.mapper.weindependent.BlogArticleMapper;
import com.weindependent.app.database.mapper.weindependent.EditorPickMapper;
import com.weindependent.app.database.mapper.weindependent.MostSavedMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.MostSavedService;
import com.weindependent.app.vo.BlogHomePageHeroVO;
import com.weindependent.app.vo.EditorPickVO;
import com.weindependent.app.dto.BlogArticleCardQry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.weindependent.app.utils.CommonUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MostSavedServiceImpl implements MostSavedService {
    @Resource
    private MostSavedMapper mostSavedMapper;

    @Resource
    private EditorPickMapper editorPickMapper;

    @Resource
    private BlogArticleMapper blogArticleMapper;

    @Override
   public List<BlogHomePageHeroVO>  getCurrentBlogHomePageHeroVO() {

        List<EditorPickDO> mostSavedList = mostSavedMapper.findCurrentMostSavedList();
        List<EditorPickDO> editorPickList = editorPickMapper.findEditorPickList(2);

        if (mostSavedList == null || mostSavedList.isEmpty()) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "没有查询到当前MostSaved推送");
        }
        if (editorPickList == null || editorPickList.isEmpty()) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "没有查询到当前editorPick推送");
        }
        if (mostSavedList.size() != 2) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "推送数量不正确，推送数量应当为2而查询到推送数量为" + mostSavedList.size());
        }

        // 提取提取所有 articleId
        List<Integer> mostSavedArticleIdList = mostSavedList.stream()
                .map(EditorPickDO::getArticleId)
                .collect(Collectors.toList());
        List<Integer> editorPickArticleIdList = editorPickList.stream()
                .map(EditorPickDO::getArticleId)
                .collect(Collectors.toList());

        List<BlogHomePageHeroVO> mostSavedBlogHomePageHeroVO = mostSavedMapper.findBlogHomePageHeroVOByIds(mostSavedArticleIdList);
        List<BlogHomePageHeroVO> editorPickBlogHomePageHeroVO = mostSavedMapper.findBlogHomePageHeroVOByIds(editorPickArticleIdList);
        List<BlogHomePageHeroVO> result = new ArrayList<>();

        // 拼装 Most Saved 数据，补全字段 + 图片地址处理
        for (BlogHomePageHeroVO vo : mostSavedBlogHomePageHeroVO) {
            vo.setHeroType("Most Saved");

            EditorPickDO match = mostSavedList.stream()
                    .filter(item -> item.getArticleId().equals(vo.getArticleId()))
                    .findFirst().orElse(null);
            if (match != null) {
                vo.setCreateTime(match.getCreateTime());
                vo.setCreateUserId(match.getCreateUserId());
            }

            vo.setBannerImageUrl(CommonUtil.convertToImgSrc(vo.getBannerImageUrl(), 400));
            result.add(vo);
        }

        // 拼装 Editor's Pick 数据，补全字段 + 图片地址处理
        for (BlogHomePageHeroVO vo : editorPickBlogHomePageHeroVO) {
            vo.setHeroType("Editor's Pick");

            EditorPickDO match = editorPickList.stream()
                    .filter(item -> item.getArticleId().equals(vo.getArticleId()))
                    .findFirst().orElse(null);
            if (match != null) {
                vo.setCreateTime(match.getCreateTime());
                vo.setCreateUserId(match.getCreateUserId());
            }

            vo.setBannerImageUrl(CommonUtil.convertToImgSrc(vo.getBannerImageUrl(), 400));
            result.add(vo);

            System.out.println(">>> articleId=" + vo.getArticleId() + ", isDeleted=" + vo.getIsDeleted());
        }

        return result;
    }

    @Override
    public List<BlogArticleCardQry> getMostSavedArticlesExcludeList(List<Integer> excludeIds, int needed) {
        return mostSavedMapper.getMostSavedArticlesExcludeList(excludeIds, needed);
    }

    /**
     *   <p>更新MostSaved，每个月的1号和15号 00：01 由spring scheduling 定时任务调用</p>
     *   方法内步骤：
     *   1. 查询数据库上次更新日期后是否有mostsave更新，时区需要统一为数据库时间。
     *   2. 如果没有mostsave更新，计算并更新mostsave。
     */
    @Override
    @Transactional()
    public int setMostSavedEveryTwoWeeks() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDateTime lastUpdateTime = LocalDateTime.of(
                now.getMonth() == Month.JANUARY && now.getDayOfMonth() == 1 ? now.getYear() - 1 : now.getYear()
                , now.getDayOfMonth() == 1 ? now.getMonth().minus(1) : now.getMonth()
                , now.getDayOfMonth() == 1 ? 15 : 1, 1, 0);

        List<EditorPickDO> recentMostSaveList = mostSavedMapper.getMostSavedSince(lastUpdateTime);

        if (recentMostSaveList == null || recentMostSaveList.isEmpty()) {
            mostSavedMapper.setAllMostSavedAsInvalid();
           return mostSavedMapper.AddNewMostSaved();
        }
        return -1;
    }


    @Override
    public List<EditorPickVO> getCurrentMostSavedEditorPickVO()  {
        List<EditorPickDO> mostSavedList = mostSavedMapper.findCurrentMostSavedList();

        if (mostSavedList == null || mostSavedList.isEmpty()) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "没有查询到当前MostSaved推送");
        }
        if (mostSavedList.size() != 2) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(),"推送数量不正确，推送数量应当为2而查询到推送数量为" + mostSavedList.size());
        }

        // 提取所有 articleId，去文章表查详情
        List<Integer> articleIds = mostSavedList.stream()
                .map(EditorPickDO::getArticleId)
                .collect(Collectors.toList());
        if (articleIds.size() > 2) {
            articleIds = articleIds.subList(0, 2);
        }
        List<BlogArticleDO> articles = blogArticleMapper.findByIds(articleIds);
        Map<Integer, BlogArticleDO> articleMap = articles.stream()
                .collect(Collectors.toMap(BlogArticleDO::getId, a -> a));

        // 组合成 VO 列表
        List<EditorPickVO> result = new ArrayList<>();
        for (EditorPickDO pickDO : mostSavedList) {
            BlogArticleDO article = articleMap.get(pickDO.getArticleId());
            if (article != null) {
                EditorPickVO editorPickVO = EditorPickConvertor.toVO(pickDO, article);
                editorPickVO.setHeroType("Most Saved");
                editorPickVO.setIsDeleted(pickDO.getStatus() == 1 ? 0 : 1);
                result.add(editorPickVO);
            }
        }
        return result;
    }



    @Override
    public List<EditorPickVO> getPreviousMostSavedEditorPickVO() {

        List<EditorPickDO> mostSavedList = mostSavedMapper.findPreviousMostSavedList();

        if (mostSavedList == null || mostSavedList.isEmpty()) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "没有查询到历史MostSaved推送");
        }

        // 提取所有 articleId，去文章表查详情
        List<Integer> articleIds = mostSavedList.stream()
                .map(EditorPickDO::getArticleId)
                .collect(Collectors.toList());

        List<BlogArticleDO> articles = blogArticleMapper.findByIds(articleIds);
        Map<Integer, BlogArticleDO> articleMap = articles.stream()
                .collect(Collectors.toMap(BlogArticleDO::getId, a -> a));

        // 组合成 VO 列表
        List<EditorPickVO> result = new ArrayList<>();
        for (EditorPickDO pickDO : mostSavedList) {
            BlogArticleDO article = articleMap.get(pickDO.getArticleId());
            if (article != null) {
                EditorPickVO editorPickVO = EditorPickConvertor.toVO(pickDO, article);
                editorPickVO.setHeroType("Most Saved");
                editorPickVO.setIsDeleted(pickDO.getStatus() == 1 ? 0 : 1);
                result.add(editorPickVO);
            }
        }
        return result;
    }

    @Override
    public List<BlogArticleCardQry> getTopSavedBlogsForColdstart(int topN) {
        List<BlogArticleCardQry> blogCards = mostSavedMapper.findTopSavedBlogs(topN);

        if (blogCards == null || blogCards.isEmpty()) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "没有查询到Top Saved博客");
        }

        for (BlogArticleCardQry card : blogCards) {
            // 1. 计算阅读时长
            if (card.getContent() != null) {
                int wordCount = card.getContent().trim().split("\\s+").length;
                int readingTimeMinutes = Math.min(30, Math.max(1, (int) Math.ceil(wordCount / 200.0)));
                card.setReadingTime(readingTimeMinutes + " min");
            } else {
                card.setReadingTime("1 min");  // 防止null
            }

            // 2. 设置 articleUrl (href)
            card.setArticleUrl("/blogs/" + card.getCategoryName().toLowerCase() + "/" + card.getId());

            // 3. 去掉 content，不传给前端
            card.setContent(null);

            // 4. author默认都是We Independence
            card.setAuthor("We Independence");
        }

        return blogCards;
    }


}
