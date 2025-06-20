package com.weindependent.app.vo.websiteoperation;

import com.weindependent.app.vo.TagVO;
import lombok.Data;

import java.util.List;

@Data
public class BlogHeroVO {
    private Integer id;
    private String heroType;
    private Integer articleId;
    private String articleTitle;
    private Integer categoryId;
    private String categoryName;
    private String  bannerImageUrl;
    private List<TagVO> tagVOList;
}
