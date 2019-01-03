package finley.gmair.service.impl;

import finley.gmair.dao.MachinePowerDailyDao;
import finley.gmair.dao.MachineStatusMongoDao;
import finley.gmair.model.machine.MachinePowerDaily;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.service.MachinePowerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    public ResultData create(MachinePowerDaily machinePowerDaily) {
        ResultData result = new ResultData();
        ResultData response = machinePowerDailyDao.insert(machinePowerDaily);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }

    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machinePowerDailyDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch data");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("null");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    public ResultData handleMachinePowerStatusDaily() {
        ResultData resultData = new ResultData();

        //从machine_status和machine_v1_status中查询出mongo中昨日power=1的数据数据
        Map<String, Object> condition = new HashMap<>();
        Timestamp todayZero = TimeUtil.getTodayZeroTimestamp();
        Timestamp lastDayZero = new Timestamp(todayZero.getTime() - 24 * 60 * 60 * 1000);
        condition.put("createAtGTE", lastDayZero.getTime());
        condition.put("createAtLT", todayZero.getTime());
        condition.put("power", 1);
        ResultData response = machineStatusMongoDao.queryMachineV1Status(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            resultData.setResponseCode(response.getResponseCode());
            resultData.setDescription("从mongo查询v1数据时出错");
            return resultData;
        }
        List<MachineV1Status> v1List = (List<MachineV1Status>) response.getData();
        response = machineStatusMongoDao.queryMachineV2Status(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            resultData.setResponseCode(response.getResponseCode());
            resultData.setDescription("从mongo查询v2数据时出错");
            return resultData;
        }
        List<MachineStatus> v2List = (List<MachineStatus>) response.getData();
        List<MachinePowerDaily> v1MachinePowerDailyList = new ArrayList<>();
        List<MachinePowerDaily> v2MachinePowerDailyList = new ArrayList<>();

        if(v1List!=null) {
            //根据获取到的昨天的数据统计所有用户昨日机器开机时长
            int v1PacketInteval = 12;                //一分钟服务器接收到12条一代主板数据报文
            Map<String, Long> v1PowerUsageMap = v1List.stream().collect(
                    Collectors.groupingBy(MachineV1Status::getMachineId, Collectors.counting()));
            v1MachinePowerDailyList = v1PowerUsageMap.entrySet().stream()
                    .map(e -> new MachinePowerDaily(e.getKey(), (int) (e.getValue() / v1PacketInteval)))
                    .collect(Collectors.toList());
        }
        if(v2List!=null) {
            int v2PacketInteval = 2;                  //一分钟服务器接收到2条二代主板数据报文
            Map<String, Long> v2PowerUsageMap = v2List.stream().collect(
                    Collectors.groupingBy(MachineStatus::getUid, Collectors.counting()));
            v2MachinePowerDailyList = v2PowerUsageMap.entrySet().stream()
                    .map(e -> new MachinePowerDaily(e.getKey(), (int) (e.getValue() / v2PacketInteval)))
                    .collect(Collectors.toList());
        }

        //合并两个统计完的数组，插入数据库
        List<MachinePowerDaily> resultList = new ArrayList<>();
        resultList.addAll(v1MachinePowerDailyList);
        resultList.addAll(v2MachinePowerDailyList);
        response = machinePowerDailyDao.insertDailyBatch(resultList);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            resultData.setDescription("成功统计数据");
            return resultData;
        } else {
            resultData.setDescription("fail to insert data");
            return resultData;
        }

    }


}
