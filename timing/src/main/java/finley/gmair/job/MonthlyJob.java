package finley.gmair.job;

import finley.gmair.pool.CorePool;
import finley.gmair.service.AirQualityFeignService;
import finley.gmair.service.MachineFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * monthly job executor
 */
@Component
public class MonthlyJob implements Job {

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private MachineFeignService machineFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            airQualityFeignService.scheduleMonthly();
        }));

        CorePool.getTimingExecutor().execute(new Thread(() -> {
            machineFeignService.handleMachineStatusMonthly();
        }));
    }
}
