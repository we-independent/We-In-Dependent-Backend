package com.weindependent.app.vo.event;

import lombok.Data;

@Data
public class EventSpeakerVO {
    private Long userId;
    private String name;            // From t_system_user.real_name
    private String title;
    private String background;
    private String description;
    private String bannerUrl;       // From event_speaker_image.file_path
}
