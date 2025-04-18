package com.weindependent.app.dto;

import lombok.Data;

/**
 * @author Elly
 * 2025-04-17
 */

@Data
public class SaveBlogQry {
    private String listName;
    private Integer listId; //optional when save/unsave blog
}
