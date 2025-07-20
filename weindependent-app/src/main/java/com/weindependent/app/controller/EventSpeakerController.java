package com.weindependent.app.controller;

import com.weindependent.app.service.IEventSpeakerService;
import com.weindependent.app.vo.event.EventSpeakerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/speaker")
@Tag(name = "Speaker")
public class EventSpeakerController {

    @Resource
    private IEventSpeakerService eventSpeakerService;

    @Operation(summary = "Get speaker by speaker ID")
    @GetMapping("/{id}")
    public EventSpeakerVO getById(@PathVariable Long id) {
        return eventSpeakerService.getById(id);
    }

    @Operation(summary = "Search speakers by name")
    @GetMapping("/search")
    public List<EventSpeakerVO> searchByName(@RequestParam String name) {
        return eventSpeakerService.searchByName(name);
    }
}

