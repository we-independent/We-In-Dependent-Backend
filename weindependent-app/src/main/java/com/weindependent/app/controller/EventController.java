package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.EventService;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.RecentEventVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "活動管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/event", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public class EventController {

    @Resource
    private EventService eventService;

    @Operation(summary = "Get recent events")
    @SignatureAuth
    @GetMapping("/recent")
    public List<RecentEventVO> getRecentEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return eventService.getRecentEvents(page, size);
    }

    @Operation(summary = "Create a new event")
    @SignatureAuth
    @PostMapping()
    public void createEvent(@RequestBody EventQry eventQry)  {
        eventService.createEvent(eventQry);
    }

    @Operation(summary = "Get event by ID")
    @SignatureAuth
    @GetMapping("/{id}")
    public EventVO getEventById(@PathVariable String id) {
        return eventService.getEventById(id);
    }

    @Operation(summary = "Update an existing event")
    @SignatureAuth
    @PutMapping()
    public void updateEvent(@RequestBody EventDO event){
         eventService.updateEvent(event);
    }

    @Operation(summary = "Delete an event")
    @SignatureAuth
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id)  {
        eventService.deleteEvent(id);
    }
}
