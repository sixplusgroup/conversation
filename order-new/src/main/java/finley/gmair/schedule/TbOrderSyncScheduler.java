package finley.gmair.schedule;

import finley.gmair.service.TbOrderSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:02
 * @description ：Synchronize orders from taobao
 */

@Component
public class TbOrderSyncScheduler {
    @Autowired
    private TbOrderSyncService tbOrderSyncServiceImpl;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void test() {
        System.out.println(System.currentTimeMillis());
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void incrementalImport() {
        //tbOrderSyncServiceImpl.incrementalImport();
    }
}
