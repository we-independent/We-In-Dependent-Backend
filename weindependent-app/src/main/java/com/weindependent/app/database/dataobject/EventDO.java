package com.weindependent.app.database.dataobject;

import java.time.Instant;
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
    private Instant eventTime;
    private Integer duration;
    private String location;
    // private Long speakerUserId;
    private Integer tagId;
    private String link;
    private String videoLink;
    private String bannerUrl;
    private boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUserId;
    private Long updateUserId;
}
