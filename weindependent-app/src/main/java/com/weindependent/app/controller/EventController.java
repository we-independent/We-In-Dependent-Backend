package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;
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

    @Operation(
        summary = "Register an event by ID",
        description = "Returns an link of the event"
    )
    @SignatureAuth
    @PostMapping("/register/{id}")
    public String register(@PathVariable Long id) { return IEventService.register(id);}

    @Operation(summary = "Unregister an event by ID")
    @SignatureAuth
    @DeleteMapping("/register/{id}")
    public void unregister(@PathVariable Long id) {IEventService.unregister(id);}

    @Operation(summary = "Bookmark an event by ID")
    @SignatureAuth
    @PostMapping("/bookmark/{id}")
    public void bookmark(@PathVariable Long id) {IEventService.bookmark(id);}

    @Operation(summary = "Unbookmark an event by ID")
    @SignatureAuth
    @DeleteMapping("/bookmark/{id}")
    public void unbookmark(@PathVariable Long id) {IEventService.unbookmark(id);}

    @Operation(summary = "List All registered ongoing events 已註冊活動列表")
    @SignatureAuth
    @GetMapping("/registered/ongoing")
    public RecentEventVOs getRegisteredOngoingEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getRegisteredOngoingEvents(page,size);
    }

    @Operation(summary = "List all registered past events 已參加活動列表")
    @SignatureAuth
    @GetMapping("/registered/past")
    public RecentEventVOs getRegisteredPastEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getRegisteredPastEvents(page,size);
    }

    @Operation(summary = "List All bookmarked events")
    @SignatureAuth
    @GetMapping("/bookmarked")
    public RecentEventVOs getBookMarkedEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getBookmarkedEvents(page,size);
    }

    @Operation(summary = "List all viewed  events ")
    @SignatureAuth
    @GetMapping("/viewed")
    public RecentEventVOs getViewedEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getViewedEvents(page,size);
    }

    @Operation(summary = "Search events using full-text search in natural language mode")
    @SignatureAuth
    @GetMapping("/search/natural")
    public List<EventVO> searchEventsNatural(@RequestParam String keyword) {
        return IEventService.searchEventsNatural(keyword);
    }

    @Operation(summary = "Search events using full-text search in boolean mode")
    @SignatureAuth
    @GetMapping("/search/boolean")
    public List<EventVO> searchEventsBoolean(@RequestParam String keyword) {
        return IEventService.searchEventsBoolean(keyword);
    }
}
