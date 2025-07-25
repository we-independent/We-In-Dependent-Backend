package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.dto.EventSpeakerQry;
import com.weindependent.app.service.IDashboardSpeakerService;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard/speaker")
@Tag(name = "Speaker管理")
public class DashboardSpeakerController {

    @Resource
    private IDashboardSpeakerService dashboardSpeakerService;

    @Operation(summary = "Upload speaker banner")
    @SaCheckRole("admin")
    @PostMapping(value = "/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadBanner(@RequestParam("file") MultipartFile file) {
        return dashboardSpeakerService.uploadBanner(file);
    }

    @Operation(summary = "Create speaker")
    @SaCheckRole("admin")
    @PostMapping()
    public void create(@Valid @RequestBody EventSpeakerQry qry) {
        dashboardSpeakerService.create(qry);
    }

    @Operation(summary = "Update speaker by speaker ID")
    @SaCheckRole("admin")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EventSpeakerQry qry) {
        dashboardSpeakerService.update(id, qry);
    }

    @Operation(summary = "Delete speaker(s) by speaker ID(s)")
    @SaCheckRole("admin")
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable List<Long> ids) {
        dashboardSpeakerService.delete(ids);
    }

    @Operation(summary = "List all speakers")
    @SaCheckRole("admin")
    @GetMapping("/list")
    public List<DashboardSpeakerVO> list() {
        return dashboardSpeakerService.list();
    }

    @Operation(summary = "Get speaker by speaker ID")
    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public DashboardSpeakerVO getById(@PathVariable Long id) {
        return dashboardSpeakerService.getById(id);
    }

    @Operation(summary = "Search speaker by full name")
    @SaCheckRole("admin")
    @GetMapping("/search")
    public List<DashboardSpeakerVO> searchByName(@RequestParam String name) {
        return dashboardSpeakerService.searchByName(name);
    }

    @Operation(summary = "Search speaker by first name")
    @SaCheckRole("admin")
    @GetMapping("/search/first-name")
    public List<DashboardSpeakerVO> searchByFirstName(@RequestParam String firstName) {
        return dashboardSpeakerService.searchByFirstName(firstName);
    }

    @Operation(summary = "Search speaker by last name")
    @SaCheckRole("admin")
    @GetMapping("/search/last-name")
    public List<DashboardSpeakerVO> searchByLastName(@RequestParam String lastName) {
        return dashboardSpeakerService.searchByLastName(lastName);
    }

}
