package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginQry {
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "remember is required")
    private String password;
    @NotNull(message = "remember is required")
    private Boolean remember;
}
