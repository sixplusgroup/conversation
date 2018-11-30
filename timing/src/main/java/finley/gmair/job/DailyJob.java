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

/**
 * daily job executor
 */

@Component
public class DailyJob implements Job {

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
            condition.put("taskId", "GTI20181130afy5g342");
            boolean status = taskService.probeTaskStatus(condition);
            if (status) {
                airQualityFeignService.scheduleDaily();
            }
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            condition.clear();
            condition.put("taskId", "GTI20181130e9ul9n27");
            boolean status = taskService.probeTaskStatus(condition);
            if (status) {
                machineFeignService.handleMachineStatusDaily();
            }
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            condition.clear();
            condition.put("taskId", "GTI20181130oyi5ae39");
            boolean status = taskService.probeTaskStatus(condition);
            if (status) {
                machineFeignService.savePartialPm25Daily();
            }
        }));
    }
}
