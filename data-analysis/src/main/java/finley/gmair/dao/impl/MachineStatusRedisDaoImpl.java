package finley.gmair.dao.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.MachineStatusRedisDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.utils.DAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class MachineStatusRedisDaoImpl implements MachineStatusRedisDao {

    private Logger logger = LoggerFactory.getLogger(MachineStatusRedisDaoImpl.class);

    @Autowired
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    //从redis中读取当前这个小时的所有机器数据，并统计成一个List<MachineStatusHouly>
    @Override
    public ResultData queryHourlyStatus() {
        ResultData result = new ResultData();
        Map<String, Object> map = new HashMap<>();
        try {
            Set<String> keys = redisTemplate.keys("*");
            for (String key : keys) {
                try {
                    if(key.length() == 12) {
                        map.put(key, redisTemplate.opsForValue().get(key));
                    }
                } catch (Exception e) {
                    logger.info("[Error] fail to serialize data from redis: " + e.getMessage() + ", key = " + key);
                }
            }
        } catch (Exception e) {
            logger.info("[Error] fail to retrieve data from redis: " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if (map.isEmpty()) {
            logger.info("[Info] no machine data available for the current hour: " + JSON.toJSONString(new Timestamp(DAUtils.hourEntry())));
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        result.setData(map);
        result.setDescription("success to fetch machine data");
        return result;
    }
}
