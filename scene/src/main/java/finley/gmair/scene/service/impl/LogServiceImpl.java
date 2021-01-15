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
    public ResultData queryMachineComLog(String uid) {
        ResultData data = logClient.queryMachineComLog(uid);
        log.info("LogService | machine com log is: {}", JSON.toJSONString(data));
        return data;
    }

    @Override
    public ResultData queryUserLog(String uid) {
        ResultData data = logClient.queryUserLog(uid);
        log.info("LogService | user log is: {}", JSON.toJSONString(data));
        return data;
    }
}
