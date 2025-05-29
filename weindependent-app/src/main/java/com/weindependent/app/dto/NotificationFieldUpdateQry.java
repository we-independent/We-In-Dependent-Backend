package com.weindependent.app.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NotificationFieldUpdateQry { 
    @NotNull(message = "userid is required")  
    private Long userId;
    private String fieldName;
    private Boolean fieldValue;
}
