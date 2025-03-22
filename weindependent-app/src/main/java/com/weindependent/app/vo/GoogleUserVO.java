package com.weindependent.app.vo;
import lombok.Data;

@Data
public class GoogleUserVO {
  private Long id;
  private String account;
  private String userName;
  private String loginProvider;
  private boolean isNewUser;
  private String language;
  private String visaType;
}
