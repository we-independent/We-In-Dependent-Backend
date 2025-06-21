package com.weindependent.app.database.mapper.weindependent;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.weindependent.app.dto.BlogArticleCardQry;
/**
 * @author Elly
 * 2025-04-17
 */
@Mapper
public interface SaveMapper {

    /*收藏文章 */
    int saveBlog(@Param("listId") int listId, @Param("blogId") int blogId);

    /*取消文章收藏 */
    int unsaveBlog(@Param("listId") int listId, @Param("blogId") int blogId);

    boolean blogIsSaved(@Param("userId")int userId, @Param("blogId") int blogId);

    /*显示收藏的所有文章 */
    List<BlogArticleCardQry> allSavedArticles(@Param("userId") int userId);

    int updateSaveListMetadata(@Param("listId") int listId, @Param("blogId") int blogId);

    boolean existBlogId(@Param("blogId") int blogId);

    boolean existEventId(@Param("eventId") int eventId);

    /* 收藏活动 */
    int saveEvent(@Param("userId") int userId, @Param("eventId") int eventId);

    /* 取消活动收藏 */
    int unsaveEvent(@Param("userId") int userId, @Param("eventId") int eventId);
}
