package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.EventTagDO;
import com.weindependent.app.service.IEventTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Tag(name = "活動標籤管理")
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/event-tag")
public class EventTagController {

    @Resource
    private IEventTagService IEventTagService;

    @Operation(summary = "Get all event tags")
    @SignatureAuth
    @GetMapping()
    public List<EventTagDO> getAllTags() {
        return IEventTagService.getAllTags();
    }
}
