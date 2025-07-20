package com.weindependent.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;

import com.weindependent.app.dto.EventAgendaItemCreateQry;
import com.weindependent.app.dto.EventAgendaItemUpdateQry;
import com.weindependent.app.service.IEventAgendaItemService;
import com.weindependent.app.vo.event.EventAgendaItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "活動議程項目")
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/event-agenda-item")
public class EventAgendaItemController {

    @Resource
    private IEventAgendaItemService eventAgendaItemService;

    @Operation(summary = "Get agenda items by agenda ID")
    @SaCheckRole("admin")
    @GetMapping("/agenda/{agendaId}")
    public List<EventAgendaItemVO> getItemsByAgendaId(@PathVariable Long agendaId) {
        return eventAgendaItemService.getItemsByAgendaId(agendaId);
    }

    @Operation(summary = "Get agenda item by ID")
    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public EventAgendaItemVO getItemById(@PathVariable Long id) {
        return eventAgendaItemService.getItemById(id);
    }

    @Operation(summary = "Create agenda item")
    //@SignatureAuth
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping
    public EventAgendaItemVO createItem(@Valid @RequestBody EventAgendaItemCreateQry qry) {
        return eventAgendaItemService.createItem(qry);
    }

    @Operation(summary = "Update agenda item")
    @SaCheckLogin
    @SaCheckRole("admin")
    @PutMapping
    public EventAgendaItemVO updateItem(@Valid @RequestBody EventAgendaItemUpdateQry qry) {
        return eventAgendaItemService.updateItem(qry);
    }

    @Operation(summary = "Delete agenda item by ID")
    @SaCheckLogin
    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        eventAgendaItemService.deleteItem(id);
    }

    @Operation(summary = "Update agenda item sort order")
    @SaCheckLogin
    @SaCheckRole("admin")
    @PutMapping("/{id}/sort-order")
    public void updateItemSortOrder(@PathVariable Long id, @RequestParam Integer sortOrder) {
        eventAgendaItemService.updateItemSortOrder(id, sortOrder);
    }
} 