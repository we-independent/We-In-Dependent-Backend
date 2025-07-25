package com.weindependent.app.dto;

import com.weindependent.app.enums.NotificationFieldEnum;

import lombok.Data;

@Data
public class UpdateNotificationFieldQry {
    private Long userId;  
    private NotificationFieldEnum field;
    private Boolean fieldValue;
}
