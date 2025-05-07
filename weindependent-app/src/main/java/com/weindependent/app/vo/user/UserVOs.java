package com.weindependent.app.vo.user;

import com.weindependent.app.database.dataobject.UserDO;
import lombok.Data;

import java.util.List;

@Data
public class UserVOs {
    private Integer pages;
    private List<UserDO> users;
}
