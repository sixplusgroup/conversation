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
 * monthly job executor
 */
@Component
public class MonthlyJob implements Job {

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private MachineFeignService machineFeignService;

//    @Autowired
//    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        Map<String, Object> condition = new HashMap<>();
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130zxz3li46");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            airQualityFeignService.scheduleMonthly();
//            }
        }));

        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130uiow9716");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            machineFeignService.handleMachineStatusMonthly();
//            }
        }));
    }
}
