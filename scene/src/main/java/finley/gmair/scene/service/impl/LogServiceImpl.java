package finley.gmair.scene.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.scene.client.LogClient;
import finley.gmair.scene.service.LogService;
import finley.gmair.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : Lyy
 * @create : 2021-01-14 22:14
 **/
@Slf4j
@Service
public class LogServiceImpl implements LogService {

    @Resource
    LogClient logClient;

    @Override
    public ResultData queryLogByUid(String uid) {
        ResultData data = logClient.queryLogByUid(uid);
        log.info("user log is: {}", JSON.toJSONString(data));
        return data;
    }
}
