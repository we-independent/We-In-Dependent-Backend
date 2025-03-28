package com.weindependent.app.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.vo.EditorPickVO;

public class EditorPickConvertor {
    /**
     * 将BlogAriticleDO转换为EditorPickVO
     * @param blogArticleDO
     * @return EditorPickVO
     */

     public static EditorPickVO toVO(BlogArticleDO article){
        if (article == null) {
            return null;
        }
        EditorPickVO vo = new EditorPickVO();
        BeanUtils.copyProperties(article, vo);
        return vo;
     }

     /**
      * 批量转换文章列表
      */
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

}
