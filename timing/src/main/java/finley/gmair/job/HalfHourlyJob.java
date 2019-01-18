package finley.gmair.job;

import finley.gmair.pool.TimingPool;
import finley.gmair.service.AirQualityFeignService;
import finley.gmair.service.MachineFeignService;
import finley.gmair.service.TaskService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class HalfHourlyJob implements Job {

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private MachineFeignService machineFeignService;

    @Autowired
    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String, Object> condition = new HashMap<>();
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            condition.clear();
            condition.put("taskId", "GTI20181130hana2n87");
            boolean status = taskService.probeTaskStatus(condition);
            if (status) {
                airQualityFeignService.cityCrawler();
            }
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            condition.clear();
            condition.put("taskId", "GTI20181130gn56nl79");
            boolean status = taskService.probeTaskStatus(condition);
            if (status) {
                airQualityFeignService.monitorStationCrawler();
            }
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            condition.clear();
            condition.put("taskId", "GTI201811307oaxgf42");
            boolean status = taskService.probeTaskStatus(condition);
            if (status) {
                machineFeignService.powerTurnOnOff();
            }
        }));
    }

}
