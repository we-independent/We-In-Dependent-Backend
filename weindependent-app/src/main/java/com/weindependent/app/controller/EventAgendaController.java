package com.weindependent.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.dto.EventAgendaCreateQry;
import com.weindependent.app.dto.EventAgendaUpdateQry;
import com.weindependent.app.service.IEventAgendaService;
import com.weindependent.app.vo.event.EventAgendaVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "活動議程")
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/event-agenda")
public class EventAgendaController {

    @Resource
    private IEventAgendaService eventAgendaService;

    @Operation(summary = "Get agenda by event ID")
    @SignatureAuth
    @GetMapping("/event/{eventId}")
    public EventAgendaVO getAgendaByEventId(@PathVariable Long eventId) {
        return eventAgendaService.getAgendaByEventId(eventId);
    }

    @Operation(summary = "Create agenda")
    @SignatureAuth
    @SaCheckLogin
    @PostMapping
    public EventAgendaVO createAgenda(@Valid @RequestBody EventAgendaCreateQry qry) {
        return eventAgendaService.createAgenda(qry);
    }

    @Operation(summary = "Update agenda")
    @SignatureAuth
    @SaCheckLogin
    @PutMapping
    public EventAgendaVO updateAgenda(@Valid @RequestBody EventAgendaUpdateQry qry) {
        return eventAgendaService.updateAgenda(qry);
    }

    @Operation(summary = "Delete agenda by ID")
    @SignatureAuth
    @SaCheckLogin
    @DeleteMapping("/{id}")
    public void deleteAgenda(@PathVariable Long id) {
        eventAgendaService.deleteAgenda(id);
    }

    @Operation(summary = "Delete agenda by event ID")
    @SignatureAuth
    @SaCheckLogin
    @DeleteMapping("/event/{eventId}")
    public void deleteAgendaByEventId(@PathVariable Long eventId) {
        eventAgendaService.deleteAgendaByEventId(eventId);
    }
} 