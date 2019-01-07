package finley.gmair.service.Impl;

import finley.gmair.dao.MachineStatusRedisDao;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.dataAnalysis.V1MachineStatusHourly;
import finley.gmair.model.dataAnalysis.V2MachineStatusHourly;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.service.MachineStatusService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MachineStatusServiceImpl implements MachineStatusService {
    @Autowired
    private MachineStatusRedisDao machineStatusRedisDao;

    public ResultData getHourlyStatisticalData() {
        ResultData result = new ResultData();

        //首先从redis中获取前一小时的数据
        ResultData response = machineStatusRedisDao.queryHourlyStatus();
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no machine status found last hour from redis");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error happen when fetch machine status from redis");
            return result;
        }
        Map<String, Object> map = (HashMap) response.getData();

        //统计
        List<Object> resultList = new ArrayList<>();

        try {
            for (String machineId : map.keySet()) {
                Object queue = map.get(machineId);
                //若这个queue存了v1的status
                if (((LimitQueue<Object>) queue).getLast() instanceof MachineStatus) {
                    LinkedList<MachineStatus> list = ((LimitQueue) queue).getLinkedList();
                    V1MachineStatusHourly msh = countV1Status(list);
                    if (msh != null)
                        resultList.add(msh);
                }
                //若这个queue存了v2的status
                else if (((LimitQueue<Object>) queue).getLast() instanceof finley.gmair.model.machine.MachineStatus) {
                    LinkedList<finley.gmair.model.machine.MachineStatus> list = ((LimitQueue) queue).getLinkedList();
                    V2MachineStatusHourly msh = countV2Status(list);
                    if (msh != null)
                        resultList.add(msh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error happen when statistic data");
            return result;
        }
        if (resultList.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("empty list");
            return result;
        }
        result.setData(resultList);
        result.setDescription("success to statistic data");
        return result;
    }


    private V1MachineStatusHourly countV1Status(LinkedList<MachineStatus> list) {
        int packetCountForOneMinute = 12;
        double averagePm25 = list.stream().mapToDouble(MachineStatus::getPm2_5).average().getAsDouble();
        int maxPm25 = list.stream().mapToInt(MachineStatus::getPm2_5).max().getAsInt();
        int minPm25 = list.stream().mapToInt(MachineStatus::getPm2_5).min().getAsInt();
        double averageVolume = list.stream().mapToDouble(MachineStatus::getVolume).average().getAsDouble();
        int maxVolume = list.stream().mapToInt(MachineStatus::getVolume).max().getAsInt();
        int minVolume = list.stream().mapToInt(MachineStatus::getVolume).min().getAsInt();
        double averageHumid = list.stream().mapToDouble(MachineStatus::getHumid).average().getAsDouble();
        int maxHumid = list.stream().mapToInt(MachineStatus::getHumid).max().getAsInt();
        int minHumid = list.stream().mapToInt(MachineStatus::getHumid).min().getAsInt();
        double averageTemp = list.stream().mapToDouble(MachineStatus::getTemp).average().getAsDouble();
        int maxTemp = list.stream().mapToInt(MachineStatus::getTemp).max().getAsInt();
        int minTemp = list.stream().mapToInt(MachineStatus::getTemp).min().getAsInt();
        int powerOnMinute = (int) list.stream().filter(a -> a.getPower() == 1).count() / packetCountForOneMinute;
        int powerOffMinute = (int) list.stream().filter(a -> a.getPower() == 0).count() / packetCountForOneMinute;
        int manualMinute = (int) list.stream().filter(a -> a.getMode() == 0).count() / packetCountForOneMinute;
        int cosyMinute = (int) list.stream().filter(a -> a.getMode() == 1).count() / packetCountForOneMinute;
        int warmMinute = (int) list.stream().filter(a -> a.getMode() == 2).count() / packetCountForOneMinute;
        int heatOnMinute = (int) list.stream().filter(a -> a.getHeat() == 1).count() / packetCountForOneMinute;
        int heatOffMinute = (int) list.stream().filter(a -> a.getHeat() == 0).count() / packetCountForOneMinute;
        String machineId = list.get(0).getUid();
        V1MachineStatusHourly msh = new V1MachineStatusHourly(machineId,averagePm25,maxPm25,minPm25,averageVolume,maxVolume,minVolume,averageTemp,maxTemp,minTemp,averageHumid,maxHumid,minHumid,powerOnMinute,powerOffMinute,manualMinute,cosyMinute,warmMinute,heatOnMinute,heatOffMinute);
        return msh;
    }

    private V2MachineStatusHourly countV2Status(LinkedList<finley.gmair.model.machine.MachineStatus> list) {
        int packetCountForOneMinute = 2;
        double averagePm25 = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getPm2_5).average().getAsDouble();
        int maxPm25 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getPm2_5).max().getAsInt();
        int minPm25 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getPm2_5).min().getAsInt();
        double averageVolume = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getVolume).average().getAsDouble();
        int maxVolume = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getVolume).max().getAsInt();
        int minVolume = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getVolume).min().getAsInt();
        double averageTemp = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getTemp).average().getAsDouble();
        int maxTemp = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getTemp).max().getAsInt();
        int minTemp = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getTemp).min().getAsInt();
        double averageHumid = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getHumid).average().getAsDouble();
        int maxHumid = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getHumid).max().getAsInt();
        int minHumid = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getHumid).min().getAsInt();
        double averageCo2 = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getCo2).average().getAsDouble();
        int maxCo2 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getCo2).max().getAsInt();
        int minCo2 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getCo2).min().getAsInt();
        int powerOnMinute = (int) list.stream().filter(a -> a.getPower() == 1).count() / packetCountForOneMinute;
        int powerOffMinute = (int) list.stream().filter(a -> a.getPower() == 0).count() / packetCountForOneMinute;
        int warmMinute = (int) list.stream().filter(a -> a.getMode() == 2).count() / packetCountForOneMinute;
        int cosyMinute = (int) list.stream().filter(a -> a.getMode() == 1).count() / packetCountForOneMinute;
        int manualMinute = (int) list.stream().filter(a -> a.getMode() == 0).count() / packetCountForOneMinute;
        int heatOnMinute = (int) list.stream().filter(a -> a.getHeat() == 1).count() / packetCountForOneMinute;
        int heatOffMinute = (int) list.stream().filter(a -> a.getHeat() == 0).count() / packetCountForOneMinute;
        String machineId = list.get(0).getUid();
        V2MachineStatusHourly msh = new V2MachineStatusHourly(machineId,averagePm25,maxPm25,minPm25,averageVolume,maxVolume,minVolume,averageTemp,maxTemp,minTemp,averageHumid,maxHumid,minHumid,averageCo2,maxCo2,minCo2,powerOnMinute,powerOffMinute,manualMinute,cosyMinute,warmMinute,heatOnMinute,heatOffMinute);
        return msh;
    }
}
