package finley.gmair.job;

import finley.gmair.pool.CorePool;
import finley.gmair.service.MachineFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * daily job executor
 */

@Component
public class DailyNoonJob implements Job {

    @Autowired
    private MachineFeignService machineFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            machineFeignService.turnOnScreenDaily();
        }));
    }
}
