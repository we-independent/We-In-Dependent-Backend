package com.weindependent.app.dto;
import com.weindependent.app.enums.CategoryEnum;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Match 前端 blogcard
 * 
 * @author Hurely
 *    2025-04-5
 */

@Data
public class BlogArticleCardQry {

    private Integer id;
    // 文章的title
    private String title;
    // updateTime 直接作为 eventime 返回前端
    private LocalDateTime time;
    // 根据文章内容字数计算出的阅读时长s
    private String readingTime;
    // 固定图片 URL（如需动态设置，可通过 db 联表查询）
    private String imageUrl;
    // category 直接使用 db 中的值，如需联表查询名称则在 service 层做转换
    private String category;
    // category name
    private String categoryName;
    // 编辑推荐状态
    private Boolean editorsPick;
    // 收藏次数
    private Integer savedCount;

    //跳过lombok的set，自己define Category name
    public void setCategory(String category) {
        this.category = category;
        this.categoryName = CategoryEnum.getNameByCode(category);
    }
}
