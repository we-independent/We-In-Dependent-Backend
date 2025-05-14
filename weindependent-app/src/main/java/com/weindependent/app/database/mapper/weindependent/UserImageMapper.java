package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.ImageDO;

public interface UserImageMapper {
  public int create(ImageDO imageDO);

  public int update(ImageDO image);

  public int delete(java.util.List<Long> ids);

}
