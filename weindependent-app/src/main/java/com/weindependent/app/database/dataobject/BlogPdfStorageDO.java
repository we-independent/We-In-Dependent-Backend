package com.weindependent.app.database.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客PDF存储对象，用于存储 PDF 二进制数据及相关元数据
 * 对应数据库中的表，blog_pdf_storage
 * 
 * @author Hurely
 *    2025-03-23
 */
@Data
public class BlogPdfStorageDO {
    
    /** 自增主键 */
    private Integer id;
    
    /** 博客文章ID（外键关联博客文章表） */
    private Integer blogId;
    
    /** PDF文件二进制数据（用于BLOB存储） */
    private byte[] pdfContent;
    
    /** PDF物理文件名（用于记录文件名称） */
    private String fileName;
    
    /** PDF生成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime pdfGenerationTime;
    
    /** 0-未删除, 1-已删除 */
    private Integer isDeleted;
    
    /** 创建人ID */
    private Integer createUserId;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    
    /** 最后更新人ID */
    private Integer updateUserId;
    
    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
