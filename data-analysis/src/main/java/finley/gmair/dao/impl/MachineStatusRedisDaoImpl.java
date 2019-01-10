package finley.gmair.dao.impl;

import finley.gmair.dao.MachineStatusRedisDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class MachineStatusRedisDaoImpl implements MachineStatusRedisDao {

    private Logger logger = LoggerFactory.getLogger(MachineStatusRedisDaoImpl.class);

    @Autowired
    @Resource(name = "MachineStatusRedisTemplate")
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
            logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if (map.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        result.setData(map);
        result.setDescription("success to fetch v1 and v2 data in redis last hour");
        return result;
    }

}
