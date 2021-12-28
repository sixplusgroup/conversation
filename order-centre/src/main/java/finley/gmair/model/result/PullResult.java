package finley.gmair.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-20 20:51
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullResult {

    /**
     * 是否批量
     */
    private boolean isBatch;

    /**
     * 批量请求订单数
     */
    private int tradeSearchNum;

    /**
     * 批量请求订单成功数
     */
    private int tradeSearchFailNum;

    /**
     * 批量请求订单详情成功数
     */
    private int tradeInfoGetNum;

    /**
     * 批量请求订单详情失败数
     */
    private int tradeInfoGetFailNum;


    public static PullResult buildSinglePullResult() {
        return new PullResult(false, 0, 0, 0, 0);

    }

    public static PullResult buildBatchPullResult() {
        return new PullResult(true, 0, 0, 0, 0);
    }

    public void addTradeSearchSuccessNum() {
        this.setTradeSearchNum(this.tradeSearchNum + 1);
    }

    public void addTradeSearchFailNum() {
        this.setTradeSearchNum(this.tradeSearchNum + 1);
        this.setTradeSearchNum(this.tradeSearchFailNum + 1);
    }

    public void addTradeInfoGetSuccessNum() {
        this.setTradeInfoGetNum(this.tradeInfoGetNum + 1);
    }

    public void addTradeInfoGetFailNum() {
        this.setTradeInfoGetNum(this.tradeInfoGetNum + 1);
        this.setTradeSearchNum(this.tradeSearchFailNum + 1);
    }
}
