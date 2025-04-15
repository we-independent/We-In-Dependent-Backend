package com.weindependent.app.service;


import com.weindependent.app.vo.EditorPickVO;

import java.util.List;

public interface MostSavedService {

    List<EditorPickVO> getCurrentMostSavedArticleVO();
    List<EditorPickVO> getCurrentMostSavedEditorPickVO();

    List<EditorPickVO> getPreviousMostSavedEditorPickVO();
}
