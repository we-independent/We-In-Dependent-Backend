package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogImageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 博客图片Mapper接口
 * @author nero
 */
@Mapper
public interface DashboardBlogImageMapper
{
    /**
     * 查询博客图片
     * 
     * @param id 博客图片主键
     * @return 博客图片
     */
    public BlogImageDO findById(Integer id);

    /**
     * 查询博客图片列表
     * 
     * @param blogImage 博客图片
     * @return 博客图片集合
     */
    public List<BlogImageDO> query(BlogImageDO blogImage);

    /**
     * 新增博客图片
     * 
     * @param blogImage 博客图片
     * @return 结果
     */
    public int insert(BlogImageDO blogImage);

    /**
     * 修改博客图片
     * 
     * @param blogImage 博客图片
     * @return 结果
     */
    public int update(BlogImageDO blogImage);

    /**
     * 删除博客图片, 软删除
     *
     * @param id 博客图片主键
     * @return 结果
     */
//    public int deleteById(Integer id);

    /**
     * 批量删除博客图片, 软删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
//    public int deleteByIds(Integer[] ids);
}
