package com.weindependent.app.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.EditorPickDO;
import com.weindependent.app.vo.EditorPickVO;

public class EditorPickConvertor {
    /**
     * 将BlogAriticleDO转换为EditorPickVO
     * @param blogArticleDO
     * @return EditorPickVO
     */

     public static EditorPickVO toVO(EditorPickDO editorPick, BlogArticleDO article){
        if (editorPick == null || article == null) {
            return null;
        }
        EditorPickVO vo = new EditorPickVO();

        vo.setArticleId(editorPick.getArticleId());
        vo.setHeroType("Editor's Pick");
        vo.setCreateUserId(editorPick.getCreateUserId());
        vo.setCreateTime(editorPick.getCreateTime());

        vo.setTitle(article.getTitle());
        vo.setIsDeleted(article.getIsDeleted());
        vo.setUpdateTime(article.getUpdateTime());
        vo.setUpdateUserId(article.getUpdateUserId());
        
        return vo;
     }

     /**
      * 批量转换文章列表
      
      public static List<EditorPickVO> toVOList(List<BlogArticleDO> articles){
          if (articles == null || articles.isEmpty()) {
              return new ArrayList<>();
          }
          List<EditorPickVO> voList = new ArrayList<>(articles.size());
          for (BlogArticleDO article : articles) {
            voList.add(toVO(article));
          }
          return voList;
      }
    */
}
