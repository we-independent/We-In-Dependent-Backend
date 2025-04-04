package com.weindependent.app.dto;

import lombok.Data;

@Data
public class SendMailQry {
    private int templateId;
    private String email;
}
