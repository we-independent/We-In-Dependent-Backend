package com.weindependent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author zlc
 * @date 4.13
 */
@Data
@Entity
@Table(schema = "ym-paas-sso-oauth", name = "t_system_sob")
public class SysSob implements Serializable {
    private static final long serialVersionUID = 2421146070571125460L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "sob_code")
    private String sobCode;
    @Column(name = "sob_name")
    private String sobName;
}
