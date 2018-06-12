package finley.gmair.job;

import finley.gmair.service.AirQualityFeignService;
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
public class DailyJob implements Job{

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private MachineFeignService machineFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        airQualityFeignService.scheduleDaily();
        machineFeignService.handleMachineStatusDaily();
    }
}
