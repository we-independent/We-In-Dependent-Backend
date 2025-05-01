package com.weindependent.app.vo.event;

import lombok.Data;

@Data
public class RecentEventVO {
    private Long id;
    private String title;
    private String summary;
    private String eventTime;
    private String bannerUrl;
}
