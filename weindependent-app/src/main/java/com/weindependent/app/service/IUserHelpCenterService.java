package com.weindependent.app.service;
import java.util.List;

import com.weindependent.app.dto.HelpCenterRequestQry;
import com.weindependent.app.vo.HelpCenterRequestVO;

public interface IUserHelpCenterService {
    void saveHelpRequest(Long userId, HelpCenterRequestQry qry);
    List<HelpCenterRequestVO> getHelpRequestHistory(Long userId);
}
