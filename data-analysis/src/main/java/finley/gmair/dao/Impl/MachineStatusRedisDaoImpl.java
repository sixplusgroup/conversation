package finley.gmair.dao.Impl;

import finley.gmair.dao.MachineStatusRedisDao;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.dataAnalysis.MachineStatusHourly;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Repository
public class MachineStatusRedisDaoImpl implements MachineStatusRedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

    //从redis中读取当前这个小时的所有机器数据，并统计成一个List<MachineStatusHouly>
    @Override
    public ResultData queryHourlyStatus() {
        ResultData result = new ResultData();
        Set<String> keys = redisTemplate.keys("*");
        List<MachineStatusHourly> resultList = new ArrayList<>();
        for (String key : keys) {
            Object queue = redisTemplate.opsForValue().get(key);
            //若这个queue存了v1的status
            if (((LimitQueue<Object>) queue).getLast() instanceof MachineStatus) {
                LinkedList<MachineStatus> list = ((LimitQueue) queue).getLinkedList();
                MachineStatusHourly msh = countV1Status(list);
                if (msh != null)
                    resultList.add(msh);
            }
            //若这个queue存了v2的status
            else if (((LimitQueue<Object>) queue).getLast() instanceof finley.gmair.model.machine.MachineStatus) {
                LinkedList<finley.gmair.model.machine.MachineStatus> list = ((LimitQueue) queue).getLinkedList();
                MachineStatusHourly msh = countV2Status(list);
                if (msh != null)
                    resultList.add(msh);
            }
        }
        result.setData(resultList);
        return result;
    }

    private MachineStatusHourly countV1Status(LinkedList<MachineStatus> list) {
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
        double averageCo2 = list.stream().mapToDouble(MachineStatus::getCo2).average().getAsDouble();
        int maxCo2 = list.stream().mapToInt(MachineStatus::getCo2).max().getAsInt();
        int minCo2 = list.stream().mapToInt(MachineStatus::getCo2).min().getAsInt();
        int powerOnMinute = (int) list.stream().filter(a -> a.getPower() == 1).count() / packetCountForOneMinute;
        int powerOffMinute = (int) list.stream().filter(a -> a.getPower() == 0).count() / packetCountForOneMinute;
        int manualMinute = (int) list.stream().filter(a -> a.getMode() == 0).count() / packetCountForOneMinute;
        int cosyMinute = (int) list.stream().filter(a -> a.getMode() == 1).count() / packetCountForOneMinute;
        int warmMinute = (int) list.stream().filter(a -> a.getMode() == 2).count() / packetCountForOneMinute;
        int heatOnMinute = (int) list.stream().filter(a -> a.getHeat() == 1).count() / packetCountForOneMinute;
        int heatOffMinute = (int) list.stream().filter(a -> a.getHeat() == 0).count() / packetCountForOneMinute;
        String machineId = list.get(0).getUid();
        MachineStatusHourly msh = new MachineStatusHourly(machineId, averagePm25, maxPm25, minPm25, averageVolume, maxVolume, minVolume, averageCo2, maxCo2, minCo2, averageHumid, maxHumid, minHumid, averageCo2, maxCo2, minCo2, powerOnMinute, powerOffMinute, manualMinute, cosyMinute, warmMinute, heatOnMinute, heatOffMinute);
        return msh;
    }

    private MachineStatusHourly countV2Status(LinkedList<finley.gmair.model.machine.MachineStatus> list) {
        int packetCountForOneMinute = 2;
        double averagePm25 = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getPm2_5).average().getAsDouble();
        int maxPm25 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getPm2_5).max().getAsInt();
        int minPm25 = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getPm2_5).min().getAsInt();
        double averageVolume = list.stream().mapToDouble(finley.gmair.model.machine.MachineStatus::getVolume).average().getAsDouble();
        int maxVolume = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getVolume).max().getAsInt();
        int minVolume = list.stream().mapToInt(finley.gmair.model.machine.MachineStatus::getVolume).min().getAsInt();
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
        MachineStatusHourly msh = new MachineStatusHourly(machineId, averagePm25, maxPm25, minPm25, averageVolume, maxVolume, minVolume, averageCo2, maxCo2, minCo2, averageHumid, maxHumid, minHumid, averageCo2, maxCo2, minCo2, powerOnMinute, powerOffMinute, manualMinute, cosyMinute, warmMinute, heatOnMinute, heatOffMinute);
        return msh;
    }
}
