package finley.gmair.job;

import finley.gmair.pool.TimingPool;
import finley.gmair.service.AirQualityFeignService;
import finley.gmair.service.MachineFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
public class HalfHourlyJob implements Job {

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private MachineFeignService machineFeignService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            airQualityFeignService.cityCrawler();
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            airQualityFeignService.monitorStationCrawler();
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            machineFeignService.powerTurnOnOff();
        }));
    }
}
