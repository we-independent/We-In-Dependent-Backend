package com.weindependent.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zetor
 * @date 2019-02-11
 */
@Data
public class SysUserDto implements Serializable {
    private static final long serialVersionUID = 5872438942257394882L;
    private String username;
    private String password;
    private String realName;

    private String defaultOrgId;

    private String sob;

    private Integer status;

}
