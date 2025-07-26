package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.DonateVolunteerDO;
import com.weindependent.app.dto.DonateVolunteerQry;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DonateVolunteerConverter {

    public DonateVolunteerDO toDO(DonateVolunteerQry qry) {
        DonateVolunteerDO volunteerDO = new DonateVolunteerDO();
        BeanUtils.copyProperties(qry, volunteerDO);
        volunteerDO.setInterests(String.join(",", qry.getInterests()));

        return volunteerDO;
    }
}

