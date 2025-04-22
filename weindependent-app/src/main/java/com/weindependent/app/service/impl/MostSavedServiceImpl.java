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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.weindependent.app.utils.CommonUtil;

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

        for (BlogHomePageHeroVO blogHomePageHeroVO : mostSavedBlogHomePageHeroVO) {
            blogHomePageHeroVO.setHeroType("Most Saved");
            blogHomePageHeroVO.setBannerImageUrl(CommonUtil.convertToImgSrc(blogHomePageHeroVO.getBannerImageUrl(),400));
            result.add(blogHomePageHeroVO);
        }

        for (BlogHomePageHeroVO blogHomePageHeroVO : editorPickBlogHomePageHeroVO) {
            blogHomePageHeroVO.setHeroType("Editor's Pick");
            blogHomePageHeroVO.setBannerImageUrl(CommonUtil.convertToImgSrc(blogHomePageHeroVO.getBannerImageUrl(),400));
            result.add(blogHomePageHeroVO);
        }

        return result;
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
}
