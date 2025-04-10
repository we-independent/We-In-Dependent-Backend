package com.weindependent.app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.weindependent.app.database.dataobject.BlogArticleDO;
/**
 * @author Elly
 * 2025-04-13
 */
@Service
public interface SaveBlogService {
    void saveBlog(int userId, int blogId);
    void unsaveBlog(int userId, int blogId);
    List<BlogArticleDO> getSavedBlogs(int userId);

} 
 