package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.user.GoogleUserVO;
import com.weindependent.app.vo.user.UserVO;
import com.weindependent.app.dto.UpdateUserQry;
import org.springframework.beans.BeanUtils;

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

    public static UserDO toUserDO(Long userId, UpdateUserQry updateUserQry) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(updateUserQry,userDO);
        userDO.setId(userId);
        return userDO;
    }

    public static GoogleUserVO toGoogleUserVO(UserDO userDO, boolean isNewUser) {
        GoogleUserVO googleUserDTO = new GoogleUserVO();
        BeanUtils.copyProperties(userDO,googleUserDTO);
        googleUserDTO.setNewUser(isNewUser);
        return googleUserDTO;
    }
}
