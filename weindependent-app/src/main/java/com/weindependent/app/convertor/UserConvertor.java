package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.GoogleUserVO;
import com.weindependent.app.vo.UserVO;

public class UserConvertor {

    public static UserVO toUserVOEntity(UserDO userDO) {
        UserVO userVO = new UserVO();
        userVO.setUsername(userDO.getAccount());
        userVO.setName(userDO.getRealName());
        userVO.setLanguage(userDO.getLanguage());
        userVO.setVisaType(userDO.getVisaType());
        userVO.setSubscription(userDO.isSubscription());
        return userVO;
    }

    public static GoogleUserVO toGoogleUserVOEntity(UserDO userDO, boolean isNewUser) {
        GoogleUserVO googleUserVO = new GoogleUserVO();
        googleUserVO.setId(userDO.getId());
        googleUserVO.setUserName(userDO.getRealName());
        googleUserVO.setAccount(userDO.getAccount());
        googleUserVO.setNewUser(isNewUser);
        googleUserVO.setLoginProvider(userDO.getLoginProvider());
        googleUserVO.setLanguage(userDO.getLanguage());
        googleUserVO.setVisaType(userDO.getVisaType());
        return googleUserVO;
    }
}
