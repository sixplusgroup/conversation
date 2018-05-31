package finley.gmair.job;

import finley.gmair.service.AirQualityFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * monthly job executor
 */
@Component
public class MonthlyJob implements Job{

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        airQualityFeignService.scheduleMonthly();
    }
}
