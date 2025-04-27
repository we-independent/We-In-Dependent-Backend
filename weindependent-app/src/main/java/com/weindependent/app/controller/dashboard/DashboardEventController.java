package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.IDashboardEventService;
import com.weindependent.app.service.IEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    public void createEvent(@RequestBody @Valid EventQry eventQry)  {
        IDashboardEventService.createEvent(eventQry);
    }

    /**
     * 新增活動图片
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "新增博客banner图片")
    @PostMapping(value = "/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageDO addBlogBanner(@RequestParam("file") MultipartFile file) {
        return IDashboardEventService.insertEventBanner(file);
    }

    @Operation(summary = "Update an existing event")
    @SaCheckRole("admin")
    @SignatureAuth
    @PutMapping()
    public void updateEvent(@RequestBody @Valid EventDO event){
        IDashboardEventService.updateEvent(event);
    }

    @Operation(summary = "Delete an event")
    @SaCheckRole("admin")
    @SignatureAuth
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable @Valid Integer id)  {
        IDashboardEventService.deleteEvent(id);
    }
}
