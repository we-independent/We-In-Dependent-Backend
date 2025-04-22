package com.weindependent.app.database.dataobject;

import lombok.Data;
/**
 * @author Elly
 * 2025-04-17
 */
@Data
public class SaveListDO {
    private Integer listId;
    private Integer userId;
    private String name;
    private Boolean isDefault;
}
