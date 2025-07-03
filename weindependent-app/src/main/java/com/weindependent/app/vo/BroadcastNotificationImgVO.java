package com.weindependent.app.vo;

import lombok.Data;

@Data
public class BroadcastNotificationImgVO {
    private String imgType;  //后端图片位置识别
    private String position; //前端图片位置提示
    private int count; //图片计数
}
