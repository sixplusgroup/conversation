package finley.gmair.service;

import finley.gmair.util.ResultData;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/19 23:22
 * @description ：
 */

public interface TbItemSyncService {
    /**
     * 全量引入商品
     *
     */
    ResultData fullImport();
}
