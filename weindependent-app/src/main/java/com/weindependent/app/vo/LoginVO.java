package com.weindependent.app.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.Data;

@Data
public class LoginVO {
    private SaTokenInfo saTokenInfo;
    private String email;
    private String username;
    private String language;
    private String visaType;
    private boolean subscription;
}
