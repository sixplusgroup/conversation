package finley.gmair.job;

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
public class HourlyJob implements Job{

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
        airQualityFeignService.scheduleHourly();
        expressFeignService.updateOrderStatus();
        expressFeignService.updateParcelStatus();
        machineFeignService.handleMachineStatusHourly();
        machineFeignService.probePartialPM2_5Hourly();
        machineModeFeignService.handleHourlyPowerSaving();
    }
}
