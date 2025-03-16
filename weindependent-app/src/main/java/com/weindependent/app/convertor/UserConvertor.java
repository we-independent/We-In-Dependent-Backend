package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.GoogleUserVO;
import com.weindependent.app.vo.UserVO;

public class UserConvertor {

    public static UserVO toUserVOEntity(UserDO userDO) {
        UserVO userVO = new UserVO();
        userVO.setUsername(userDO.getAccount());
        userVO.setName(userDO.getRealName());
        return userVO;
    }

    public static GoogleUserVO toUserDTOEntity(UserDO userDO, boolean isNewUser) {
        GoogleUserVO googleUserVO = new GoogleUserVO();
        googleUserVO.setId(userDO.getId());
        googleUserVO.setRealName(userDO.getRealName());
        googleUserVO.setEmail(userDO.getEmail());
        googleUserVO.setAccount(userDO.getAccount());
        googleUserVO.setNewUser(isNewUser);
        googleUserVO.setLoginProvider(userDO.getLoginProvider());
        return googleUserVO;
    }
}
