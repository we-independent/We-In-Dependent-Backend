package com.weindependent.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.dto.EventSpeakerQry;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;

public interface IDashboardSpeakerService {
    void create(EventSpeakerQry qry);
    void update(Long id, EventSpeakerQry qry);
    void delete(List<Long> ids);
    List<DashboardSpeakerVO> list();
    DashboardSpeakerVO getById(Long id);
    String uploadBanner(MultipartFile file);

    List<DashboardSpeakerVO> searchByName(String name);
    List<DashboardSpeakerVO> searchByFirstName(String firstName);
    List<DashboardSpeakerVO> searchByLastName(String lastName);
}
