package finley.gmair.model.request;

import finley.gmair.model.domain.UnifiedShop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 14:39
 * @description ：
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchPullRequest {
    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 分页
     */
    private int pageSize;

    /**
     * 根据修改时间排序true, 根据创建时间排序false
     */
    private boolean isSortByModified;

    /**
     * 是否降序
     */
    private boolean isDesc;

    public static BatchPullRequest buildByShopSchedule(UnifiedShop unifiedShop) {
        BatchPullRequest request = new BatchPullRequest();
        //todo:配置同步延迟时间,暂无延迟
        request.setStartTime(unifiedShop.getShopPullInfo().getLastPullTime());
        request.setEndTime(new Date());
        request.setSortByModified(true);
        request.setDesc(true);
        //todo:配置分页
        request.setPageSize(50);
        return request;
    }

    public static BatchPullRequest buildByShopAndModified(UnifiedShop shop, PullRequestDTO requestDTO) {
        BatchPullRequest request = new BatchPullRequest();
        request.setStartTime(requestDTO.getStartTime());
        request.setEndTime(requestDTO.getEndTime());
        request.setSortByModified(requestDTO.getIsSortByModified());
        request.setDesc(true);
        request.setPageSize(50);
        return request;
    }
}
