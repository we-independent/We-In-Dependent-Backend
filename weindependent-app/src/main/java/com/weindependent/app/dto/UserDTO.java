package com.weindependent.app.dto;
import lombok.Data;

@Data
public class UserDTO {
  private Long id;
  private String account;
  private String email;
  private String realName;
  private String loginProvider;
  private boolean isNewUser;
}
