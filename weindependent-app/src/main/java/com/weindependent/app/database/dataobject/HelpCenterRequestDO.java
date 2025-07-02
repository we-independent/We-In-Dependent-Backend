package com.weindependent.app.database.dataobject;

import lombok.Data;
import java.time.LocalDateTime;


/*
 * @author Hurely
 * @for user profile help center 
 */

@Data
public class HelpCenterRequestDO {

    private Long id;

    /* 发送请求的userId */
    private Long userId;

    /* 发送请求的用户姓名 */
    private String name;

    /* 发送请求的用户email */
    private String email;

    /* 发送请求的标题 */
    private String subject;

    /* 发送请求的内容 */
    private String message;

    /* 发送请求的时间 */
    private LocalDateTime createTime;
}
