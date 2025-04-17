package com.weindependent.app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.weindependent.app.database.dataobject.BlogArticleDO;
/**
 * @author Elly
 * 2025-04-13
 */

public interface SaveBlogService {
    int saveBlog(int userId, int blogId);
    int unsaveBlog(int userId, int blogId);
    List<BlogArticleDO> getSavedBlogs(int userId);
    int createList(int userId, String listName);
    boolean existList(int userId, String listName);

} 
 