package finley.gmair.controller;

import finley.gmair.model.machine.*;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/partial/status")
public class MachinePartialStatusController {
    @Autowired
    private MachinePm25Service machinePm25Service;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private CoreV2Service coreV2Service;

    @Autowired
    private OutPm25DailyService outPm25DailyService;

    @Autowired
    private FilterLimitConfigService filterLimitConfigService;

    @Autowired
    private FilterLightService filterLightService;

    @Autowired
    private OutPm25HourlyService outPm25HourlyService;

    Map<String, Integer> pm25Over25Count = new HashMap<>();

    @PostConstruct
    public void loadDataFromDatabase() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("dateDiff", true);
        ResultData response = outPm25DailyService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<OutPm25Daily> list = (List<OutPm25Daily>) response.getData();
            for (OutPm25Daily opd : list) {
                String machineId = opd.getMachineId();
                int overCount = opd.getOverCount();
                pm25Over25Count.put(machineId, overCount);
            }
        }
    }

    //每小时调用,向V2机器发送查询滤网pm2.5的报文
    @GetMapping("/pm25/probe/hourly")
    public ResultData ProbePartialPM2_5Hourly() {
        ResultData result = new ResultData();
        new Thread(() -> {
            //get the qrcode-machine-bind list
            Map<String, Object> condition = new HashMap<>();
            condition.put("blockFlag", false);
            ResultData response = machineQrcodeBindService.fetch(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<MachineQrcodeBindVo> machineQrcodeBindVoList = (List<MachineQrcodeBindVo>) response.getData();
                //foreach the uid, send the packet to the online machine
                for (MachineQrcodeBindVo mqb : machineQrcodeBindVoList) {
                    try {
                        response = coreV2Service.isOnline(mqb.getMachineId().trim());
                        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            coreV2Service.probePartialPm25(mqb.getMachineId());
                        }
                        Thread.sleep(50);
                    } catch (Exception e) {
                        System.out.println(mqb.getMachineId() + " error to judge online");
                        continue;
                    }
                }
            }
        }).start();
        System.out.println("success to probe partial pm2_5 hourly");
        return result;
    }

    //每日24点调用,记录前24小时内pm25平均值以及超阈值次数
    @PostMapping("/pm25/save/daily")
    public ResultData savePm25Daily() {
        ResultData result = new ResultData();
        new Thread(() -> {
            //首先,获取滤网Pm25的阈值
            ResultData response = filterLimitConfigService.fetch(new HashMap<>());
            int overPm25Limit = 25;
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                overPm25Limit = ((List<FilterLimitConfig>) response.getData()).get(0).getOverPm25Limit();
            }

            //然后,从Mongo获取当天所有机器 pm25的统计平均值
            response = machinePm25Service.fetchAveragePm25();
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                //计算超出阈值的连续天数,将 每日平均pm2.5 和 连续超阈值天数记录 到 out_pm2_5_daily 表中
                List<MachinePartialStatus> mpsList = (List<MachinePartialStatus>) response.getData();
                for (MachinePartialStatus mps : mpsList) {
                    String machineId = mps.getUid();
                    Double averagePm25 = ((Double) mps.getData()).doubleValue();
                    int recordOverCount = 0;

                    //如果过去24小时滤网pm2.5平均值小于阈值,则将计数清零
                    if (averagePm25 < overPm25Limit) {
                        pm25Over25Count.put(machineId, 0);
                    }
                    //如果过去24小时pm2.5平均值超标,则根据计数情况分类讨论
                    else {
                        if (pm25Over25Count.containsKey(machineId)) {
                            int overCount = pm25Over25Count.get(machineId);
                            pm25Over25Count.put(machineId, overCount + 1);
                            recordOverCount = overCount + 1;
                        } else {
                            pm25Over25Count.put(machineId, 1);
                            recordOverCount = 1;
                        }
                    }

                    //记录到数据库中
                    try {
                        outPm25DailyService.create(new OutPm25Daily(machineId, averagePm25, recordOverCount));
                    } catch (Exception e) {
                        System.out.println("fail to save this machineId: " + machineId);
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return result;
    }

    //查出超出阈值天数的机器列表
    @GetMapping("/probe/overlimit/list")
    public ResultData probeScreenShouldOn() {
        //首先,获取连续超出阈值的天数上限
        ResultData response = filterLimitConfigService.fetch(new HashMap<>());
        int overCountLimit = 3;
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            overCountLimit = ((List<FilterLimitConfig>) response.getData()).get(0).getOverCountLimit();
        }

        //获取今天零点的时间戳
        Long currentTimestamps = System.currentTimeMillis();
        Long oneDayTimestamps = Long.valueOf(60 * 60 * 24 * 1000);
        Long zero = currentTimestamps - (currentTimestamps + 60 * 60 * 8 * 1000) % oneDayTimestamps;

        //其次,获取应当点亮灯的机器列表.
        Map<String, Object> condition = new HashMap<>();
        condition.put("overCountLimit", overCountLimit);
        condition.put("createAt", new Timestamp(zero));
        condition.put("blockFlag", false);
        return outPm25DailyService.fetch(condition);
    }

    //每日12点调用,根据mysql中的数据来点亮滤网警戒灯.
    @PostMapping("/screen/on/daily")
    public ResultData turnOnScreenDaily() {
        ResultData result = new ResultData();
        new Thread(() -> {
            //找出需要被点亮的警戒灯对应的机器列表
            ResultData response = probeScreenShouldOn();
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                //点亮这些灯
                List<OutPm25Daily> opds = (List<OutPm25Daily>) response.getData();
                for (OutPm25Daily opd : opds) {
                    String machineId = opd.getMachineId();
                    try {
                        if (coreV2Service.isOnline(machineId).getResponseCode() == ResponseCode.RESPONSE_OK) {
                            Map<String, Object> condition = new HashMap<>();
                            condition.put("machineId", machineId);
                            condition.put("blockFlag", false);
                            response = filterLightService.fetch(condition);
                            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                condition.put("lightStatus", true);
                                condition.put("createAt",new Timestamp(System.currentTimeMillis()));
                                filterLightService.update(condition);
                            } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                                filterLightService.create(new FilterLight(machineId, true));
                            }
                            coreV2Service.configScreen(machineId, 1);
                        }
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return result;
    }

    //每小时调用,根据mysql中的数据来关闭滤网灯.
    @PostMapping("/screen/off/hourly")
    public ResultData turnOffScreenHourly() {
        ResultData result = new ResultData();
        new Thread(() -> {
            //获取pm25上限
            ResultData response = filterLimitConfigService.fetch(new HashMap<>());
            int overPm25Limit = 25;
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                overPm25Limit = ((List<FilterLimitConfig>) response.getData()).get(0).getOverPm25Limit();
            }

            //获取前一个小时的所有机器滤网pm2.5数据
            long lastHour = (System.currentTimeMillis() - 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60);
            long currentHour = (System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));
            Map<String, Object> condition = new HashMap<>();
            condition.put("blockFalse", false);
            condition.put("lastHour", new Timestamp(lastHour));
            condition.put("currentHour", new Timestamp(currentHour));
            response = outPm25HourlyService.fetch(condition);

            //关灯
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<OutPm25Hourly> ophs = (List<OutPm25Hourly>) response.getData();
                for (OutPm25Hourly oph : ophs) {
                    String machineId = oph.getMachineId();
                    if (oph.getPm2_5() >= overPm25Limit)
                        continue;
                    try {
                        if (coreV2Service.isOnline(machineId).getResponseCode() == ResponseCode.RESPONSE_OK) {
                            coreV2Service.configScreen(machineId, 0);
                            condition.clear();
                            condition.put("machineId", machineId);
                            condition.put("blockFlag", false);
                            response = filterLightService.fetch(condition);
                            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                condition.put("lightStatus", false);
                                condition.put("createAt", new Timestamp(System.currentTimeMillis()));
                                filterLightService.update(condition);
                            } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                                filterLightService.create(new FilterLight(machineId, false));
                            }
                        }
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return result;
    }
}
