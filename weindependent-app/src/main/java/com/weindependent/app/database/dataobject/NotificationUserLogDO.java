package com.weindependent.app.database.dataobject;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationUserLogDO {
    private Long id;
    private Long notificationId;
    private Long userId;
    private LocalDateTime sentAt;
    private String status; // "success" or "failed"
    private String errorMessage;
    private String email;
}
