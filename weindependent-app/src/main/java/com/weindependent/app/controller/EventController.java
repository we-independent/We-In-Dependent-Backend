package com.weindependent.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.event.EventRegisterDetailVO;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.List;

@Tag(name = "活動")
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/event")
public class EventController {

    @Resource
    private IEventService IEventService;

    @Operation(summary = "Get upcoming events")
    @GetMapping("/upcoming")
    public RecentEventVOs getUpcomingEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getUpcomingEvents(page,size);
    }

    @Operation(summary = "Search or filter past events with keyword, mode, or filters")
    @GetMapping("/past")
    public RecentEventVOs getPast(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<Integer> filter) {

        return IEventService.getPast(page, size, keyword, filter);
    }


    @Operation(summary = "Get upcoming events by month")
    @GetMapping("/upcoming/month")
    public List<RecentEventVO> getUpcomingByMonth(
            @RequestParam @NotNull @Min(1900) @Max(2100) Integer year,
            @RequestParam @NotNull @Min(1) @Max(12) Integer month
    ) {
        return IEventService.getUpcomingByMonth(year, month);
    }

    @Operation(summary = "Get event by ID")
    @GetMapping("/{id}")
    public EventVO getEventById(@PathVariable Long id) {
        return IEventService.getEventById(id);
    }

    @Operation(summary = "Register an event by ID")
    @SaCheckLogin
    @PostMapping("/register/{id}")
    public EventRegisterDetailVO register(@PathVariable Long id , @RequestParam(required = false) String userTimeZone ) {
        ZoneId zoneId= IEventService.getZoneIdByUserTimeZone(userTimeZone);
        return IEventService.register(id,zoneId);
    }

    @Operation(summary = "Unregister an event by ID")
    @SaCheckLogin
    @DeleteMapping("/register/{id}")
    public void unregister(@PathVariable Long id) {IEventService.unregister(id);}

    @Operation(summary = "Bookmark an event by ID")
    @SaCheckLogin
    @PostMapping("/bookmark/{id}")
    public void bookmark(@PathVariable Long id) {IEventService.bookmark(id);}

    @Operation(summary = "Unbookmark an event by ID")
    @SaCheckLogin
    @DeleteMapping("/bookmark/{id}")
    public void unbookmark(@PathVariable Long id) {IEventService.unbookmark(id);}

    @Operation(summary = "List All registered upcoming events 已註冊活動列表")
    @SaCheckLogin
    @GetMapping("/registered/upcoming")
    public RecentEventVOs getRegisteredUpcomingEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getRegisteredUpcomingEvents(page,size);
    }

    @Operation(summary = "List all registered past events 已參加活動列表")
    @SaCheckLogin
    @GetMapping("/registered/past")
    public RecentEventVOs getRegisteredPastEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getRegisteredPastEvents(page,size);
    }

    @Operation(summary = "List All bookmarked past events")
    @SaCheckLogin
    @GetMapping("/bookmarked/past")
    public RecentEventVOs getBookMarkedPastEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getBookmarkedPastEvents(page,size);
    }

    @Operation(summary = "List All bookmarked upcoming events")
    @SaCheckLogin
    @GetMapping("/bookmarked/upcoming")
    public RecentEventVOs getBookMarkedUpcomingEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getBookmarkedUpcomingEvents(page,size);
    }

    @Operation(summary = "List all viewed events ")
    @SaCheckLogin
    @GetMapping("/viewed")
    public RecentEventVOs getViewedEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return IEventService.getViewedEvents(page,size);
    }


    @Operation(summary = "Resend register event email")
    @SaCheckLogin
    @PostMapping("/resend-register-confirmation-email/{eventId}")
    public void resendConfirmationEmail(@PathVariable Long eventId, @RequestParam(required = false) String userTimeZone ) throws Exception {
        if(!IEventService.isRegistered(eventId)){
            throw new Exception("Event not found, or user not registered to this event");
        }
        Long userId =StpUtil.getLoginIdAsLong();
        ZoneId zoneId = IEventService.getZoneIdByUserTimeZone(userTimeZone);
        EventVO event = IEventService.getEventById(eventId);
        String googleCalendarLink = IEventService.generateGoogleCalendarLink(event, zoneId);
        IEventService.sendRegisterConfirmationEmail(userId,zoneId, event, googleCalendarLink);
    }
}
