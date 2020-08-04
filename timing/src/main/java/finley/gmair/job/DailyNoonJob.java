package finley.gmair.job;

import finley.gmair.pool.TimingPool;
import finley.gmair.service.DriftFeignService;
import finley.gmair.service.MachineFeignService;
import finley.gmair.service.TaskService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * daily job executor
 */

@Component
public class DailyNoonJob implements Job {

    @Autowired
    private MachineFeignService machineFeignService;

    @Autowired
    private DriftFeignService driftFeignService;

//    @Autowired
//    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        Map<String, Object> condition = new HashMap<>();
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130ex72fi16");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            machineFeignService.turnOnScreenDaily();
//            }
        }));

        TimingPool.getTimingExecutor().execute(() -> driftFeignService.orderReturnMessage());

        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            //初效滤网每天中午更新数据
            machineFeignService.filterCleanDailyCheck();
        }));
    }
}
