package com.weindependent.app.config;

import com.weindependent.app.service.MostSavedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@Configuration
public class TaskConfig {

    @Resource
    MostSavedService mostSavedService;

    @Scheduled(cron = "0 1 0 1,15 * ? ")
    public void task1() {
        log.info("setMostSavedEveryTwoWeeks task start:{}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            try {
                int resultInt = mostSavedService.setMostSavedEveryTwoWeeks();
                if (resultInt == 2) {
                    log.info("setMostSavedEveryTwoWeeks  completed, added two Most Saved records.");
                } else if (resultInt == -1) {
                    log.info("abort setMostSavedEveryTwoWeeks, Most Saved has already updated");
                } else {
                    log.error("setMostSavedEveryTwoWeeks, unknown error {}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                }
            }catch (Exception e){
                log.error("setMostSavedEveryTwoWeeks {}, Exception Message: {}, StackTrace :{}",
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
                        e.getMessage(),
                        e.getStackTrace()
                      );
            }
        log.info("setMostSavedEveryTwoWeeks task end:{}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
    }
}