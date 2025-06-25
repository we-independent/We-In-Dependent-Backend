package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.ImageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardEventImageMapper {
   public int create(EventDO eventDO);


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

    public int delete(List<Long> ids);

  /**
   * 標記活動图片idDelete = false
   *
   * @param bannerId 活動图片id
   * @return 结果成功與否
   */
    public int markUsed(Long bannerId);

}
