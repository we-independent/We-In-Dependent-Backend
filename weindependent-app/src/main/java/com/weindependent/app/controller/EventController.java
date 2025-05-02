package com.weindependent.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "活動")
@Slf4j
@RestController
@RequestMapping(value = "/api/event")
public class EventController {

    @Resource
    private IEventService IEventService;

    @Operation(summary = "Get recent events")
    @SignatureAuth
    @GetMapping("/recent")
    public List<RecentEventVO> getRecentEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getRecentEvents(page, size);
    }


    @Operation(summary = "Get event by ID")
    @SignatureAuth
    @GetMapping("/{id}")
    public EventVO getEventById(@PathVariable Long id) {
        return IEventService.getEventById(id);
    }

    @Operation(summary = "Register an event by ID")
    @SignatureAuth
    @PostMapping("/register/{id}")
    public void register(@PathVariable Long id) {IEventService.registerEvent(id);}

    @Operation(summary = "Unregister an event by ID")
    @SignatureAuth
    @DeleteMapping("/register/{id}")
    public void unregister(@PathVariable Long id) {IEventService.unregisterEvent(id);}

}
