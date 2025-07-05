package com.weindependent.app.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.EventMapper;
import com.weindependent.app.database.mapper.weindependent.UserNotificationMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.enums.NotificationFieldEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IEmailService;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.service.UserService;
import com.weindependent.app.vo.event.EventSpeakerVO;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.management.NotificationBroadcaster;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EventServiceImpl implements IEventService {

    @Resource
    private EventMapper eventMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private IEmailService emailService;
    @Autowired
    private UserNotificationMapper userNotificationMapper;

    @Override
    public RecentEventVOs getUpcomingEvents(int page,int size) {
        Integer userId =null;
        if(StpUtil.isLogin()){
            userId=StpUtil.getLoginIdAsInt();
        }
        Page p = PageHelper.startPage(page, size);
        List<RecentEventVO> events = eventMapper.getUpcoming(userId);
        if(events == null ){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find events");
        }
        RecentEventVOs recentEventVOs = new RecentEventVOs();
        recentEventVOs.setEvents(events);
        recentEventVOs.setPages(p.getPages());
        return recentEventVOs;
    }

    @Override
    public List<RecentEventVO> getUpcomingByMonth(int year, int month) {
        Integer userId =null;
        if(StpUtil.isLogin()){
            userId=StpUtil.getLoginIdAsInt();
        }
        List<RecentEventVO> events = eventMapper.getUpcomingByMonth(userId,year,month);
        if(events == null ){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find events");
        }
        return events;

    }

    @Override
    public RecentEventVOs getPastEvents(int page, int size) {
        Integer userId =null;
        if(StpUtil.isLogin()){
            userId=StpUtil.getLoginIdAsInt();
        }

        Page p = PageHelper.startPage(page, size);
        List<RecentEventVO> events = eventMapper.getPast(userId);
        if(events == null ){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find events");
        }

        RecentEventVOs recentEventVOs = new RecentEventVOs();
        recentEventVOs.setEvents(events);
        recentEventVOs.setPages(p.getPages());
        return recentEventVOs;
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

        // NEW: fetch and set speaker list
        List<EventSpeakerVO> speakers = eventMapper.getSpeakersByEventId(id);
        eventVO.setSpeakers(speakers);

        if (userId != null) {
            recordUserViewEvent(userId, id);
        }
        else{
            eventVO.setVideoLink(null);   //prevent not logged in user from accessing the video.
        }
        return eventVO;
    }

    @Override
    public void register(Long id, String userTimeZone) {
        Long userId = StpUtil.getLoginIdAsLong();

        int affectedRow = 0;
        try {
            affectedRow = eventMapper.register(id, userId);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException &&
                    cause.getMessage() != null &&
                    cause.getMessage().contains("Duplicate entry")) {
                throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "User already registered for this event"); //Throw exception when user already registered
            }
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Cannot register event"); // Unknown exception
        }
        if(affectedRow == 0){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(), "Cannot find event");
        }

        sendRegisterConfirmationEmail(id,userId, userTimeZone);
    }

    @Override
    @Async
    public void sendRegisterConfirmationEmail(Long eventId, Long userId, String userTimeZone) {
        EventVO eventVO =eventMapper.getById(eventId, null);

        if (eventVO == null) {
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find event");
        }

        UserDO user= userService.findUserById(userId);
        if(user == null){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), "User does not exist");
        }

        Map<String, String> sendMailParams = new HashMap<>();
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(userTimeZone != null ? userTimeZone : "America/New_York");
        } catch (DateTimeException e) {
            zoneId = ZoneId.of("America/New_York"); // fallback if user passed an invalid string
        }
        String formattedTime = eventVO.getEventTime().atZone(zoneId)
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a (z)"));

        sendMailParams.put("title", eventVO.getTitle());
        sendMailParams.put("username",user.getRealName());
        sendMailParams.put("link", eventVO.getLink());
        sendMailParams.put("time", formattedTime);
        sendMailParams.put("location", eventVO.getLocation());
        // sendMailParams.put("speaker", eventVO.getSpeakerName()); # TODO
        // emailService.send(user.getAccount(), MailTypeEnum.REGISTER_EVENT, sendMailParams);

        NotificationFieldEnum fieldEnum = NotificationFieldEnum.EVENTS_EVENT_REMINDER;
        NotificationSettingsDO settingsDO = userNotificationMapper.findByUserId(userId);
        Boolean isEnabled = fieldEnum.isEnabled(settingsDO);

        if(isEnabled){
            emailService.send(user.getAccount(), MailTypeEnum.REGISTER_EVENT, sendMailParams);
        }
        // emailService.send(user.getAccount(), MailTypeEnum.REGISTER_EVENT, sendMailParams);
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
    public RecentEventVOs getRegisteredUpcomingEvents(Integer pageNum, Integer pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getRegisteredUpcomingEvents(userId);
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
    public RecentEventVOs getBookmarkedPastEvents(int pageNum, int pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getBookmarkedPastEvents(userId);
        RecentEventVOs recentEventVOS = new RecentEventVOs();
        recentEventVOS.setEvents(events);
        recentEventVOS.setPages(page.getPages());
        return recentEventVOS;
    }

    @Override
    public RecentEventVOs getBookmarkedUpcomingEvents(int pageNum, int pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getBookmarkedUpcomingEvents(userId);
        RecentEventVOs recentEventVOS = new RecentEventVOs();
        recentEventVOS.setEvents(events);
        recentEventVOS.setPages(page.getPages());
        return recentEventVOS;
    }

    @Override
    public RecentEventVOs searchEventsNatural(String keyword, int pageNum, int pageSize) {
        List<RecentEventVO> events = eventMapper.searchEventsNatural(keyword);
        Page page = PageHelper.startPage(pageNum, pageSize);
        RecentEventVOs result = new RecentEventVOs();
        result.setEvents(events);
        result.setPages(page.getPages());
        return result;
    }

    @Override
    public RecentEventVOs searchEventsBoolean(String keyword, int pageNum, int pageSize) {
        List<RecentEventVO> events = eventMapper.searchEventsBoolean(keyword);
        Page page = PageHelper.startPage(pageNum, pageSize);
        RecentEventVOs result = new RecentEventVOs();
        result.setEvents(events);
        result.setPages(page.getPages());
        return result;
    }

    @Async
    protected void recordUserViewEvent(int userId, long eventId) {
        try {
            eventMapper.recordUserViewEvent(userId, eventId);
        } catch (Exception e) {
            log.error("Failed to record event view: userId={}, eventId={}", userId, eventId, e);
        }
    }

    @Override
    public RecentEventVOs filterPastEventsByTags(List<Integer> filter, int pageNum, int pageSize) {
        List<RecentEventVO> events = eventMapper.getPastEventsFiltered(filter);
        Page page = PageHelper.startPage(pageNum, pageSize);

        if (events == null || events.isEmpty()) {
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(), "No events found");
        }

        RecentEventVOs result = new RecentEventVOs();
        result.setEvents(events);
        result.setPages(page.getPages());
        return result;
    }

    @Override
    public boolean isRegistered(Long eventId) {
        int userId = StpUtil.getLoginIdAsInt();
        return eventMapper.isRegistered (eventId,userId);
    }
}
