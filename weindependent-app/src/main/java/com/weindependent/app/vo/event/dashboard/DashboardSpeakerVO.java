package com.weindependent.app.vo.event.dashboard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardSpeakerVO {
    private Long id;
    // private Long userId;
    // private String userName;        // From t_system_user.real_name
    private String firstName;
    private String lastName;
    private String title;
    private String background;
    private String description;
    private Long bannerId;
    private String bannerUrl;       // From event_speaker_image.file_path
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
