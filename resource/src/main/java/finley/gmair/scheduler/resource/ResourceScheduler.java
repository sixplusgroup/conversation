package finley.gmair.scheduler.resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ResourceScheduler {

    @Scheduled(cron = "0/5 * * * * ?")
    public void deleteUselessPic() {
        System.out.println("start delete useless pic");
        System.out.println("deleting");
        System.out.println("over!");
    }
}
