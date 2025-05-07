package com.weindependent.app.vo.event.dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class DashboardEventVOs {
    private List<DashboardEventVO> events;
    private Integer pages;
}
