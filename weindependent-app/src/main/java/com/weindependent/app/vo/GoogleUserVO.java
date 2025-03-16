package com.weindependent.app.vo;
import lombok.Data;

@Data
public class GoogleUserVO {
  private Long id;
  private String account;
  private String email;
  private String realName;
  private String loginProvider;
  private boolean isNewUser;
}
