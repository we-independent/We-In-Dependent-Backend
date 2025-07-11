package com.weindependent.app.vo;

import lombok.Data;
import java.util.Date;

@Data
public class UpdateProgressRequestVO {
    private Integer step; // 1, 2, or 3
}
