package com.weindependent.app.dto;

import lombok.Data;
import java.util.List;

@Data
public class EventFilterQry {
    private List<Integer> tagIds;
}