package com.weindependent.app.vo.event;

import lombok.Data;

import java.time.Instant;

@Data
public class RecentEventVO {
    private Long id;
    private String title;
    private String summary;
    private String location;
    private Instant eventTime;
    private Integer duration;
    private String bannerUrl;
    private boolean bookmarked;
}
