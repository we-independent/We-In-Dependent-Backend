package com.weindependent.app.dto;

import lombok.Data;

@Data
public class UpdateNotificationFieldQry {
    private String fieldName;
    private Boolean fieldValue;
}
