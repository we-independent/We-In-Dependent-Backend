package com.weindependent.app.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BlogArticlePdfListQry {

    private String fileName; // 模糊匹配
    private LocalDate pdfStartDate;
    private LocalDate pdfEndDate;
    private LocalDate createStartDate;
    private LocalDate createEndDate;
    private LocalDate updateStartDate;
    private LocalDate updateEndDate;
    private Boolean isDeleted;
    private Integer createUserId;
    private Integer articleId;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
