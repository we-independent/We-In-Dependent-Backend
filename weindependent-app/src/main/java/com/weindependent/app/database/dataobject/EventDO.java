package com.weindependent.app.database.dataobject;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime eventTime;
    private String location;
    private Long speakerUserId;
    private Integer categoryId;
    private String link;
    private Long bannerId;
    private boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUserId;
    private Long updateUserId;
}
