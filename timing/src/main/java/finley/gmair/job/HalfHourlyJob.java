package finley.gmair.job;

import finley.gmair.model.timing.Task;
import finley.gmair.pool.TimingPool;
import finley.gmair.service.AirQualityFeignService;
import finley.gmair.service.MachineFeignService;
import finley.gmair.service.TaskService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
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
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            airQualityFeignService.cityCrawler();
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            airQualityFeignService.monitorStationCrawler();
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            Map<String, Object> condition = new HashMap<>();
            condition.put("blockFlag", false);
            condition.put("taskName", "poweronoff");
            ResultData response = taskService.fetch(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                return;
            }
            boolean status = ((List<Task>) response.getData()).get(0).isStatus();
            if (status) {
                machineFeignService.powerTurnOnOff();
            }
        }));
    }
}
