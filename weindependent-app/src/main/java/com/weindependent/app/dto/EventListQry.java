package com.weindependent.app.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class EventListQry {
    private String title;
    private Long speakerUserId;
    private String location;
    private Integer categoryId;
    private Integer createUserId;
    private LocalDateTime eventTimeStart;
    private LocalDateTime eventTimeEnd;
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;
    private Boolean isDeleted;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer offset;
}
