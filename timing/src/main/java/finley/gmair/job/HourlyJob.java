package finley.gmair.job;

import finley.gmair.pool.CorePool;
import finley.gmair.service.AirQualityFeignService;
import finley.gmair.service.ExpressFeignService;
import finley.gmair.service.MachineFeignService;
import finley.gmair.service.MachineModeFeignService;
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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("start schedule hourly..");
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            airQualityFeignService.scheduleHourly();
        }));
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            expressFeignService.updateOrderStatus();
        }));
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            expressFeignService.updateParcelStatus();
        }));
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            machineFeignService.handleMachineStatusHourly();
        }));
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            machineFeignService.probePartialPM2_5Hourly();
        }));
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            machineModeFeignService.handleHourlyPowerSaving();
        }));
        CorePool.getTimingExecutor().execute(new Thread(() -> {
            machineFeignService.turnOffScreenHourly();
        }));
    }
}
