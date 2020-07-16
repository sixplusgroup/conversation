package finley.gmair.service;

import finley.gmair.model.tmallGenie.AliGenieRe;
import finley.gmair.model.tmallGenie.Header;
import finley.gmair.util.ResultData;

public interface TmallDiscoveryService {
    /**
     * 包装设备发现返回结果
     * @param resultData 结果
     * @param header header
     * @return 包装后结果
     */
    AliGenieRe discovery(ResultData resultData, Header header);
}
