package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BlogArticleAddQry {
    /** 文章ID */
    private Integer id;

    /** 文章来源类型 */
    private String articleSourceType;

    /** 原始文章链接 */
    private String sourceUrl;

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
    @NotNull
    private Integer categoryId;

    /** 0-未删除, 1-已删除 */
    @NotNull
    private Boolean isDeleted;

    private List<String> tags;
}
