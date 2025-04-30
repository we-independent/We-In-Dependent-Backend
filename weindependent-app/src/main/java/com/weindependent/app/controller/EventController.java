package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.RecentEventVO;
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
    public EventVO getEventById(@PathVariable Integer id) {
        return IEventService.getEventById(id);
    }

}
