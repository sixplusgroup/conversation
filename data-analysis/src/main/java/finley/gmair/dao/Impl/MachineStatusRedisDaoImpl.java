package finley.gmair.dao.Impl;

import finley.gmair.dao.MachineStatusRedisDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class MachineStatusRedisDaoImpl implements MachineStatusRedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

    //从redis中读取当前这个小时的所有机器数据，并统计成一个List<MachineStatusHouly>
    @Override
    public ResultData queryHourlyStatus() {
        ResultData result = new ResultData();
        Map<String, Object> map = new HashMap<>();
        try {
            Set<String> keys = redisTemplate.keys("*");
            for (String key : keys) {
                map.put(key,redisTemplate.opsForValue().get(key));
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch machine status last hour in redis");
            return result;
        }
        if (map.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine status last hour last hour");
            return result;
        }
        result.setData(map);
        result.setDescription("success to fetch v1 and v2 data in redis last hour");
        return result;
    }

}
