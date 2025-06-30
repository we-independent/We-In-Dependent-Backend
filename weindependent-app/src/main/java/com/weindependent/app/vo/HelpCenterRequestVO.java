package com.weindependent.app.vo;
import lombok.Data;
import java.time.LocalDateTime;

/*
 * @Author Hurely
 * @For user checking their help history
*/

@Data
public class HelpCenterRequestVO {

    /* 请求标题 */
    private String subject;

    /* 请求内容 */
    private String message;

    /* 请求时间 */
    private LocalDateTime createTime;

}
