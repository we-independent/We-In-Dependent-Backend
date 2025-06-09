package com.weindependent.app.vo.event;


import lombok.Data;

import java.util.List;

@Data
public class RecentEventVOs {
    private List<RecentEventVO> events;
    private Integer pages;
}
