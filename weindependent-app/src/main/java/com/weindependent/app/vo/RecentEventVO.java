package com.weindependent.app.vo;

import lombok.Data;

@Data
public class RecentEventVO {
    private Long id;
    private String title;
    private String description;
    private String eventStartTime;
    private String bannerUrl;
}
