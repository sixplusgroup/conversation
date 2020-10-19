package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:30
 * @description ：
 */

public interface TbOrderSyncService {
    /**
     * 全量同步订单
     */
    ResultData fullImport();

    /**
     * 增量同步订单
     */
    ResultData incrementalImport();
}
