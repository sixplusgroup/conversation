package finley.gmair.Schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class statusSchedule {

    /**
     *This method is used to update express status every hour
     *
     */
    @Scheduled(cron = "59 * * * * ?")
    public void update(){
        System.out.println();
    }
}
