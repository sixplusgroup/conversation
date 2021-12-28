package finley.gmair.job;

import finley.gmair.pool.TimingPool;
import finley.gmair.service.AirQualityFeignService;
import finley.gmair.service.MachineFeignService;
import finley.gmair.service.OrderCentreFeignService;
import finley.gmair.service.OrderNewFeignService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HalfHourlyJob implements Job {

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private MachineFeignService machineFeignService;

    @Autowired
    private OrderNewFeignService orderNewFeignService;

    @Autowired
    private OrderCentreFeignService orderCentreFeignService;

//    @Autowired
//    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        Map<String, Object> condition = new HashMap<>();
//        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130gn56nl79");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
//                airQualityFeignService.monitorStationCrawler();
//            }
//        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI201811307oaxgf42");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            machineFeignService.powerTurnOnOff();
//            }
        }));

        TimingPool.getTimingExecutor().execute(new Thread(() -> orderNewFeignService.incrementalImport()));

        TimingPool.getTimingExecutor().execute(new Thread(() -> orderCentreFeignService.schedulePullAll()));
    }

}
