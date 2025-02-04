package com.weindependent.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

/**
 * @author zetor
 * @date 2019-02-11
 */
@Data
@Entity
@Table(schema = "ym-paas-sso-oauth", name = "t_system_user")
public class SysUser implements Serializable  {
    private static final long serialVersionUID = 5872438942257394882L;
    @Id
    @Column(name = "account")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "default_org_id")
    private String defaultOrgId;

    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "create_account")
    private String createAccount;

    @Column(name = "create_real_name")
    private String createRealName;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_account")
    private String modifyAccount;

    @Column(name = "modify_real_name")
    private String modifyRealName;

    @Column(name = "modify_time")
    private Timestamp modifyTime;
}
