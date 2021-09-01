package finley.gmair.scene.scheduled;

import finley.gmair.scene.dto.AppMsgPushDTO;
import finley.gmair.scene.service.AppMsgPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author : Lyy
 * @create : 2021-01-15 15:29
 * @description 定时任务
 **/
@Slf4j
@Component
public class ScheduledTask {

    @Resource
    AppMsgPushService service;

    /**
     * 每天上午8点，向App推送天气信息
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void weatherScheduledTask() {
        log.info("开始推送天气信息：{}", LocalDateTime.now());
        String title = "天气信息";
        String content = "天气内容";
        AppMsgPushDTO appMsgPush = new AppMsgPushDTO();
        appMsgPush.setTitle(title);
        appMsgPush.setContent(content);
        appMsgPush.setAlias(service.getPushUsers());
        service.sendPush(appMsgPush);
        log.info("推送结束：{}", LocalDateTime.now());
    }
}
