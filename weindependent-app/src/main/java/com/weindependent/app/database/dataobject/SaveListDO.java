package com.weindependent.app.database.dataobject;

import java.time.LocalDateTime;

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
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime lastEditedTime;
    private String lastEditedImage;

    private Integer articleCount;
}
