package com.weindependent.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;

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
import java.util.HashMap;
import java.util.Map;

@Tag(name = "活動議程")
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/event-agenda")
public class EventAgendaController {

    @Resource
    private IEventAgendaService eventAgendaService;

    @Operation(summary = "测试接口 - 不需要认证")
    @GetMapping("/test")
    public Map<String, Object> testAgenda() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Agenda测试接口正常");
        result.put("success", true);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @Operation(summary = "Get agenda by event ID")
    // @SignatureAuth
    @GetMapping("/event/{eventId}")
    public EventAgendaVO getAgendaByEventId(@PathVariable Long eventId) {
        return eventAgendaService.getAgendaByEventId(eventId);
    }

    @Operation(summary = "Create agenda")
    @SignatureAuth
    @SaCheckLogin
    @SaCheckRole("admin") 
    @PostMapping
    public EventAgendaVO createAgenda(@Valid @RequestBody EventAgendaCreateQry qry) {
        return eventAgendaService.createAgenda(qry);
    }

    @Operation(summary = "Update agenda")
    @SignatureAuth
    @SaCheckLogin
    @SaCheckRole("admin") 
    @PutMapping
    public EventAgendaVO updateAgenda(@Valid @RequestBody EventAgendaUpdateQry qry) {
        return eventAgendaService.updateAgenda(qry);
    }

    @Operation(summary = "Delete agenda by ID")
    @SignatureAuth
    @SaCheckLogin
    @SaCheckRole("admin") 
    @DeleteMapping("/{id}")
    public void deleteAgenda(@PathVariable Long id) {
        eventAgendaService.deleteAgenda(id);
    }

    @Operation(summary = "Delete agenda by event ID")
    @SignatureAuth
    @SaCheckLogin
    @SaCheckRole("admin") 
    @DeleteMapping("/event/{eventId}")
    public void deleteAgendaByEventId(@PathVariable Long eventId) {
        eventAgendaService.deleteAgendaByEventId(eventId);
    }
} 