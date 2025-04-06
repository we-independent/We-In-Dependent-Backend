package com.weindependent.app.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 博客文章pdf对象 blog_pdf
 * 
 * @author christina
 *    2025-03-23
 */
@Data
public class BlogPdfQry
{

    /** Pdf信息ID */
    private Integer id;

    /** 物理文件名 */
    private String fileName;

    /** pdf文件生成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date pdfFileGenerationTime;

    /** 评论者ID */
    private Long articleId;

    /** 0-未删除, 1-已删除 */
    private Integer isDeleted;

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
}
