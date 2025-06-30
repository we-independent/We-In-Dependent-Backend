package com.weindependent.app.dto;

import lombok.Data;

@Data
public class ResetPasswordQry {
    private String token;
    private String password;
}