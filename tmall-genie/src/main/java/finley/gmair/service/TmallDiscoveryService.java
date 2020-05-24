package finley.gmair.service;

import finley.gmair.model.tmallGenie.Payload;
import finley.gmair.util.ResultData;

public interface TmallDiscoveryService {
    /**
     * 包装设备发现返回结果
     * @param resultData 结果
     * @return 包装后结果
     */
    Payload discovery(ResultData resultData);
}
