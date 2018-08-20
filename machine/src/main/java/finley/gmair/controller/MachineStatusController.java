package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachinePm2_5Vo;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
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
    private LatestPM2_5Service latestPM2_5Service;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private CoreService coreService;

    @PostMapping("/schedule/hourly")
    ResultData handleMachineStatusHourly() {
        return machinePm25Service.handleHourly();
    }

    @PostMapping("/schedule/daily")
    ResultData handleMachineStatusDaily() {
        return machinePm25Service.handleDaily();
    }

    @PostMapping("/schedule/monthly")
    ResultData handleMachineStatusMonthly() {
        return machinePm25Service.handleMonthly();
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
            return coreService.isOnline(machineQrcodeBindVo.getMachineId());
        }
    }

    @GetMapping("/partial/schedule/hourly")
    public ResultData ProbePartialPM2_5Hourly() {
        ResultData result = new ResultData();

        //get the qrcode-machine-bind list
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            return result;
        }
        List<MachineQrcodeBindVo> machineQrcodeBindVoList = (List<MachineQrcodeBindVo>) response.getData();

        //foreach the uid, send the packet to the online machine
        StringBuffer sb = new StringBuffer("");
        for (MachineQrcodeBindVo mqb : machineQrcodeBindVoList) {
            response = repositoryService.isOnilne(mqb.getMachineId());
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            //System.out.println(mqb.getMachineId());
            sb.delete(0, sb.length());
            sb.append(mqb.getMachineId());
            coreService.probePartialPm25(sb.toString());
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                result.setDescription(e.getMessage());
            }

        }
        return result;
    }

    //每日调用该接口,检查滤网pm2.5超标的机器,发送警告信号灯
    @PostMapping("/screen/schedule/daily")
    public ResultData configScreenDaily() {
        ResultData result = new ResultData();
        ResultData response = machinePm25Service.fetchAveragePm25();
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch average partial pm2_5");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find average partial pm2_5");
            return result;
        }
        List<MachinePartialStatus> mpsList = (List<MachinePartialStatus>) response.getData();
        for (MachinePartialStatus mps : mpsList) {
            //check online
            response = repositoryService.isOnilne(mps.getUid());
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;

            //if pm2.5 > 25, send the packet
            if (((Double) mps.getData()).doubleValue() < 25.0)
                continue;

            try {
                coreService.configScreen(mps.getUid(), 1);
                Thread.sleep(100);
            } catch (Exception e) {
                result.setDescription(e.getMessage());
            }
        }

        return result;
    }

    //获取machine过去24小时的pm2.5记录
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
        Timestamp lastHour = new Timestamp((System.currentTimeMillis()) / (1000 * 60 * 60) * (1000 * 60 * 60));
        condition.clear();
        condition.put("uid", machineId);
        condition.put("createTimeGTE", last24Hour);
        condition.put("createTimeLTE", lastHour);
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

        //3.format data
        List<MachinePm2_5Vo> list = (List<MachinePm2_5Vo>) response.getData();
        for (int i = 0; i < list.size(); i++) {
            long thatTime = list.get(i).getCreateTime().getTime();
            list.get(i).setCreateTime(new Timestamp((thatTime / (1000 * 60 * 60) * (1000 * 60 * 60))));
        }
        int last24Index = (int) ((last24Hour.getTime() / (1000 * 60 * 60) + 8) % 24);
        for (int i = 0; i < 24; i++) {
            if (list.size() == i || list.get(i).getCreateTime().getTime() != (last24Hour.getTime() + (i + 1) * 60 * 60 * 1000)) {
                list.add(i, new MachinePm2_5Vo(machineId, (last24Index + i) % 24, 0, new Timestamp(last24Hour.getTime() + (i + 1) * 60 * 60 * 1000)));
            }
        }
        result.setData(list);
        return result;
    }

    //获取machine过去七天的pm2.5记录
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
        Timestamp lastDay = new Timestamp(zero);
        Timestamp last7Day = new Timestamp(zero - 7 * 24 * 60 * 60 * 1000);

        //查询数据库获取室内的过去七天的pm2.5
        condition.clear();
        condition.put("uid", machineId);
        condition.put("createTimeGTE", last7Day);
        condition.put("createTimeLTE", lastDay);
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

        //对缺失的时间补0
        for (int i = 0; i < 7; i++) {
            if (list.size() == i || list.get(i).getCreateTime().getTime() != last7Day.getTime() + (i + 1) * 1000 * 60 * 60 * 24) {
                list.add(i, new MachinePm2_5Vo(machineId, 0, 0, new Timestamp(last7Day.getTime() + (i + 1) * 1000 * 60 * 60 * 24)));
            }
        }
        result.setData(list);
        return result;
    }

    //根据machineId的List获取前一个小时的pm2.5的List
    @GetMapping("/last/hour/pm25/list")
    public ResultData fetchLastHourPM25ListByMachineIdList(String machineIdList) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineIdList)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId list");
            return result;
        }

        ResultData response = coreService.onlineList(machineIdList);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }

        List<String> idList = (List<String>)response.getData();
        //fetch hourly pm25
        List<MachinePm2_5Vo> resultList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        Timestamp currentHour = new Timestamp((System.currentTimeMillis()) / (1000 * 60 * 60) * (1000 * 60 * 60));
        Timestamp lastHour = new Timestamp(currentHour.getTime() - 1000 * 60 * 60);
        condition.put("createTimeGTE", lastHour);
        condition.put("createTimeLTE", currentHour);
        condition.put("blockFlag", false);
        for (String machineId : idList) {
            condition.put("machineId", machineId);
            response = machinePm25Service.fetchMachineHourlyPm25(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            resultList.add(((List<MachinePm2_5Vo>) response.getData()).get(0));
        }
        if(resultList.size()==0){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        result.setData(resultList);
        return result;
    }
}
