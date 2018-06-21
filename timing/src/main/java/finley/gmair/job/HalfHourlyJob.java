package finley.gmair.job;

import finley.gmair.model.air.AirQuality;
import finley.gmair.service.AirQualityFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HalfHourlyJob implements Job{

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        airQualityFeignService.cityCrawler();
        airQualityFeignService.monitorStationCrawler();
    }
}
