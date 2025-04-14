package com.weindependent.app.database.dataobject;

public class SavedCountDO {
    private Integer articleId;
    private Integer savedCount;

    public Integer getArticleId() {
        return articleId;
    }
    
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    
    public Integer getSavedCount() {
        return savedCount;
    }
    
    public void setSavedCount(Integer savedCount) {
        this.savedCount = savedCount;
    }
}
