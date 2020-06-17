package finley.gmair.dao.impl;

import finley.gmair.dao.MachineStatusRedisDao;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.model.machine.v3.MachineStatusV3;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class MachineStatusRedisDaoImpl implements MachineStatusRedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResultData queryHourlyPm25() {
        ResultData result = new ResultData();
        Set<String> keys = redisTemplate.keys("*");
        List<MachinePm2_5> resultList = new ArrayList<>();
        for (String key : keys) {
            Object queue = redisTemplate.opsForValue().get(key);
            if (queue == null) continue;
            //若这个queue存了v1的status
            if (((LimitQueue<Object>) queue).getLast() instanceof MachineStatus) {

                double sumPm2_5 = 0;
                int dataLength = ((LimitQueue<MachineStatus>) queue).size();
                for (int i = 0; i < dataLength; i++) {
                    MachineStatus v1status = ((LimitQueue<MachineStatus>) queue).get(i);
                    sumPm2_5 += v1status.getPm2_5();
                }

                resultList.add(new MachinePm2_5(((LimitQueue<MachineStatus>) queue).getLast().getUid(), sumPm2_5 / dataLength));

            }
            //若这个queue存了v2的status
            else if (((LimitQueue<Object>) queue).getLast() instanceof finley.gmair.model.machine.MachineStatus) {

                double sumPm2_5 = 0;
                int dataLength = ((LimitQueue<finley.gmair.model.machine.MachineStatus>) queue).size();
                for (int i = 0; i < dataLength; i++) {
                    finley.gmair.model.machine.MachineStatus v2status = ((LimitQueue<finley.gmair.model.machine.MachineStatus>) queue).get(i);
                    sumPm2_5 += v2status.getPm2_5();
                }
                resultList.add(new MachinePm2_5(((LimitQueue<finley.gmair.model.machine.MachineStatus>) queue).getLast().getUid(), sumPm2_5 / dataLength));
            }
            //若这个queue存了v3的status
            else if (((LimitQueue<Object>) queue).getLast() instanceof MachineStatusV3) {
                double sumPm2_5 = 0;
                int length = ((LimitQueue<Object>) queue).size();
                for (int i = 0; i < length; i++) {
                    MachineStatusV3 statusV3 = ((LimitQueue<MachineStatusV3>) queue).get(i);
                    sumPm2_5 += statusV3.getPm2_5a();
                }
                resultList.add(new MachinePm2_5(((LimitQueue<MachineStatusV3>) queue).getLast().getUid(), sumPm2_5 / length));
            }
        }
        result.setData(resultList);
        return result;
    }
}
