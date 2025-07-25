package com.weindependent.app.vo.event.dashboard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardSpeakerVO {
    private Long id;
    private String firstName;
    private String lastName;
    private String title;
    private String background;
    private String description;
    private String bannerUrl;
}
