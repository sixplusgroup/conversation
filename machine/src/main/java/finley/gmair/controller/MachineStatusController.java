package finley.gmair.controller;

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
            }catch (Exception e){
                result.setDescription(e.getMessage());
            }

        }
        return result;
    }

    @PostMapping("/screen/schedule/daily")
    public ResultData configScreenDaily() {
        ResultData result = new ResultData();
        ResultData response = machinePm25Service.fetchAveragePm25();
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch average partial pm2_5");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find average partial pm2_5");
            return result;
        }
        List<MachinePartialStatus> mpsList = (List<MachinePartialStatus>) response.getData();
        for (MachinePartialStatus mps: mpsList) {
            //check online
            response = repositoryService.isOnilne(mps.getUid());
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;

            //if pm2.5 > 25, send the packet
            if(((Double)mps.getData()).doubleValue() < 25.0)
                continue;

            try {
                coreService.configScreen(mps.getUid(),1);
                Thread.sleep(100);
            }catch (Exception e){
                result.setDescription(e.getMessage());
            }
        }

        return result;
    }

    //获取machine过去24小时的pm2.5记录
    @GetMapping("/hourly")
    public ResultData fetchMachineHourlyPm2_5(String qrcode) {

        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide qrcode");
            return result;
        }
        //according the qrcode ,find machine id
        Map<String,Object> condition = new HashMap<>();
        condition.put("codeValue",qrcode);
        condition.put("blockFlag",false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get machineId by qrcode");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find machineId by qrcode");
        }
        String machineId = ((List<MachineQrcodeBindVo>)response.getData()).get(0).getMachineId();

        //get the last 24 hour data from database
        Timestamp last24Hour = new Timestamp((System.currentTimeMillis() - 24 * 1000 * 60 * 60) / (1000 * 60 * 60) * (1000 * 60 * 60));
        Timestamp lastHour = new Timestamp((System.currentTimeMillis()) / (1000 * 60 * 60) * (1000 * 60 * 60));
        condition.clear();
        condition.put("uid",machineId);
        condition.put("createTimeGTE",last24Hour);
        condition.put("createTimeLTE",lastHour);
        condition.put("blockFlag",false);
        response = machinePm25Service.fetchMachineHourlyPm25(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch machine hourly pm2.5");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find machine hourly pm2.5");
        }

        //if some data loss , just fill the 0.
        List<MachinePm2_5Vo> list = (List<MachinePm2_5Vo>)response.getData();
        //+8 是因为东八区,下面这个方法,在数据库中对于某天某个小时有两条数据时就会出错
        int last24Index = (int) ((last24Hour.getTime() / (1000 * 60 * 60) + 8) % 24 );
        for(int i=0;i<24;i++){
            if(list.size()==i || list.get(i).getIndex() != (last24Index+i)%24) {
                list.add(i,new MachinePm2_5Vo(machineId,(last24Index+i)%24,0,new Timestamp(last24Hour.getTime()+(i+1)*60*60*1000)));
            }
        }
        result.setData(list);
        return result;
    }
}
