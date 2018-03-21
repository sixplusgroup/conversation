package finley.gmair.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WechatScheduler {

    /**
     * This method will renew the access token once an hour
     * and will be saved to database
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void renewal() {

    }
}
