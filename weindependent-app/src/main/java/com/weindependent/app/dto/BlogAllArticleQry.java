package com.weindependent.app.dto;
import lombok.Data;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Match 前端 blogcard
 * 
 * @author Hurely
 *    2025-04-5
 */
@Data
@Schema(description = "返回给前端的文章列表字段结构")
public class BlogAllArticleQry {
    
    @Schema(description = "文章ID", example = "1")
    private Integer id;
    
    @Schema(description = "文章标题", example = "Visa & Policy")
    private String title;
    
    @Schema(description = "文章简要描述", example = "Policy Changes: Will It Be Harder to Apply?")
    private String description;
    
    @Schema(description = "文章时间")
    private LocalDateTime time;
    
    @Schema(description = "作者", example = "We Independent")
    private String author;
    
    @Schema(description = "阅读时长", example = "7 min")
    private String readingTime;
    
    @Schema(description = "图片URL", example = "BlogArticleImage1")
    private String imageUrl;
    
    @Schema(description = "文章对应链接", example = "/blogs/visa-policy/policy-changes-2025")
    private String url;
    
    @Schema(description = "分类", example = "Visa & Policy")
    private String category;
    
    @Schema(description = "是否为编辑推荐", example = "true")
    private Boolean editorsPick;
    
    @Schema(description = "文章被收藏的次数", example = "45")
    private Integer savedCount;

       /** 文章来源类型 */
       private String articleSourceType;

       /** 原始文章链接 */
       private String sourceUrl;
   
       /** 作者ID */
       private Integer authorId;
   
   
       /** 文章状态 */
       private String articleStatus;
   
       /** 文章分类 */
       private Integer categoryId;
   
       /** 0-未删除, 1-已删除 */
       private Boolean isDeleted;
   
       /** 创建人 */
       private Integer createUserId;
   
       /** 创建时间 */
       private LocalDateTime createTime;
   
       /** 最后更新人 */
       private Integer updateUserId;
   
       /** 最后更新时间 */
       private LocalDateTime  updateTime;
   
       private  Integer pageNum;
   
       private  Integer pageSize;
       
       /** 排序 */
       private String orderBy;

}
