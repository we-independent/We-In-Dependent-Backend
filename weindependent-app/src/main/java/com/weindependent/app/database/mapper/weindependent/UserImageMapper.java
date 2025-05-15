package com.weindependent.app.database.mapper.weindependent;
import org.apache.ibatis.annotations.Mapper;
import com.weindependent.app.database.dataobject.ImageDO;

import java.util.List;

@Mapper
public interface UserImageMapper {
  public int create(ImageDO imageDO);

  public int update(ImageDO image);

  public int delete(java.util.List<Long> ids);

}
