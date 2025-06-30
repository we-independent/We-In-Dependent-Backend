package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.EventListQry;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.IDashboardEventService;
import com.weindependent.app.vo.event.dashboard.DashboardEventVO;
import com.weindependent.app.vo.event.dashboard.DashboardEventVOs;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;
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
        return IDashboardEventService.create(eventQry);
    }

    @Operation(summary = "list all events")
    @SaCheckRole("admin")
    @SignatureAuth
    @PostMapping("/list")
    public DashboardEventVOs list(@Valid @RequestBody EventListQry eventListQry)  {
        return IDashboardEventService.list(eventListQry);
    }

    @Operation(summary = "get an event by id")
    @SaCheckRole("admin")
    @SignatureAuth
    @GetMapping("/{id}")
    public DashboardEventVO getById(@PathVariable Long id)  {
        return IDashboardEventService.getById(id);
    }

    /**
     * 新增活動图片
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "新增博客banner图片")
    @PostMapping(value = "/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createBanner(@RequestParam("file") MultipartFile file) {
        return IDashboardEventService.insertEventBanner(file);
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

    @Operation(summary = "Assign speakers to an event")
    @SaCheckRole("admin")
    @SignatureAuth
    @PostMapping("/{eventId}/speakers")
    public void addSpeakersToEvent(@PathVariable Long eventId, @RequestBody List<Long> speakerIds) {
        IDashboardEventService.addSpeakersToEvent(eventId, speakerIds);
    }

    @Operation(summary = "Update speakers of an event (replace existing ones)")
    @SaCheckRole("admin")
    @SignatureAuth
    @PutMapping("/{eventId}/speakers")
    public void updateSpeakersOfEvent(@PathVariable Long eventId, @RequestBody List<Long> speakerIds) {
        IDashboardEventService.updateSpeakersOfEvent(eventId, speakerIds);
    }

    @Operation(summary = "Get speakers of an event")
    @SaCheckRole("admin")
    @SignatureAuth
    @GetMapping("/{eventId}/speakers")
    public List<DashboardSpeakerVO> getSpeakersByEventId(@PathVariable Long eventId) {
        return IDashboardEventService.getSpeakersByEventId(eventId);
    }

}
