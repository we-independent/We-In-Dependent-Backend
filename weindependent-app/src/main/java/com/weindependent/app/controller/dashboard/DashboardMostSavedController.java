package com.weindependent.app.controller.dashboard;


import cn.dev33.satoken.annotation.SaCheckRole;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.service.MostSavedService;
import com.weindependent.app.vo.EditorPickVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Tag(name = "most saved管理")
@RestController
@RequestMapping("/api/dashboard/most-saved")
public class DashboardMostSavedController {

    @Resource
    private MostSavedService mostSavedService;

    @SignatureAuth
    @SaCheckRole("admin")
    @GetMapping("")
    public List<EditorPickVO> getEditorPickArticles() {
        return mostSavedService.getCurrentMostSavedEditorPickVO();
    }

    @SignatureAuth
    @SaCheckRole("admin")
    @GetMapping("/previous")
    public List<EditorPickVO> getPreviousEditorPickArticles() {
        return mostSavedService.getPreviousMostSavedEditorPickVO();
    }
}
