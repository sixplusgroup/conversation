package finley.gmair.service.impl;

import finley.gmair.dao.MachinePowerDailyDao;
import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachinePowerDaily;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.service.MachinePowerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MachinePowerServiceImpl implements MachinePowerService {
    @Autowired
    private MachineStatusMongoDao machineStatusMongoDao;

    @Autowired
    private MachinePowerDailyDao machinePowerDailyDao;

    public ResultData create(MachinePowerDaily machinePowerDaily){
        ResultData result = new ResultData();
        ResultData response = machinePowerDailyDao.insert(machinePowerDaily);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }

    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = machinePowerDailyDao.query(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch data");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("null");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    public ResultData handleMachinePowerStatusDaily(){
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        Timestamp todayZero = TimeUtil.getTodayZeroTimestamp();
        Timestamp lastDayZero = new Timestamp(todayZero.getTime() - 24 * 60 * 60 * 1000);
        condition.put("createAtGTE", lastDayZero.getTime());
        condition.put("createAtLT", todayZero.getTime());
        condition.put("power", 1);
        ResultData response = machineStatusMongoDao.queryMachineV2Status(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            resultData.setResponseCode(response.getResponseCode());
            resultData.setDescription("mongo中没有昨日数据");
            return resultData;
        }
        List<MachineStatus> list = (List<MachineStatus>)response.getData();

        //根据获取到的昨天的数据统计所有用户昨日机器开机时长
        int packetInteval = 2;                  //一分钟服务器接收到两条二代主板数据报文
        Map<String, Long> powerUsageMap = list.stream().collect(
                Collectors.groupingBy(MachineStatus::getUid, Collectors.counting()));
        List<MachinePowerDaily> machinePowerDailyList = powerUsageMap.entrySet().stream()
                .map(e-> new MachinePowerDaily(e.getKey(), (int)(e.getValue()/packetInteval)))
                .collect(Collectors.toList());
        response = machinePowerDailyDao.insertDailyBatch(machinePowerDailyList);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            resultData.setDescription("成功统计数据");
            resultData.setData(machinePowerDailyList);
            return resultData;
        }else{
            resultData.setDescription("fail to insert data");
            return resultData;
        }
    }


}
