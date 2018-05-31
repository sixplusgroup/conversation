package finley.gmair.job;

import finley.gmair.service.AirQualityFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * hourly job executor
 */
@Component
public class HourlyJob implements Job{

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        airQualityFeignService.scheduleHourly();
    }
}
