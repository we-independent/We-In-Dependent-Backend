package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.UserVO;
import com.weindependent.app.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor {

    public static UserVO toUserVOEntity(UserDO userDO) {
        UserVO userVO = new UserVO();
        userVO.setUsername(userDO.getAccount());
        userVO.setName(userDO.getRealName());
        return userVO;
    }

    public UserDTO toUserDTOEntity(UserDO userDO, boolean isNewUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDO.getId());
        userDTO.setRealName(userDO.getRealName());
        userDTO.setEmail(userDO.getEmail());
        userDTO.setAccount(userDO.getAccount());
        userDTO.setNewUser(isNewUser);
        userDTO.setLoginProvider(userDO.getLoginProvider());
        return userDTO;
    }
}
