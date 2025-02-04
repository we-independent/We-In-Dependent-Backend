package com.weindependent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zetor
 * @date 2019-02-12
 */
@Data
@Entity
@Table(schema = "ym-paas-sso-oauth", name = "t_sys_user_role")
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = -1810195806444298544L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "account")
    private String account;

}
