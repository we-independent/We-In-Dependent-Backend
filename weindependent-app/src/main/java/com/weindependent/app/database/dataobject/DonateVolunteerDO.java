package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 志愿者申请对象 donate_volunteer
 *
 * @author yourname
 * @date 2025-07-25
 */
@Data
public class DonateVolunteerDO {

    /** 申请ID */
    private Long id;

    /** 姓名 */
    private String fullName;

    /** 邮箱 */
    private String email;

    /** 电话号码 */
    private String phoneNumber;

    /** 兴趣，逗号分隔字符串 */
    private String interests;

    /** 是否需要OPT支持 */
    private Boolean needsOPT;

    /** 需要OPT开始时间，字符串表示 */
    private String optStartTime;

    /** 申请动机 */
    private String motivation;

    /** 额外信息 */
    private String additionalInfo;

    /** 简历访问链接 */
    private String resumeUrl;

    /** 创建时间 */
    private LocalDateTime createTime;

}
