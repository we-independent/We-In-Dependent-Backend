package com.weindependent.app.database.dataobject;

import lombok.Data;
import java.util.Date;

@Data
public class EditorPickDO {
    private Integer id;
    private Integer articleId;
    private Date createTime;
    private Integer createUserId;
    private Integer status; // 0:无效/已删除, 1:有效
    private Date updateTime;        
    private Integer updateUserId; 
}

