package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.ImageDO;

import java.util.List;

public interface DashboardEventImageMapper {
    /**
     * 查询活動图片
     *
     * @param id 活動图片主键
     * @return 活動图片
     */
    public ImageDO findById(Integer id);

    /**
     * 查询活動图片列表
     *
     * @param image  活動图片
     * @return 活動图片集合
     */
    public List<ImageDO> query(ImageDO image);

    /**
     * 新增活動图片
     *
     * @param image 活動图片
     * @return 结果
     */
    public int insert(ImageDO image);

    /**
     * 修改活動图片
     *
     * @param image 活動图片
     * @return 结果
     */
    public int update(ImageDO image);

}
