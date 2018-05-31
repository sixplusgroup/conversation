package finley.gmair.job;

import finley.gmair.service.AirQualityFeignService;
import finley.gmair.service.ExpressFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * hourly job executor
 */
@Component
public class HourlyJob implements Job{

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private ExpressFeignService expressFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("start schedule hourly..");
        airQualityFeignService.scheduleHourly();
        expressFeignService.updateOrderStatus();
        expressFeignService.updateParcelStatus();
    }
}
