package finley.gmair.job;

import finley.gmair.pool.TimingPool;
import finley.gmair.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * hourly job executor
 */
@Component
public class HourlyJob implements Job {

    @Autowired
    private AirQualityFeignService airQualityFeignService;

    @Autowired
    private ExpressFeignService expressFeignService;

    @Autowired
    private MachineFeignService machineFeignService;

    @Autowired
    private MachineModeFeignService machineModeFeignService;

    @Autowired
    private DataAnalysisService dataAnalysisService;

//    @Autowired
//    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("start schedule hourly..");
//        Map<String, Object> condition = new HashMap<>();
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130hana2n87");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            airQualityFeignService.cityCrawler();
//            }
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI2018113052aewx61");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            airQualityFeignService.scheduleHourly();
//            }
        }));
//        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130ezixio31");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
//            expressFeignService.updateOrderStatus();
//            }
//        }));
//        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI2018113099ioov80");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
//            expressFeignService.updateParcelStatus();
//            }
//        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130222xa623");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            machineFeignService.handleMachineStatusHourly();
//            }
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI201811309xagf377");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            machineFeignService.probePartialPM2_5Hourly();
//            }
        }));
//        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130yo7yi341");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
//            machineModeFeignService.handleHourlyPowerSaving();
//            }
//        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20181130oeo6l479");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            machineFeignService.turnOffScreenHourly();
//            }
        }));
        TimingPool.getTimingExecutor().execute(new Thread(() -> {
//            condition.clear();
//            condition.put("taskId", "GTI20190109522w8i1");
//            boolean status = taskService.probeTaskStatus(condition);
//            if (status) {
            dataAnalysisService.statisticalDataHourly();
//            }
        }));

        TimingPool.getTimingExecutor().execute(new Thread(() -> {
            //高效滤网每小时更新数据
            machineFeignService.efficientFilterHourlyCheck();
        }));
    }
}
