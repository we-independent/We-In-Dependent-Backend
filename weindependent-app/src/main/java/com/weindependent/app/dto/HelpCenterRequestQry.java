package com.weindependent.app.dto;
import lombok.Data;

/*
 * @Author Hurely
 * @Front End Sending Format
 */

@Data
public class HelpCenterRequestQry {
    private String subject;
    private String questionType;
    private String message;
}
