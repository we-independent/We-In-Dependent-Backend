package com.weindependent.app.service;


import com.weindependent.app.vo.BlogHomePageHeroVO;
import com.weindependent.app.vo.EditorPickVO;

import java.util.List;

public interface MostSavedService {

    List<BlogHomePageHeroVO>  getCurrentBlogHomePageHeroVO();

    List<EditorPickVO> getCurrentMostSavedEditorPickVO();

    List<EditorPickVO> getPreviousMostSavedEditorPickVO();
}
