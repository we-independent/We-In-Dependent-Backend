package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.UserVO;

public class UserConvertor {

    public static UserVO toUserVOEntity(UserDO userDO) {
        UserVO userVO = new UserVO();
        userVO.setUsername(userDO.getAccount());
        userVO.setName(userDO.getRealName());
        return userVO;
    }
}
