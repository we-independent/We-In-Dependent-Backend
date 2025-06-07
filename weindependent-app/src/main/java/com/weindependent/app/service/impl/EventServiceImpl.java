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
import com.weindependent.app.vo.event.EventRegisterDetailVO;
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

import java.time.ZoneId;
import java.time.ZoneOffset;
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
    public List<RecentEventVO> getByMonth(int year, int month) {
        List<RecentEventVO> events = eventMapper.getByMonth(year,month);
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
    public EventRegisterDetailVO register(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        EventVO eventVO =eventMapper.getById(id, null);
        if (eventVO == null) {
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find event");
        }

        UserDO user= userService.findUserById(userId);
        if(user == null){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), "User does not exist");
        }

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

        Map<String, String> sendMailParams = new HashMap<>();
        sendMailParams.put("title", eventVO.getTitle());
        sendMailParams.put("username",user.getRealName());
        sendMailParams.put("link", eventVO.getLink());
        sendMailParams.put("time", eventVO.getEventTime().toString());
        sendMailParams.put("location", eventVO.getLocation());
        // sendMailParams.put("speaker", eventVO.getSpeakerName()); # TODO
        // emailService.send(user.getAccount(), MailTypeEnum.REGISTER_EVENT, sendMailParams);

        Map<String, String> adminMailParams = new HashMap<>();
        adminMailParams.put("name", user.getRealName());
        adminMailParams.put("email", user.getAccount());
        adminMailParams.put("title", eventVO.getTitle());
        adminMailParams.put("link", eventVO.getLink());
        adminMailParams.put("time", eventVO.getEventTime().toString());
        adminMailParams.put("location", eventVO.getLocation());
        adminMailParams.put("replyTo", user.getAccount());

        NotificationFieldEnum fieldEnum = NotificationFieldEnum.EVENTS_RSVP_CONFIRMATIONS;
        NotificationSettingsDO settingsDO = userNotificationMapper.findByUserId(userId);
        Boolean isEnabled = fieldEnum.isEnabled(settingsDO);

        if(isEnabled){
            emailService.send(user.getAccount(), MailTypeEnum.REGISTER_EVENT, sendMailParams);
        }


        EventRegisterDetailVO eventRegisterDetailVO = new EventRegisterDetailVO();
        if (eventVO.getLink() != null) {
            eventRegisterDetailVO.setLink(eventVO.getLink());
            eventRegisterDetailVO.setIcsText(generateIcsCalendarText(eventVO));
            eventRegisterDetailVO.setGoogleCalendarLink(generateGoogleCalendarLink(eventVO));
        }
        return eventRegisterDetailVO;
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
    public RecentEventVOs getBookmarkedEvents(int pageNum, int pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RecentEventVO> events = eventMapper.getBookmarkedEvents(userId);
        RecentEventVOs recentEventVOS = new RecentEventVOs();
        recentEventVOS.setEvents(events);
        recentEventVOS.setPages(page.getPages());
        return recentEventVOS;
    }

    private String generateGoogleCalendarLink(EventVO eventVO) {
        String startUtc = eventVO.getEventTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String endUtc = eventVO.getEventTime().plusHours(1).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String description = "Link: "+eventVO.getLink();

        return String.format(
                "https://calendar.google.com/calendar/render?action=TEMPLATE&text=%s&dates=%s/%s&details=%s&location=%s",
                eventVO.getTitle(), startUtc, endUtc,description, eventVO.getLocation()
        );
    }

    private String generateIcsCalendarText(EventVO eventVO) {
        String startUtc = eventVO.getEventTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String endUtc = eventVO.getEventTime().plusHours(1).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String description = "Link: "+eventVO.getLink();

        return String.format(
                "BEGIN:VCALENDAR\n" +
                        "VERSION:2.0\n" +
                        "BEGIN:VEVENT\n" +
                        "SUMMARY:%s\n" +
                        "DESCRIPTION:%s\n" +
                        "DTSTART:%s\n" +
                        "DTEND:%s\n" +
                        "LOCATION:%s\n" +
                        "END:VEVENT\n" +
                        "END:VCALENDAR",
                eventVO.getTitle(),
                description,
                startUtc,
                endUtc,
                eventVO.getLocation()
        );
    }
    @Override
    public List<EventVO> searchEventsNatural(String keyword) {
        return eventMapper.searchEventsNatural(keyword);
    }

    @Override
    public List<EventVO> searchEventsBoolean(String keyword) {
        return eventMapper.searchEventsBoolean(keyword);
    }

    @Async
    protected void recordUserViewEvent(int userId, long eventId) {
        try {
            eventMapper.recordUserViewEvent(userId, eventId);
        } catch (Exception e) {
            log.error("Failed to record event view: userId={}, eventId={}", userId, eventId, e);
        }
    }
}
