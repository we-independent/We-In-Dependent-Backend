package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.UserVO;

import java.util.ArrayList;
import java.util.List;

public class UserConvertor {

    public static List<UserVO> toUserVOListEntity(List<UserDO> userDOList) {
        List<UserVO> userVOList = new ArrayList<>();
        for (UserDO userDO : userDOList) {
            UserVO userVO = new UserVO();
            userVO.setUsername(userDO.getAccount());
            userVO.setName(userDO.getRealName());
            userVOList.add(userVO);
        }
        return userVOList;
    }
}
