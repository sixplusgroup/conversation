package finley.gmair.scene.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.scene.client.LogClient;
import finley.gmair.scene.constant.ErrorCode;
import finley.gmair.scene.service.LogService;
import finley.gmair.scene.utils.BizException;
import finley.gmair.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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

    /**
     * 获取用户控制设备的日志
     *
     * @param uid    用户ID（consumerID）
     * @param qrCode 设备二维码（非必需，如果不填，则查询用户的全部控制日志）
     * @return 结果
     */
    @Override
    public ResultData getUserActionLog(String uid, String qrCode) {
        ResultData data = logClient.getUserActionLog(uid, qrCode);
        log.info("LogService | machine com log is: {}", JSON.toJSONString(data));
        if (ObjectUtils.isEmpty(data.getData())) {
            throw new BizException(ErrorCode.USER_ACTION_LOG_ERROR);
        }
        return data;
    }
}
