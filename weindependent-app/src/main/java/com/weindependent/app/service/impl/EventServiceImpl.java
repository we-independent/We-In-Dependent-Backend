package com.weindependent.app.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weindependent.app.database.mapper.weindependent.EventMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EventServiceImpl implements IEventService {

    @Resource
    private EventMapper eventMapper;

    @Override
    public List<RecentEventVO> getRecentEvents(int page, int size) {
        Integer userId =null;
        if(StpUtil.isLogin()){
            userId=StpUtil.getLoginIdAsInt();
        }
        PageHelper.startPage(page, size);
        List<RecentEventVO> recentEventVOS = eventMapper.getRecent(userId);
        if(recentEventVOS == null ){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find events");
        }
        return recentEventVOS;
    }



    @Override
    public EventVO getEventById(Long id) {
        Integer userId =null;
        if(StpUtil.isLogin()){
            userId=StpUtil.getLoginIdAsInt();
        }
        EventVO eventVO= eventMapper.getById(id,userId);
        if (eventVO == null) {
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find event");
        }
        if(userId != null){
            eventMapper.recordUserViewEvent(userId,id);
        }
        return eventVO;
    }

    @Override
    public String register(Long id) {
        int userId = StpUtil.getLoginIdAsInt();
        int affectedRow = 0;
        try {
            affectedRow = eventMapper.register(id, userId);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException &&
                    cause.getMessage() != null &&
                    cause.getMessage().contains("Duplicate entry")) {
                throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "User already registered for this event");
            }
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Cannot register event");
        }
        if(affectedRow == 0){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(), "Cannot find event");
        }
        EventVO eventVO =eventMapper.getById(id,null);
        return eventVO.getLink();
    }

    @Override
    public void unregister(Long id) {
        int userId = StpUtil.getLoginIdAsInt();
        if (eventMapper.unregister(id, userId) == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Event not found or user not registered for this event");
        }
    }

    @Override
    public void bookmark(Long id) {
        int userId = StpUtil.getLoginIdAsInt();
        int affectedRow = 0;
        try {
            affectedRow = eventMapper.bookmark(id, userId);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException &&
                    cause.getMessage() != null &&
                    cause.getMessage().contains("Duplicate entry")) {
                throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "User already bookmark this event");
            }
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Cannot bookmark event");
        }
        if(affectedRow == 0){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(), "Cannot find event");
        }
    }

    @Override
    public void unbookmark(Long id) {
        int userId = StpUtil.getLoginIdAsInt();
        if (eventMapper.unbookmark(id, userId) == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Event not found or user not bookmark this event");
        }
    }

    @Override
    public RecentEventVOs getRegisteredOngoingEvents(Integer pageNum, Integer pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getRegisteredOngoingEvents(userId);
        RecentEventVOs recentEventVOS = new RecentEventVOs();
        recentEventVOS.setEvents(events);
        recentEventVOS.setPages(page.getPages());
        return recentEventVOS;
    }

    @Override
    public RecentEventVOs getRegisteredPastEvents(Integer pageNum, Integer pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getRegisteredPastEvents(userId);
        RecentEventVOs recentEventVOS = new RecentEventVOs();
        recentEventVOS.setEvents(events);
        recentEventVOS.setPages(page.getPages());
        return recentEventVOS;
    }

    @Override
    public RecentEventVOs getViewedEvents(int pageNum, int pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getViewedEvents(userId);
        RecentEventVOs recentEventVOS = new RecentEventVOs();
        recentEventVOS.setEvents(events);
        recentEventVOS.setPages(page.getPages());
        return recentEventVOS;
    }

    @Override
    public RecentEventVOs getBookmarkedEvents(int pageNum, int pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getBookmarkedEvents(userId);
        RecentEventVOs recentEventVOS = new RecentEventVOs();
        recentEventVOS.setEvents(events);
        recentEventVOS.setPages(page.getPages());
        return recentEventVOS;
    }


}