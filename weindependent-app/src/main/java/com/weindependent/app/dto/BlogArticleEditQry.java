package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BlogArticleEditQry {

    /** 文章ID */
    @NotNull(message = "Blog ID can not be null")
    private Integer id;

    /** 文章来源类型 */
    private String articleSourceType;

    /** 原始文章链接 */
    private String sourceUrl;

    /** 原始文章链接显示文字 */
    private String sourceUrlDisplayText;
    
    /** 作者ID */
    private Integer authorId;

    /** bannerID */
    private Integer bannerImgId;

    /** 文章摘要 */
    private String summary;

    /** 文章标题 */
    @NotNull
    private String title;

    /** 文章内容 */
    @NotNull
    private String content;

    /** 文章状态
     * */
    private String articleStatus;

    /** 文章分类 */
    @NotNull(message = "分类 ID（categoryId）不能为空")
    private Integer categoryId;

    /** 0-未删除, 1-已删除 */
    // @NotNull(message = "是否删除（isDeleted）不能为空")
    private Boolean isDeleted;

    private List<Integer>  tags;
}
