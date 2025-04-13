package com.weindependent.app.convertor;

import java.util.Date;
import java.time.ZoneId;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.EditorPickDO;
import com.weindependent.app.vo.EditorPickVO;

public class EditorPickConvertor {

    /**
     * 将 BlogArticleDO + EditorPickDO 转换为 EditorPickVO
     */
    public static EditorPickVO toVO(EditorPickDO editorPick, BlogArticleDO article) {
        if (editorPick == null || article == null) {
            return null;
        }
    
        EditorPickVO vo = new EditorPickVO();
    
        vo.setArticleId(editorPick.getArticleId());
        vo.setHeroType("Editor's Pick");
        vo.setCreateUserId(editorPick.getCreateUserId());
    
        // Date → Date 直接赋值
        if (editorPick.getCreateTime() != null) {
            vo.setCreateTime(editorPick.getCreateTime());
        }
    
        vo.setTitle(article.getTitle());
    
        // Boolean → Integer（0/1）
        vo.setIsDeleted(article.getIsDeleted() != null && article.getIsDeleted() ? 1 : 0);
    
        // LocalDateTime → Date 转换
        if (article.getUpdateTime() != null) {
            vo.setUpdateTime(Date.from(article.getUpdateTime()
                    .atZone(ZoneId.systemDefault()).toInstant()));
        }
    
        vo.setUpdateUserId(article.getUpdateUserId());
    
        return vo;
    }
}
