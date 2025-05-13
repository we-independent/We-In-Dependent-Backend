package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendMailQry {
    @NotBlank(message = "email is mandatory")
    private String email;
}
