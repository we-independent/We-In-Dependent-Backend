package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.GoogleUserDTO;
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

    public static GoogleUserDTO toGoogleUserDTOEntity(UserDO userDO, boolean isNewUser) {
        GoogleUserDTO googleUserDTO = new GoogleUserDTO();
        googleUserDTO.setId(userDO.getId());
        googleUserDTO.setRealName(userDO.getRealName());
        googleUserDTO.setAccount(userDO.getAccount());
        googleUserDTO.setNewUser(isNewUser);
        googleUserDTO.setLoginProvider(userDO.getLoginProvider());
        googleUserDTO.setLanguage(userDO.getLanguage());
        googleUserDTO.setVisaType(userDO.getVisaType());
        return googleUserDTO;
    }
}
