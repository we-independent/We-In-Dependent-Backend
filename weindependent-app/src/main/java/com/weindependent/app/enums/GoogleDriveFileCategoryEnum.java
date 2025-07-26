package com.weindependent.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoogleDriveFileCategoryEnum {
    BLOG_BANNER("blog-banner"),
    BLOG_PDF("blog-pdf"),
    EVENT_BANNER("event-banner"),
    EVENT_SPEAKER_BANNER("event-speaker-banner"),
    USER_PROFILE_IMAGE("user-profile-image"),
    DONATE_VOLUNTEER_RESUME("donate-volunteer-resume");
    
    public final String folderName;
}
