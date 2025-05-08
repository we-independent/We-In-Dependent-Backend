package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.EventListQry;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.IDashboardEventService;
import com.weindependent.app.vo.event.dashboard.DashboardEventVOs;
import com.weindependent.app.vo.user.UserVOs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "博客活動管理")
@RestController
@RequestMapping("api/dashboard/event")
public class DashboardEventController {

    @Resource
    private IDashboardEventService IDashboardEventService;

    @Operation(summary = "Create a new event")
    @SaCheckRole("admin")
    @SignatureAuth
    @PostMapping()
    public EventDO create(@Valid @RequestBody EventQry eventQry)  {
        int userId = StpUtil.getLoginIdAsInt();
        return IDashboardEventService.create(eventQry);
    }

    @Operation(summary = "list all events")
    @SaCheckRole("admin")
    @SignatureAuth
    @PostMapping("/list")
    public DashboardEventVOs list(@Valid @RequestBody EventListQry eventListQry)  {
        return IDashboardEventService.list(eventListQry);
    }

    /**
     * 新增活動图片
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "新增博客banner图片")
    @PostMapping(value = "/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageDO createBanner(@RequestParam("file") MultipartFile file) {
        return IDashboardEventService.insertEventBanner(file);
    }

    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "上傳講者圖片")
    @PostMapping(value = "/speaker/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageDO uploadSpeakerBanner(@RequestParam("file") MultipartFile file) {
        return IDashboardEventService.insertSpeakerBanner(file);
    }

    @Operation(summary = "Update an existing event")
    @SaCheckRole("admin")
    @SignatureAuth
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid EventQry event){
        IDashboardEventService.update(id,event);
    }

    @Operation(summary = "Delete an event")
    @SaCheckRole("admin")
    @SignatureAuth
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable @Valid List<Long> ids)  {
        IDashboardEventService.delete(ids);
    }


    @Operation(summary = "Get all user that registered for an event")
    @SaCheckRole("admin")
    @SignatureAuth
    @GetMapping("/user/registered/{id}")
    public UserVOs getRegisteredUsers(@PathVariable Long id,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return IDashboardEventService.getRegisteredUsers(id, page, size);
    }

    @Operation(summary = "Get all user that bookmarked this event")
    @SaCheckRole("admin")
    @SignatureAuth
    @GetMapping("/user/bookmark/{id}")
    public UserVOs getBookmarkedUsers(@PathVariable Long id,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return IDashboardEventService.getBookmarkedUsers(id, page, size);
    }
}
