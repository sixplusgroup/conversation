package finley.gmair.controller;


import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.machine.*;
import finley.gmair.service.*;
import finley.gmair.service.impl.RedisService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import finley.gmair.vo.machine.MachinePm2_5Vo;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/machine/status")
public class MachineStatusController {

    @Autowired
    private MachinePm25Service machinePm25Service;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private CoreV2Service coreV2Service;

    @Autowired
    private CoreV1Service coreV1Service;

    @Autowired
    private CoreV3Service coreV3Service;

    @Autowired
    private BoardVersionService boardVersionService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OutPm25DailyService outPm25DailyService;

    @Autowired
    private FilterLimitConfigService overDayService;

    @Autowired
    private FilterLightService filterLightService;

    @Autowired
    private MachinePowerService machinePowerService;

    Map<String, Integer> pm25Over25Count = new HashMap<>();

    //每小时调用,从redis中统计每个机器的pm25每小时平均值,并插入到mysql数据库中machine_hourly_status
    @PostMapping("/schedule/hourly")
    ResultData handleMachineStatusHourly() {
        return machinePm25Service.handleHourly();
    }

    //每天调用,根据machine_hourly_status统计出每个机器的pm25每日平均值
    @PostMapping("/schedule/daily")
    ResultData handleMachineStatusDaily() {
        return machinePm25Service.handleDaily();
    }

    //每月调用,根据machine_daily_status统计出每个机器的pm25每月平均值
    @PostMapping("/schedule/monthly")
    ResultData handleMachineStatusMonthly() {
        return machinePm25Service.handleMonthly();
    }

    //每日调用,从mongo中统计每个机器的每天power开机时间(minute),并插入到mysql数据库中machine_daily_power
    @GetMapping("/power/schedule/daily")
    public ResultData handleMachinePowerDaily() {
        return machinePowerService.handleMachinePowerStatusDaily();
    }

    @GetMapping("/isonline/{qrcode}")
    ResultData isOnline(@PathVariable String qrcode) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);

        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(String.format("cannot find the qrcode %s", qrcode));
            return result;
        } else {
            MachineQrcodeBindVo machineQrcodeBindVo = ((List<MachineQrcodeBindVo>) response.getData()).get(0);
            //根据machineId查board_version表,获取version
            condition.clear();
            condition.put("machineId", machineQrcodeBindVo.getMachineId());
            condition.put("blockFlag", false);
            response = boardVersionService.fetchBoardVersion(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find board version by machineId");
                return result;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to find borad version by machineId");
                return result;
            }
            int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
            switch (version) {
                case 1:
                    response = coreV1Service.isOnline(machineQrcodeBindVo.getMachineId());
                    break;
                case 2:
                    response = coreV2Service.isOnline(machineQrcodeBindVo.getMachineId());
                    break;
                case 3:
                    response = coreV3Service.isOnline(machineQrcodeBindVo.getMachineId());
                    break;
            }
            return response;
        }
    }

    //从mysql获取machine过去24小时的pm2.5记录,并格式化
    @GetMapping("/hourly")
    public ResultData fetchMachineHourlyPm2_5(String qrcode) {

        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode");
            return result;
        }
        //1.according the qrcode ,find machine id
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get machineId by qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find machineId by qrcode");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();

        //2.get the last 24 hour data from database
        Timestamp last24Hour = new Timestamp((System.currentTimeMillis() - 24 * 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60));
        condition.clear();
        condition.put("uid", machineId);
        condition.put("createTimeGTE", last24Hour);
        condition.put("blockFlag", false);
        response = machinePm25Service.fetchMachineHourlyPm25(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch machine hourly pm2.5");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find machine hourly pm2.5");
            return result;
        }

        //3.format time
        List<MachinePm2_5Vo> list = (List<MachinePm2_5Vo>) response.getData();
        List<MachinePm2_5Vo> resultList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            resultList.add(new MachinePm2_5Vo(machineId, 0, 0, new Timestamp(last24Hour.getTime() + (i + 1) * 60 * 60 * 1000)));
        }

        for (int i = 0; i < list.size(); i++) {
            //获取这条记录时间戳整点的时间戳
            Timestamp thatHour = TimeUtil.getThatTimeStampHourTimestamp(list.get(i).getCreateTime());
            long hourDiff = (thatHour.getTime() - last24Hour.getTime()) / (60 * 60 * 1000) - 1;
            if (hourDiff < 0 || hourDiff > 23)
                continue;
            resultList.get((int) hourDiff).setPm2_5(list.get(i).getPm2_5());
        }

        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to find latest pm2.5 by machineId");
        result.setData(resultList);
        return result;
    }

    //从mysql获取machine过去七天的pm2.5记录
    @GetMapping("/daily")
    public ResultData fetchMachineDailyPm2_5(String qrcode) {

        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode");
            return result;
        }
        //根据qrcode查询machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get machineId by qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find machineId by qrcode.");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();

        //通过Calendar获取今日零点零分零秒的毫秒数
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long zero = cal.getTimeInMillis(); // 今天零点零分零秒的毫秒数

        //查询数据库获取室内的过去七天的pm2.5
        condition.clear();
        condition.put("uid", machineId);
        condition.put("dateDiff", System.currentTimeMillis());
        condition.put("blockFlag", false);
        response = machinePm25Service.fetchMachineDailyPm25(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch machine daily pm2.5");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find machine daily pm2.5");
            return result;
        }

        List<MachinePm2_5Vo> list = (List<MachinePm2_5Vo>) response.getData();
        //格式化createTime
        for (int i = 0; i < list.size(); i++) {
            long thatTime = list.get(i).getCreateTime().getTime();
            list.get(i).setCreateTime(new Timestamp(thatTime - (thatTime + 8 * 60 * 60 * 1000) % (24 * 60 * 60 * 1000)));
        }
        while (list.size() > 7) {
            list.remove(0);
        }

        //对缺失的时间补0
        Timestamp last7Day = new Timestamp(zero - 7 * 24 * 60 * 60 * 1000);
        for (int i = 0; list.size() < 7; i++) {
            if (i == list.size() || list.get(i).getCreateTime().getTime() != last7Day.getTime() + (i + 1) * 1000 * 60 * 60 * 24) {
                list.add(i, new MachinePm2_5Vo(machineId, 0, 0, new Timestamp(last7Day.getTime() + (i + 1) * 1000 * 60 * 60 * 24)));
            }
        }
        result.setDescription("list.size = " + list.size());
        result.setData(list);
        return result;
    }

    //根据machineId的List获取前一个小时的pm2.5的List
    @GetMapping("/last/hour/list")
    public ResultData fetchLastHourStatusListByMachineIdList(String machineIdList) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineIdList)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId list");
            return result;
        }

        ResultData response = coreV2Service.onlineList(machineIdList);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        List<String> idList = (List<String>) response.getData();

        //fetch latest status(pm25 co2)
        List<Object> resultList = new ArrayList<>();
        List<finley.gmair.model.machine.v1.MachineStatus> machineV1StatusList = new ArrayList<>();
        List<MachineStatus> machineV2StatusList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        for (String machineId : idList) {
            condition.put("machineId", machineId);
            response = boardVersionService.fetchBoardVersion(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find board version by machineId");
                continue;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to find borad version by machineId");
                continue;
            }
            int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
            switch (version) {
                case 1:
                    LimitQueue<finley.gmair.model.machine.v1.MachineStatus> machineStatusV1Queue = (LimitQueue<finley.gmair.model.machine.v1.MachineStatus>) redisService.get(machineId);
                    if (machineStatusV1Queue != null)
                        machineV1StatusList.add(machineStatusV1Queue.getLast());
                    break;
                case 2:
                    LimitQueue<MachineStatus> statusQueue = (LimitQueue<MachineStatus>) redisService.get(machineId);
                    MachineStatus machineStatusV2 = statusQueue.getLast();
                    if (machineStatusV2 != null)
                        machineV2StatusList.add(machineStatusV2);
                    break;
            }
        }
        if (machineV1StatusList.size() == 0 && machineV2StatusList.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        } else {
            resultList.add(machineV1StatusList);
            resultList.add(machineV2StatusList);
        }
        result.setData(resultList);
        return result;
    }
}
