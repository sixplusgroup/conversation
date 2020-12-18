package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:30
 * @description ：
 */

public interface TbOrderSyncService {
    /**
     * 全量同步订单
     *
     * @return
     */
    ResultData fullImport();

    /**
     * 增量同步订单
     *
     * @return
     */
    ResultData incrementalImport();

    /**
     * 手动同步订单,根据创建时间
     *
     * @param startCreated
     * @param endCreated
     * @return
     */
    ResultData manualImportByCreated(Date startCreated, Date endCreated);

    /**
     * 手动同步订单,根据更新时间
     *
     * @param startModified
     * @param endModified
     * @param startCreated
     * @return
     */
    ResultData manualImportByModified(Date startModified, Date endModified, Date startCreated);

    /**
     * 手动同步订单,根据主订单id
     *
     * @param tid
     * @return
     */
    ResultData manualImportByTid(Long tid);
}
